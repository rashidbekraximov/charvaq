package uz.cluster.repository.lb;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.cluster.dao.lb.LBSaleData;
import uz.cluster.dao.purchase.SaleData;
import uz.cluster.entity.lb.LBCost;

import java.util.List;
import java.util.Optional;

@Repository
public interface LBCostRepository extends JpaRepository<LBCost,Integer> {

    List<LBCost> findAllByPurchaseId(long purchaseId);

    @Modifying
    void deleteAllByPurchaseId(long purchaseId);


    @Query(value = "select count(*) num,COALESCE(sum(auto_cost_amount),0) autoCostAmount" +
            ",COALESCE(sum(product_cost_amount),0) productCostAmount from lb_cost e " +
            " WHERE (:beginDate IS NULL OR e.date >=  TO_DATE(:beginDate, 'YYYY-MM-DD')) " +
            " AND (:endDate IS NULL OR e.date <=  TO_DATE(:endDate, 'YYYY-MM-DD')) AND mchj = :mchj", nativeQuery = true)
    List<LBSaleData>  geCountOfNoDebtForBarChart(@Param("beginDate") String beginDate,
                                                        @Param("endDate") String endDate,
                                                        @Param("mchj") String mchj);

}
