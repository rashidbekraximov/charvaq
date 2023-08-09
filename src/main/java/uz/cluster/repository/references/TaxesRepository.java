package uz.cluster.repository.references;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.cluster.entity.references.model.TaxType;
import uz.cluster.entity.references.model.Taxes;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaxesRepository extends JpaRepository<Taxes,Integer> {

    List<Taxes> findAllByClusterId(int clusterId);

    Optional<Taxes> findByTaxType_Id(int taxTypeId);

    Optional<Taxes> findByTaxTypeAndClusterId(TaxType taxType, int clusterId);

}
