package uz.cluster.repository.lb;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.cluster.entity.lb.LBPurchase;
import uz.cluster.entity.purchase.Purchase;

import java.util.List;

@Repository
public interface LBPurchaseRepository extends JpaRepository<LBPurchase,Long> {


    @Query(value = "select * from lb_purchase where  debt_total_value != 0 ", nativeQuery = true)
    List<LBPurchase> getAllLBDebts();

    @Query(value = "select * from lb_purchase where  debt_total_value != 0 and lower(customer) like  '%'||:client||'%' ", nativeQuery = true)
    List<LBPurchase> getSearchedLBDebts(@Param("client") String client);

    @Query(value = "select * from lb_purchase p " +
            "order by p.date desc " +
            " LIMIT 6 ", nativeQuery = true)
    List<LBPurchase> getAllByDesc();

    @Query(value = "select count(*) num, p.mark mark, COALESCE(sum(p.amount),0) amount from lb_purchase p " +
            " where (:beginDate IS NULL OR p.date >=  TO_DATE(:beginDate, 'YYYY-MM-DD')) " +
            " AND (:endDate IS NULL OR p.date <=  TO_DATE(:endDate, 'YYYY-MM-DD')) " +
            " AND p.mchj = :mchj " +
            " group by p.mark ", nativeQuery = true)
    List<BarChart> getAllByMarkDaily(@Param("beginDate") String beginDate,
                                     @Param("endDate") String endDate,
                                     @Param("mchj") String mchj);

    @Query(value = "select count(*) from lb_purchase p " +
            " where EXTRACT(MONTH FROM  p.date) = :month_id ", nativeQuery = true)
    long getAllPurchaseNumberForBarChart(@Param("month_id") int month_id);

    @Query(value = "select * from lb_purchase s " +
            " where (:beginDate IS NULL OR s.date >=  TO_DATE(:beginDate, 'YYYY-MM-DD')) " +
            " AND (:endDate IS NULL OR s.date <=  TO_DATE(:endDate, 'YYYY-MM-DD')) " +
            "and nasos != 0", nativeQuery = true)
    List<LBPurchase> getAllPurchaseNumberForBarChartDaily(@Param("beginDate") String beginDate,
                                                          @Param("endDate") String endDate);

    @Query(value = "select count(*) from lb_purchase p " +
            " where EXTRACT(MONTH FROM  p.date) = :month_id and nasos != 0", nativeQuery = true)
    long getAllHireForNasos(@Param("month_id") int month_id);



    @Query(value = "select count(*) num, COALESCE(sum(p.total_value - p.nasos),0) amount from lb_purchase p " +
            " where (:beginDate IS NULL OR p.date >=  TO_DATE(:beginDate, 'YYYY-MM-DD')) " +
            " AND (:endDate IS NULL OR p.date <=  TO_DATE(:endDate, 'YYYY-MM-DD')) and mchj = :mchj" , nativeQuery = true)
    List<BarChart> getAllIncomes(@Param("beginDate") String beginDate,
                                     @Param("endDate") String endDate,
                                     @Param("mchj") String mchj);

}
