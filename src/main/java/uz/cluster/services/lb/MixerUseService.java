package uz.cluster.services.lb;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.cluster.entity.lb.MixerUse;
import uz.cluster.repository.lb.MixerUseRepository;

@Service
@RequiredArgsConstructor
public class MixerUseService {

    private final MixerUseRepository mixerUseRepository;

    public MixerUse saveMixerUse(MixerUse mixerUse) {
        return mixerUseRepository.save(mixerUse);
    }

    // Other CRUD methods
}