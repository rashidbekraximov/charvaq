package uz.cluster.repository.purchase;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.cluster.entity.purchase.Purchase;

import java.sql.Date;
import java.util.List;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase,Integer> {

    @Query(value = "select id id,client client,location location,debt_total_value debtTotalValue,EXTRACT(DAY FROM CURRENT_DATE) - EXTRACT(DAY FROM  expiry_date) days from purchase " +
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

    @Query(value = "select id from purchase p " +
            " where EXTRACT(MONTH FROM  p.date) = :month_id ", nativeQuery = true)
    List<Integer> getAllPurchaseForLineChart(@Param("month_id") int month_id);

    @Query(value = "select count(*) from purchase p " +
            " where EXTRACT(MONTH FROM  p.date) = EXTRACT(MONTH FROM CURRENT_DATE) ", nativeQuery = true)
    long geCountOfPurchaseForBarChartMonthly();

    @Query(value = "select count(*) from purchase p " +
            " where EXTRACT(MONTH FROM  p.date) = EXTRACT(MONTH FROM CURRENT_DATE) and debt_total_value != 0 ", nativeQuery = true)
    long geCountOfDebtForBarChartMonthly();

    @Query(value = "select count(*) from purchase p " +
            " where EXTRACT(MONTH FROM  p.date) = EXTRACT(MONTH FROM CURRENT_DATE) and debt_total_value = 0 ", nativeQuery = true)
    long geCountOfNoDebtForBarChartMonthly();

    @Query(value = "select count(*) from purchase p " +
            " where p.date = CURRENT_DATE ", nativeQuery = true)
    long geCountOfPurchaseForBarChartDaily();

    @Query(value = "select count(*) from purchase p " +
            " where p.date = CURRENT_DATE and debt_total_value != 0 ", nativeQuery = true)
    long geCountOfDebtForBarChartDaily();

    @Query(value = "select count(*) from purchase p " +
            " where p.date = CURRENT_DATE and debt_total_value = 0 ", nativeQuery = true)
    long geCountOfNoDebtForBarChartDaily();

    @Query(value = "select count(*) from purchase p " +
            " where EXTRACT(MONTH FROM  p.date) = :month_id ", nativeQuery = true)
    long getAllPurchaseNumberForBarChart(@Param("month_id") int month_id);

    @Query(value = "select * from purchase p " +
            " where EXTRACT(MONTH FROM p.date) = EXTRACT(MONTH FROM CURRENT_DATE) order by modified_on desc " +
            " LIMIT 1 ", nativeQuery = true)
    List<Purchase> getAllByDescForTallon();

    @Query(value = "select * from purchase p " +
            "order by p.date desc " +
            " LIMIT 6 ", nativeQuery = true)
    List<Purchase> getAllByDesc();

    @Query(value = "select * from purchase p where p.date = :date or p.paid_date = :date  order by id desc ", nativeQuery = true)
    List<Purchase> getAllByDate(@Param("date") Date date);

    @Query(value = "select COALESCE(sum(pp.value),0) allAmount, COALESCE(sum(pp.weight),0) weight from purchase s " +
            "left join purchased_product pp " +
            "on s.id = pp.purchase_id where EXTRACT(MONTH FROM  s.date) = EXTRACT(MONTH FROM CURRENT_DATE) and pp.product_type_id = :product_type_id", nativeQuery = true)
    List<PurchaseDto> getAllIncomeProductTypeIdMonthly(@Param("product_type_id") int productTypeId);


    @Query(value = "select COALESCE(sum(pp.value),0) allAmount, COALESCE(sum(pp.weight),0) weight from purchase s " +
            "left join purchased_product pp " +
            "on s.id = pp.purchase_id where s.date = CURRENT_DATE and  pp.product_type_id = :product_type_id", nativeQuery = true)
    List<PurchaseDto> getAllIncomeProductTypeIdDaily(@Param("product_type_id") int productTypeId);


}
