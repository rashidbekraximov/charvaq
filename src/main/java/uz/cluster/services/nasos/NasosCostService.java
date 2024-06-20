package uz.cluster.services.nasos;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.cluster.annotation.CheckPermission;
import uz.cluster.dao.nasos.NasosCostDao;
import uz.cluster.dao.nasos.NasosDao;
import uz.cluster.entity.nasos.Nasos;
import uz.cluster.entity.nasos.NasosCost;
import uz.cluster.entity.references.model.NasosCostType;
import uz.cluster.enums.auth.Action;
import uz.cluster.enums.forms.FormEnum;
import uz.cluster.payload.response.ApiResponse;
import uz.cluster.repository.nasos.NasosCostRepository;
import uz.cluster.repository.nasos.NasosRepository;
import uz.cluster.repository.references.NasosCostTypeRepository;
import uz.cluster.services.general.SparePartService;
import uz.cluster.util.LanguageManager;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class NasosCostService {

    private final NasosCostRepository nasosRepository;

    private final NasosCostTypeRepository nasosCostTypeRepository;

    @CheckPermission(form = FormEnum.NASOS_COST, permission = Action.CAN_VIEW)
    public List<NasosCostDao> getNasos(){
        return nasosRepository.findAll(Sort.by(Sort.Order.desc("date"))).stream().map(NasosCost::asDao).collect(Collectors.toList());
    }

    public NasosCostDao getById(int id){
        Optional<NasosCost> nasos = nasosRepository.findById(id);
        return nasos.map(NasosCost::asDao).orElse(null);
    }

    @CheckPermission(form = FormEnum.NASOS_COST, permission = Action.CAN_ADD)
    @Transactional
    public ApiResponse add(NasosCostDao nasosDao) {
        NasosCost nasos = nasosDao.copy(nasosDao);
        if (nasos.getCostTypeId() == 0){
            return new ApiResponse(false, "Xarajat guruhi tanlanmadi !");
        }
        Optional<NasosCostType> optional = nasosCostTypeRepository.findById(nasos.getCostTypeId());
        optional.ifPresent(nasos::setCostType);

        if (nasos.getId() != 0){
            return edit(nasos);
        }
        NasosCost saved = null;
        try {
            saved = nasosRepository.save(nasos);
            log.info("Nasos service qo'shildi !");
        }catch (Exception e){
            log.error("Nasos serviceni qo'shishda xatolik yuzaga keldi !");
        }
        return new ApiResponse(true, saved, LanguageManager.getLangMessage("saved"));
    }

    @CheckPermission(form = FormEnum.NASOS_COST, permission = Action.CAN_EDIT)
    @Transactional
    public ApiResponse edit(NasosCost nasos) {
        NasosCost saved = null;
        try {
            saved = nasosRepository.save(nasos);
            log.info("Nasos cost o'zgartirildi !");
        }catch (Exception e){
            log.error("Nasos cost o'zgartirishda xatolik yuzaga keldi !");
        }
        return new ApiResponse(true, saved, LanguageManager.getLangMessage("saved"));
    }

    @CheckPermission(form = FormEnum.NASOS_COST, permission = Action.CAN_DELETE)
    public ApiResponse delete(int id) {
        Optional<NasosCost> optionalNasos = nasosRepository.findById(id);
        if (optionalNasos.isPresent()){
            nasosRepository.deleteById(id);
            log.info("Filtr ID=>" + id + " tozalandi !");
            return new ApiResponse(true, LanguageManager.getLangMessage("deleted"));
        }else{
            log.error("Filtr ID=> " + id + "(O'chirish uchun uchun) topilmadi !");
            return new ApiResponse(false, LanguageManager.getLangMessage("cant_find"));
        }
    }
}
