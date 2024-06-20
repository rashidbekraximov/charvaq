package uz.cluster.services.lb;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.cluster.repository.lb.LBCostRepository;

@Service
@RequiredArgsConstructor
public class LBCostService {

    private final LBCostRepository lbCostRepository;


}
