package uz.cluster.repository.purchase;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.cluster.entity.purchase.Price;

import java.util.Optional;

@Repository
public interface PriceRepository extends JpaRepository<Price,Integer> {

    Optional<Price> findByProductType_Id(int productType_id);

}
