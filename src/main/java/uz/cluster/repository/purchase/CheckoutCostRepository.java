package uz.cluster.repository.purchase;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.cluster.entity.purchase.CheckoutCost;

@Repository
public interface CheckoutCostRepository extends JpaRepository<CheckoutCost,Long> {
}
