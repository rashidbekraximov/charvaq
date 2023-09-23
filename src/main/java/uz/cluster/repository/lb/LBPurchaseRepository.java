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

}
