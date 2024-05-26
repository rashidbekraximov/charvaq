package uz.cluster.services.nasos;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.cluster.annotation.CheckPermission;
import uz.cluster.entity.nasos.Nasos;
import uz.cluster.enums.auth.Action;
import uz.cluster.enums.forms.FormEnum;
import uz.cluster.payload.response.ApiResponse;
import uz.cluster.repository.nasos.NasosRepository;
import uz.cluster.util.LanguageManager;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NasosService {

    private final NasosRepository nasosRepository;

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
}
