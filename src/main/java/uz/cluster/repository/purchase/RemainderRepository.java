package uz.cluster.repository.purchase;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.cluster.entity.purchase.Remainder;
import uz.cluster.enums.MCHJ;
import uz.cluster.enums.SexEnum;

import java.util.List;
import java.util.Optional;

@Repository
public interface RemainderRepository extends JpaRepository<Remainder,Integer> {

    Optional<Remainder> findByProductType_IdAndMchj(int productType_id,MCHJ mchj);

    Optional<Remainder> findByProductType_IdAndSexEnum(int productType_id, SexEnum sexEnum);

    @Query(value = "select * from remainder where mchj like '%'||:mchj||'%'",nativeQuery = true)
    List<Remainder> findAllByMchj(@Param("mchj") String mchj);
}
