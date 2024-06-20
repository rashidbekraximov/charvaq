package uz.cluster.repository.purchase;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.cluster.entity.purchase.CheckoutCost;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface CheckoutCostRepository extends JpaRepository<CheckoutCost,Long> {

    List<CheckoutCost> findAllByDate(LocalDate date);

    @Query(value = "SELECT * FROM checkout_cost e " +
            "WHERE (:costTypeId IS NULL OR :costTypeId = 0 OR e.cost_type_id = :costTypeId)",
            nativeQuery = true)
    List<CheckoutCost> findAllByCostType_Id(@Param("costTypeId") int costTypeId);

    @Query(value = "SELECT * FROM checkout_cost e " +
            "WHERE (:costTypeId IS NULL OR :costTypeId = 0 OR e.cost_type_id = :costTypeId) " +
            "AND (:beginDate IS NULL OR e.date >=  TO_DATE(:beginDate, 'YYYY-MM-DD')) " +
            "AND (:endDate IS NULL OR e.date <=  TO_DATE(:endDate, 'YYYY-MM-DD')) ",
            nativeQuery = true)
    List<CheckoutCost> findByParams(
            @Param("costTypeId") int costTypeId,
            @Param("beginDate") String beginDate,
            @Param("endDate") String endDate
    );


//    List<CheckoutCost> findAllByCostType_IdAndDate(int costType_id);
}
