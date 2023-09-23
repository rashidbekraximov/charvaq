package uz.cluster.repository.purchase;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.cluster.entity.purchase.Remainder;
import uz.cluster.enums.MCHJ;

import java.util.List;
import java.util.Optional;

@Repository
public interface RemainderRepository extends JpaRepository<Remainder,Integer> {

    Optional<Remainder> findByProductType_IdAndMchj(int productType_id,MCHJ mchj);

    List<Remainder> findAllByMchj(MCHJ mchj);
}
