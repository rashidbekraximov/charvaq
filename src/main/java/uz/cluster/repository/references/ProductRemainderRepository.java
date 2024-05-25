package uz.cluster.repository.references;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.cluster.entity.produce.ProduceRemainder;

@Repository
public interface ProductRemainderRepository extends JpaRepository<ProduceRemainder,Long> {
}
