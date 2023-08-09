package uz.cluster.repository.references;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.cluster.entity.references.model.TaxType;

@Repository
public interface TaxTypeRepository extends JpaRepository<TaxType,Integer> {

}
