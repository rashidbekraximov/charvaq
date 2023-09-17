package uz.cluster.repository.purchase;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.cluster.entity.purchase.Purchase;

import java.util.List;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase,Integer> {

    @Query(value = "select id id,client client,EXTRACT(DAY FROM CURRENT_DATE) - EXTRACT(DAY FROM  expiry_date) days from purchase " +
            "where (EXTRACT(DAY FROM CURRENT_DATE) - EXTRACT(DAY FROM  expiry_date)) >= 0 and debt_total_value != 0 ", nativeQuery = true)
    List<NotificationDto> getAllDebt();

    @Query(value = "select COALESCE(sum(fare),0) from purchase ", nativeQuery = true)
    List<Double> getAllIncome();

    @Query(value = "select * from purchase where  debt_total_value != 0 ", nativeQuery = true)
    List<Purchase> getAllDebts();

    @Query(value = "select * from purchase where  debt_total_value != 0  and client like  '%'||:client||'%' ", nativeQuery = true)
    List<Purchase> getSearchedDebts(@Param("client") String client);

    @Query(value = "select COALESCE(sum(fare),0) from purchase " +
            "where technician_id = :technician_id", nativeQuery = true)
    List<Double> getAllIncomeTechnicianId(@Param("technician_id") int technicianId);

    @Query(value = "select COALESCE(sum(pp.value),0) allAmount, COALESCE(sum(pp.weight),0) weight from purchase s " +
            "left join purchased_product pp " +
            "on s.id = pp.purchase_id where pp.product_type_id = :product_type_id", nativeQuery = true)
    List<PurchaseDto> getAllIncomeProductTypeId(@Param("product_type_id") int productTypeId);

}
