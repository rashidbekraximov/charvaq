package uz.cluster.services.purchase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.cluster.entity.purchase.Distribution;
import uz.cluster.repository.purchase.DistributionRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DistributionService {

    private final DistributionRepository distributionRepository;

    public List<Distribution> getList(){
        return distributionRepository.findAll();
    }

}
