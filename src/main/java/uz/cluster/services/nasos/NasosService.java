package uz.cluster.services.nasos;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.cluster.entity.nasos.Nasos;
import uz.cluster.payload.response.ApiResponse;
import uz.cluster.repository.nasos.NasosRepository;
import uz.cluster.services.general.SparePartService;
import uz.cluster.util.LanguageManager;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NasosService {

    private final NasosRepository nasosRepository;

    private static final Logger logger = LoggerFactory.getLogger(SparePartService.class);


    //    @CheckPermission(form = FormEnum.NASOS, permission = Action.CAN_VIEW)
    public List<Nasos> getNasos(){
        return nasosRepository.findAll();
    }

//    @CheckPermission(form = FormEnum.PRICE, permission = Action.CAN_ADD)
    @Transactional
    public ApiResponse add(Nasos nasos) {
        Nasos saved = nasosRepository.save(nasos);
        return new ApiResponse(true, saved, LanguageManager.getLangMessage("saved"));
    }

    public ApiResponse delete(long id) {
        Optional<Nasos> optionalNasos = nasosRepository.findById(id);
        if (optionalNasos.isPresent()){
            nasosRepository.deleteById(id);
            logger.info("Filtr ID=>" + id + " tozalandi !");
            return new ApiResponse(true, LanguageManager.getLangMessage("deleted"));
        }else{
            logger.error("Filtr ID=> " + id + "(O'chirish uchun uchun) topilmadi !");
            return new ApiResponse(false, LanguageManager.getLangMessage("cant_find"));
        }
    }
}
