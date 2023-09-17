package uz.cluster.repository.references;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.cluster.entity.references.model.CostType;

@Repository
public interface CostTypeRepository extends JpaRepository<CostType,Integer> {
}
