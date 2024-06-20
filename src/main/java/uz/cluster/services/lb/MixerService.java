package uz.cluster.services.lb;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.cluster.annotation.CheckPermission;
import uz.cluster.dao.lb.MixerDao;
import uz.cluster.entity.lb.Mixer;
import uz.cluster.enums.Status;
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
@Slf4j
@RequiredArgsConstructor
public class MixerService {

    private final MixerRepository mixerRepository;

    @CheckPermission(form = FormEnum.MIXER, permission = Action.CAN_VIEW)
    public List<MixerDao> getList() {
        return mixerRepository.findAll(Sort.by(Sort.Order.asc("status"))).stream().map(Mixer::asDao).collect(Collectors.toList());
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
            log.error("Bu Id " + id + " haqida ma'lumot topilmadi !");
            return null;
        } else {
            log.info("Bu Id " + id + " bo'yicha malumot chiqarildi !");
            return optionalIngredient.get().asDao();
        }
    }

    @CheckPermission(form = FormEnum.MIXER, permission = Action.CAN_ADD)
    @Transactional
    public ApiResponse add(MixerDao mixerDao) {
        Mixer mixer = mixerDao.copy(mixerDao);

        if (mixer.getId() != 0) {
            log.info("Malumot yangilandi !");
            return edit(mixer);
        }

        Mixer saved = mixerRepository.save(mixer);
        log.info("Ma'lumot saqlandi !");
        return new ApiResponse(true, saved, LanguageManager.getLangMessage("saved"));
    }

    @CheckPermission(form = FormEnum.MIXER, permission = Action.CAN_EDIT)
    @Transactional
    public ApiResponse edit(Mixer mixer) {
        Optional<Mixer> optionalMixer = mixerRepository.findById(mixer.getId());

        if (optionalMixer.isPresent()){
            Mixer edited = mixerRepository.save(mixer);
            log.info("Ma'lumot tahrirlandi :)");
            return new ApiResponse(true, edited, LanguageManager.getLangMessage("edited"));
        }else{
            log.error("Ma'lumotni tahrirlab bo'lmadi :(");
            return new ApiResponse(false, null, LanguageManager.getLangMessage("cant_find"));
        }
    }

    @CheckPermission(form = FormEnum.MIXER, permission = Action.CAN_DELETE)
    @Transactional
    public ApiResponse delete(int id) {
        Optional<Mixer> optionalMixer = mixerRepository.findById(id);
        if (optionalMixer.isPresent()){
            optionalMixer.get().setStatus(Status.PASSIVE);
            mixerRepository.save(optionalMixer.get());
            log.info("Bu Id " + id + " o'chirildi !");
            return new ApiResponse(true, LanguageManager.getLangMessage("deleted"));
        }else{
            log.error("Bu Id " + id + " o'chirib bo'lmadi !");
            return new ApiResponse(false, LanguageManager.getLangMessage("cant_find"));
        }
    }
}
