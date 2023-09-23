package uz.cluster.services.lb;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.cluster.annotation.CheckPermission;
import uz.cluster.dao.lb.MixerDao;
import uz.cluster.entity.lb.Ingredient;
import uz.cluster.entity.lb.Mixer;
import uz.cluster.enums.auth.Action;
import uz.cluster.enums.forms.FormEnum;
import uz.cluster.payload.response.ApiResponse;
import uz.cluster.repository.lb.MixerRepository;
import uz.cluster.util.LanguageManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MixerService {

    private final MixerRepository mixerRepository;

    @CheckPermission(form = FormEnum.MIXER, permission = Action.CAN_VIEW)
    public List<MixerDao> getList() {
        return mixerRepository.findAll().stream().map(Mixer::asDao).collect(Collectors.toList());
    }

    public Map<Integer,String> getHtmlList(){
        Map<Integer,String> htmlOption = new HashMap<>();
        List<Mixer> mixers = mixerRepository.findAll();
        for (Mixer mixer : mixers) {
            htmlOption.put(mixer.getId(), mixer.getAuto());
        }
        return htmlOption;
    }

    public MixerDao getById(int id) {
        Optional<Mixer> optionalIngredient = mixerRepository.findById(id);
        if (optionalIngredient.isEmpty()) {
            return null;
        } else {
            return optionalIngredient.get().asDao();
        }
    }

    @CheckPermission(form = FormEnum.MIXER, permission = Action.CAN_ADD)
    @Transactional
    public ApiResponse add(MixerDao mixerDao) {
        Mixer mixer = mixerDao.copy(mixerDao);

        if (mixer.getId() != 0) {
            return edit(mixer);
        }

        Mixer saved = mixerRepository.save(mixer);
        return new ApiResponse(true, saved, LanguageManager.getLangMessage("saved"));
    }

    @CheckPermission(form = FormEnum.MIXER, permission = Action.CAN_EDIT)
    @Transactional
    public ApiResponse edit(Mixer mixer) {
        Optional<Mixer> optionalMixer = mixerRepository.findById(mixer.getId());

        if (optionalMixer.isPresent()){
            Mixer edited = mixerRepository.save(mixer);
            return new ApiResponse(true, edited, LanguageManager.getLangMessage("edited"));
        }else{
            return new ApiResponse(false, null, LanguageManager.getLangMessage("cant_find"));
        }
    }

    @CheckPermission(form = FormEnum.MIXER, permission = Action.CAN_DELETE)
    @Transactional
    public ApiResponse delete(int id) {
        Optional<Mixer> optionalIngredient = mixerRepository.findById(id);
        if (optionalIngredient.isPresent()){
            mixerRepository.deleteById(id);
            return new ApiResponse(true, LanguageManager.getLangMessage("deleted"));
        }else{
            return new ApiResponse(false, LanguageManager.getLangMessage("cant_find"));
        }
    }
}
