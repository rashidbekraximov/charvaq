package uz.cluster.repository.purchase;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.cluster.dao.purchase.SaleData;
import uz.cluster.entity.purchase.Purchase;

import java.sql.Date;
import java.util.List;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase,Integer> {

    @Query(value = "select id id,client client,location location,debt_total_value debtTotalValue,EXTRACT(DAY FROM  expiry_date) - EXTRACT(DAY FROM CURRENT_DATE) days from purchase " +
            "where debt_total_value != 0 LIMIT :limit ", nativeQuery = true)
    List<NotificationDto> getAllDebt(@Param("limit") int limit);

    @Query(value = "select COALESCE(sum(e.fare),0) from purchase e"
            +" WHERE (:beginDate IS NULL OR e.date >=  TO_DATE(:beginDate, 'YYYY-MM-DD')) " +
            " AND (:endDate IS NULL OR e.date <=  TO_DATE(:endDate, 'YYYY-MM-DD')) ", nativeQuery = true)
    List<Double> getAllIncome(@Param("beginDate") String beginDate,
                              @Param("endDate") String endDate);

    @Query(value = "select * from purchase where  debt_total_value != 0 ", nativeQuery = true)
    List<Purchase> getAllDebts();

    @Query(value = "select * from purchase where  debt_total_value != 0  and lower(client) like  '%'||:client||'%' ", nativeQuery = true)
    List<Purchase> getSearchedDebts(@Param("client") String client);

    @Query(value = "select COALESCE(sum(fare),0) from purchase e" +
            " where technician_id = :technician_id"
            + " AND (:beginDate IS NULL OR e.date >=  TO_DATE(:beginDate, 'YYYY-MM-DD')) " +
            " AND (:endDate IS NULL OR e.date <=  TO_DATE(:endDate, 'YYYY-MM-DD')) "
            , nativeQuery = true)
    List<Double> getAllIncomeTechnicianId(@Param("technician_id") int technicianId,@Param("beginDate") String beginDate,
                                          @Param("endDate") String endDate);

    @Query(value = "select id from purchase p " +
            " where EXTRACT(MONTH FROM  p.date) = :month_id ", nativeQuery = true)
    List<Integer> getAllPurchaseForLineChart(@Param("month_id") int month_id);

    @Query(value = "select count(*) num,COALESCE(sum(total_value),0) amount from purchase e  WHERE (:beginDate IS NULL OR e.date >=  TO_DATE(:beginDate, 'YYYY-MM-DD')) " +
            " AND (:endDate IS NULL OR e.date <=  TO_DATE(:endDate, 'YYYY-MM-DD')) ", nativeQuery = true)
    List<SaleData> geCountOfPurchaseForBarChartMonthly(@Param("beginDate") String beginDate,
                                                       @Param("endDate") String endDate);

    @Query(value = "select count(*) num,COALESCE(sum(debt_total_value),0) amount from purchase e " +
            " WHERE (:beginDate IS NULL OR e.date >=  TO_DATE(:beginDate, 'YYYY-MM-DD')) " +
            " AND (:endDate IS NULL OR e.date <=  TO_DATE(:endDate, 'YYYY-MM-DD')) and debt_total_value != 0 ", nativeQuery = true)
    List<SaleData> geCountOfDebtForBarChartMonthly(@Param("beginDate") String beginDate,
                                                   @Param("endDate") String endDate);

    @Query(value = "select count(*) num,COALESCE(sum(paid_total_value),0) amount from purchase e " +
            " WHERE (:beginDate IS NULL OR e.date >=  TO_DATE(:beginDate, 'YYYY-MM-DD')) " +
            " AND (:endDate IS NULL OR e.date <=  TO_DATE(:endDate, 'YYYY-MM-DD')) and debt_total_value = 0 ", nativeQuery = true)
    List<SaleData>  geCountOfNoDebtForBarChartMonthly(@Param("beginDate") String beginDate,
                                           @Param("endDate") String endDate);

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
            "on s.id = pp.purchase_id where pp.sex = :sex and EXTRACT(MONTH FROM  s.date) = EXTRACT(MONTH FROM CURRENT_DATE) and pp.product_type_id = :product_type_id", nativeQuery = true)
    List<PurchaseDto> getAllIncomeProductTypeIdMonthly(@Param("product_type_id") int productTypeId,@Param("sex") String sex);


    @Query(value = "select COALESCE(sum(pp.value),0) allAmount, COALESCE(sum(pp.weight),0) weight from purchase s " +
            "left join purchased_product pp " +
            "on s.id = pp.purchase_id " +
            "where pp.sex = :sex and" +
            " (:beginDate IS NULL OR s.date >=  TO_DATE(:beginDate, 'YYYY-MM-DD')) " +
            " AND (:endDate IS NULL OR s.date <=  TO_DATE(:endDate, 'YYYY-MM-DD')) " +
            "and  pp.product_type_id = :product_type_id", nativeQuery = true)
    List<PurchaseDto> getAllIncomeProductTypeIdDaily(@Param("product_type_id") int productTypeId,@Param("sex") String sex,@Param("beginDate") String beginDate,
                                                     @Param("endDate") String endDate);


}
