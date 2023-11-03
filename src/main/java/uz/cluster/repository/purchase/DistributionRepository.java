package uz.cluster.repository.purchase;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.cluster.entity.purchase.Distribution;

@Repository
public interface DistributionRepository extends JpaRepository<Distribution,Integer> {
}
