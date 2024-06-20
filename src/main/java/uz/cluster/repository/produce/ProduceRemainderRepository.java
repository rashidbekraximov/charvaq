package uz.cluster.repository.produce;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.cluster.entity.produce.ProduceRemainder;
import uz.cluster.enums.SexEnum;

import java.util.Optional;

@Repository
public interface ProduceRemainderRepository extends JpaRepository<ProduceRemainder,Long> {


    Optional<ProduceRemainder> findByProductForProduce_IdAndSexEnum(int productForProduce_id, SexEnum sexEnum);

}
