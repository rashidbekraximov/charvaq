package uz.cluster.repository.purchase;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.cluster.entity.purchase.Remainder;

import java.util.Optional;

@Repository
public interface RemainderRepository extends JpaRepository<Remainder,Integer> {

    Optional<Remainder> findByProductType_Id(int productType_id);

}
