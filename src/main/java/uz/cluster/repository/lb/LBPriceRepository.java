package uz.cluster.repository.lb;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.cluster.entity.lb.LBPrice;

import java.util.Optional;

@Repository
public interface LBPriceRepository extends JpaRepository<LBPrice,Integer> {

    Optional<LBPrice> findByProductType_Id(int productType_id);

}
