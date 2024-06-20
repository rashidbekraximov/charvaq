package uz.cluster.repository.produce;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.cluster.entity.produce.Cost;
import uz.cluster.repository.logistic.LogisticDao;

import java.util.List;

@Repository
public interface CostRepository extends JpaRepository<Cost,Integer> {

    @Query(value = "select cc.spending_type_id spendingTypeId, COALESCE(sum(amount),0) amount from cost cc" +
            " where cc.sex = :sex and EXTRACT(MONTH FROM  cc.date) = EXTRACT(MONTH FROM CURRENT_DATE) group by spending_type_id", nativeQuery = true)
    List<ProduceBarDao> getAllByCostIdMonthly(@Param("sex") String sex);

    @Query(value = "select cc.spending_type_id spendingTypeId, COALESCE(sum(amount),0) amount from cost cc" +
            " where cc.sex = :sex and " +
            " (:beginDate IS NULL OR cc.date >=  TO_DATE(:beginDate, 'YYYY-MM-DD')) " +
            " AND (:endDate IS NULL OR cc.date <=  TO_DATE(:endDate, 'YYYY-MM-DD')) " +
            " group by spending_type_id", nativeQuery = true)
    List<ProduceBarDao> getAllByCostIdDaily(@Param("sex") String sex,@Param("beginDate") String beginDate,
                                            @Param("endDate") String endDate);

}
