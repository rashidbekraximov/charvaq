package uz.cluster.repository.references;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.cluster.entity.references.model.PaymentType;

@Repository
public interface PaymentTypeRepository extends JpaRepository<PaymentType,Integer> {
}
