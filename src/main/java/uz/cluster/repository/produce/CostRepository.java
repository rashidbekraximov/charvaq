package uz.cluster.repository.produce;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.cluster.entity.produce.Cost;
import uz.cluster.repository.logistic.LogisticDao;

import java.util.List;

@Repository
public interface CostRepository extends JpaRepository<Cost,Integer> {

    @Query(value = "select  cost_type_id costId, COALESCE(sum(amount),0) amount from cost cc" +
            " where EXTRACT(MONTH FROM  cc.date) = EXTRACT(MONTH FROM CURRENT_DATE) group by cost_type_id", nativeQuery = true)
    List<LogisticDao> getAllByCostIdMonthly();

    @Query(value = "select  cost_type_id costId, COALESCE(sum(amount),0) amount from cost cc" +
            " where cc.date = CURRENT_DATE group by cost_type_id", nativeQuery = true)
    List<LogisticDao> getAllByCostIdDaily();


}
