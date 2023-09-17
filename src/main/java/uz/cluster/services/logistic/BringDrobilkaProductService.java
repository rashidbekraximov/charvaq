package uz.cluster.services.logistic;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.cluster.annotation.CheckPermission;
import uz.cluster.dao.logistic.BringDrobilkaProductDao;
import uz.cluster.entity.logistic.BringDrobilkaProduct;
import uz.cluster.entity.logistic.Drobilka;
import uz.cluster.entity.logistic.Technician;
import uz.cluster.entity.references.model.ProductType;
import uz.cluster.entity.references.model.Unit;
import uz.cluster.enums.auth.Action;
import uz.cluster.enums.forms.FormEnum;
import uz.cluster.payload.response.ApiResponse;
import uz.cluster.repository.logistic.BringDrobilkaProductRepository;
import uz.cluster.repository.logistic.DrobilkaRepository;
import uz.cluster.repository.references.ProductTypeRepository;
import uz.cluster.repository.logistic.TechnicianRepository;
import uz.cluster.repository.references.UnitRepository;
import uz.cluster.services.purchase.RemainderService;
import uz.cluster.util.LanguageManager;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BringDrobilkaProductService {

    private final BringDrobilkaProductRepository bringDrobilkaProductRepository;

    private final DrobilkaRepository drobilkaRepository;

    private final ProductTypeRepository productTypeRepository;

    private final UnitRepository unitRepository;

    private final TechnicianRepository technicianRepository;

    private final LogisticService logisticService;

    private final RemainderService remainderService;

    @CheckPermission(form = FormEnum.BRING_DROBILKA_PRODUCT, permission = Action.CAN_VIEW)
    public List<BringDrobilkaProductDao> getRemainderList() {
        return bringDrobilkaProductRepository.findAll().stream().map(BringDrobilkaProduct::asDao).collect(Collectors.toList());
    }

    public BringDrobilkaProductDao getById(int id) {
        Optional<BringDrobilkaProduct> optionalDrobilka = bringDrobilkaProductRepository.findById(id);
        if (optionalDrobilka.isEmpty()) {
            return null;
        } else {
            return optionalDrobilka.get().asDao();
        }
    }

    @CheckPermission(form = FormEnum.BRING_DROBILKA_PRODUCT, permission = Action.CAN_ADD)
    @Transactional
    public ApiResponse add(BringDrobilkaProductDao bringDrobilkaProductDao) {
        BringDrobilkaProduct bringDrobilkaProduct = bringDrobilkaProductDao.copy(bringDrobilkaProductDao);

        if (bringDrobilkaProduct.getProductTypeId() == 0 || bringDrobilkaProduct.getUnitId() == 0 || bringDrobilkaProduct.getTechnicianId() == 0) {
            return new ApiResponse(false, LanguageManager.getLangMessage("no_data_submitted"));
        }

        Optional<ProductType> optionalProductType = productTypeRepository.findById(bringDrobilkaProduct.getProductTypeId());
        optionalProductType.ifPresent(bringDrobilkaProduct::setProductType);

        Optional<Technician> optionalTechnician = technicianRepository.findById(bringDrobilkaProduct.getTechnicianId());
        optionalTechnician.ifPresent(bringDrobilkaProduct::setTechnician);

        Optional<Unit> optionalUnit = unitRepository.findById(bringDrobilkaProduct.getUnitId());
        optionalUnit.ifPresent(bringDrobilkaProduct::setUnit);

        if (bringDrobilkaProduct.getId() != 0) {
            return edit(bringDrobilkaProduct);
        }

        Optional<Drobilka> optionalDrobilka = drobilkaRepository.findByProductType_Id(bringDrobilkaProduct.getProductTypeId());
        if (optionalDrobilka.isPresent()){
            Drobilka drobilka = optionalDrobilka.get();
            if (drobilka.getAmount() >= bringDrobilkaProduct.getAmount()){
                drobilka.setAmount(drobilka.getAmount() - bringDrobilkaProduct.getAmount());
                drobilkaRepository.save(drobilka);
                BringDrobilkaProduct saved = bringDrobilkaProductRepository.save(bringDrobilkaProduct);
                logisticService.createBringProductLogisticCost(saved);
                remainderService.addByBringing(saved);
                return new ApiResponse(true, saved, LanguageManager.getLangMessage("saved"));
            }else{
                return new ApiResponse(false, LanguageManager.getLangMessage("more_from_remainder"));
            }
        }else{
            return new ApiResponse(false, LanguageManager.getLangMessage("cant_find"));
        }

    }

    @CheckPermission(form = FormEnum.BRING_DROBILKA_PRODUCT, permission = Action.CAN_EDIT)
    @Transactional
    public ApiResponse edit(BringDrobilkaProduct bringDrobilkaProduct) {
        logisticService.deleteAllById(bringDrobilkaProduct.getId());
        Optional<BringDrobilkaProduct> optionalBringDrobilkaProduct = bringDrobilkaProductRepository.findById(bringDrobilkaProduct.getId());
        if (optionalBringDrobilkaProduct.isPresent()){
            Optional<Drobilka> optionalDrobilka = drobilkaRepository.findByProductType_Id(bringDrobilkaProduct.getProductTypeId());
            if (optionalDrobilka.isPresent()){
                Drobilka drobilka = optionalDrobilka.get();
                if ((drobilka.getAmount()+ optionalBringDrobilkaProduct.get().getAmount()) >= bringDrobilkaProduct.getAmount()){
                    drobilka.setAmount((drobilka.getAmount() + optionalBringDrobilkaProduct.get().getAmount()) - bringDrobilkaProduct.getAmount());
                    drobilkaRepository.save(drobilka);
                    remainderService.editByBringing(optionalBringDrobilkaProduct.get(),bringDrobilkaProduct);
                    BringDrobilkaProduct edited = bringDrobilkaProductRepository.save(bringDrobilkaProduct);
                    logisticService.createBringProductLogisticCost(edited);
                    return new ApiResponse(true, edited, LanguageManager.getLangMessage("edited"));
                }else{
                    return new ApiResponse(false, LanguageManager.getLangMessage("more_from_remainder"));
                }
            }else{
                return new ApiResponse(false, LanguageManager.getLangMessage("cant_find"));
            }
        }else{
            return new ApiResponse(false, null, LanguageManager.getLangMessage("cant_find"));
        }
    }

    @CheckPermission(form = FormEnum.BRING_DROBILKA_PRODUCT, permission = Action.CAN_DELETE)
    @Transactional
    public ApiResponse delete(int id) {
        Optional<BringDrobilkaProduct> optionalDrobilka = bringDrobilkaProductRepository.findById(id);
        if (optionalDrobilka.isPresent()){
            bringDrobilkaProductRepository.deleteById(id);
            logisticService.deleteAllById(id);
            return new ApiResponse(true, LanguageManager.getLangMessage("deleted"));
        }else{
            return new ApiResponse(false, LanguageManager.getLangMessage("cant_find"));
        }
    }
}
