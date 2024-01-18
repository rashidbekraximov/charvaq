package uz.cluster.services.logistic;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.cluster.annotation.CheckPermission;
import uz.cluster.dao.logistic.DrobilkaDao;
import uz.cluster.entity.logistic.Drobilka;
import uz.cluster.entity.references.model.DrobilkaType;
import uz.cluster.entity.references.model.ProductType;
import uz.cluster.entity.references.model.Unit;
import uz.cluster.enums.auth.Action;
import uz.cluster.enums.forms.FormEnum;
import uz.cluster.payload.response.ApiResponse;
import uz.cluster.repository.logistic.DrobilkaRepository;
import uz.cluster.repository.references.DrobilkaTypeRepository;
import uz.cluster.repository.references.ProductTypeRepository;
import uz.cluster.repository.references.UnitRepository;
import uz.cluster.util.LanguageManager;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DrobilkaService {

    private final DrobilkaRepository drobilkaRepository;

    private final ProductTypeRepository productTypeRepository;

    private final DrobilkaTypeRepository drobilkaTypeRepository;

    private final UnitRepository unitRepository;

    @CheckPermission(form = FormEnum.DROBILKA, permission = Action.CAN_VIEW)
    public List<DrobilkaDao> getRemainderList() {
        return drobilkaRepository.findAll().stream().map(Drobilka::asDao).collect(Collectors.toList());
    }

    public DrobilkaDao getById(int id) {
        Optional<Drobilka> optionalDrobilka = drobilkaRepository.findById(id);
        if (optionalDrobilka.isEmpty()) {
            return null;
        } else {
            return optionalDrobilka.get().asDao();
        }
    }

    public DrobilkaDao getByDrobilkaTypeAndProductTypeId(int drobilkaId,int productTypeId) {
        Optional<Drobilka> optionalDrobilka = drobilkaRepository.findByProductType_IdAndDrobilkaType_Id(productTypeId,drobilkaId);
        if (optionalDrobilka.isEmpty()) {
            return null;
        } else {
            return optionalDrobilka.get().asDao();
        }
    }

    @CheckPermission(form = FormEnum.DROBILKA, permission = Action.CAN_ADD)
    @Transactional
    public ApiResponse add(DrobilkaDao drobilkaDao) {
        Drobilka drobilka = drobilkaDao.copy(drobilkaDao);

        if (drobilka.getDrobilkaTypeId() == 0 || drobilka.getProductTypeId() == 0 || drobilka.getUnitId() == 0) {
            return new ApiResponse(false, LanguageManager.getLangMessage("no_data_submitted"));
        }

        Optional<ProductType> optionalProductType = productTypeRepository.findById(drobilka.getProductTypeId());
        optionalProductType.ifPresent(drobilka::setProductType);

        Optional<DrobilkaType> optionalDrobilkaType = drobilkaTypeRepository.findById(drobilka.getDrobilkaTypeId());
        optionalDrobilkaType.ifPresent(drobilka::setDrobilkaType);

        Optional<Unit> optionalUnit = unitRepository.findById(drobilka.getUnitId());
        optionalUnit.ifPresent(drobilka::setUnit);

        if (drobilka.getId() != 0) {
            return edit(drobilka);
        }

        Optional<Drobilka> optionalDrobilka = drobilkaRepository.findByProductType_IdAndDrobilkaType_Id(drobilka.getProductTypeId(),drobilka.getDrobilkaTypeId());
        if (optionalDrobilka.isPresent()){
            optionalDrobilka.get().setAmount(drobilka.getAmount() + optionalDrobilka.get().getAmount());
            Drobilka remainderSaved = drobilkaRepository.save(optionalDrobilka.get());
            return new ApiResponse(true, remainderSaved, LanguageManager.getLangMessage("saved"));
        }

        Drobilka remainderSaved = drobilkaRepository.save(drobilka);
        return new ApiResponse(true, remainderSaved, LanguageManager.getLangMessage("saved"));
    }

    @CheckPermission(form = FormEnum.DROBILKA, permission = Action.CAN_EDIT)
    @Transactional
    public ApiResponse edit(Drobilka drobilka) {
        Optional<Drobilka> optionalDrobilka = drobilkaRepository.findById(drobilka.getId());
        if (optionalDrobilka.isPresent()){
            Drobilka edited = drobilkaRepository.save(drobilka);
            return new ApiResponse(true, edited, LanguageManager.getLangMessage("edited"));
        }else{
            return new ApiResponse(false, null, LanguageManager.getLangMessage("cant_find"));
        }
    }

    @CheckPermission(form = FormEnum.DROBILKA, permission = Action.CAN_DELETE)
    @Transactional
    public ApiResponse delete(int id) {
        Optional<Drobilka> optionalDrobilka = drobilkaRepository.findById(id);
        if (optionalDrobilka.isPresent()){
            drobilkaRepository.deleteById(id);
            return new ApiResponse(true, LanguageManager.getLangMessage("deleted"));
        }else{
            return new ApiResponse(false, LanguageManager.getLangMessage("cant_find"));
        }
    }
}
