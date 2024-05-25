package uz.cluster.repository.purchase;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.cluster.entity.purchase.CheckoutCost;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface CheckoutCostRepository extends JpaRepository<CheckoutCost,Long> {

    List<CheckoutCost> findAllByDate(LocalDate date);

    List<CheckoutCost> findAllByCostType_Id(int costType_id);
}
