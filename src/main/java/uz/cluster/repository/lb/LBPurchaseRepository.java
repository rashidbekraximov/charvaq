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

    @Query(value = "select * from lb_purchase where  debt_total_value != 0  and customer like  '%'||:client||'%' ", nativeQuery = true)
    List<LBPurchase> getSearchedLBDebts(@Param("client") String client);

    @Query(value = "select * from lb_purchase p " +
            "order by p.date desc " +
            " LIMIT 6 ", nativeQuery = true)
    List<LBPurchase> getAllByDesc();

    @Query(value = "select p.mark mark, COALESCE(sum(p.amount),0) amount from lb_purchase p " +
            "where EXTRACT(MONTH FROM  p.date) = EXTRACT(MONTH FROM CURRENT_DATE) group by p.mark ", nativeQuery = true)
    List<BarChart> getAllByMarkMonthly();

    @Query(value = "select p.mark mark, COALESCE(sum(p.amount),0) amount from lb_purchase p " +
            "where p.date = CURRENT_DATE group by p.mark ", nativeQuery = true)
    List<BarChart> getAllByMarkDaily();

    @Query(value = "select count(*) from lb_purchase p " +
            " where EXTRACT(MONTH FROM  p.date) = :month_id ", nativeQuery = true)
    long getAllPurchaseNumberForBarChart(@Param("month_id") int month_id);

    @Query(value = "select * from lb_purchase p " +
            " where EXTRACT(MONTH FROM  p.date) = EXTRACT(MONTH FROM CURRENT_DATE) and nasos != 0 ", nativeQuery = true)
    List<LBPurchase> getAllPurchaseNumberForBarChartMonthly();

    @Query(value = "select * from lb_purchase p " +
            " where p.date = CURRENT_DATE and nasos != 0", nativeQuery = true)
    List<LBPurchase> getAllPurchaseNumberForBarChartDaily();

    @Query(value = "select count(*) from lb_purchase p " +
            " where EXTRACT(MONTH FROM  p.date) = :month_id and nasos != 0", nativeQuery = true)
    long getAllHireForNasos(@Param("month_id") int month_id);

}
