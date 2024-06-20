package uz.cluster.repository.logistic;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.cluster.entity.logistic.Logistic;

import java.util.List;

@Repository
public interface LogisticRepository extends JpaRepository<Logistic,Integer> {

    void deleteAllByDocumentId(int documentId);

    @Query(value = "select e.cost_id costId, COALESCE(sum(e.amount),0) amount from logistic e" +
            " WHERE (:beginDate IS NULL OR e.date >=  TO_DATE(:beginDate, 'YYYY-MM-DD')) " +
            " AND (:endDate IS NULL OR e.date <=  TO_DATE(:endDate, 'YYYY-MM-DD')) "+
            " group by e.cost_id order by e.cost_id", nativeQuery = true)
    List<LogisticDao> getAllByCost(@Param("beginDate") String beginDate,
                                   @Param("endDate") String endDate);


    @Query(value = "select e.cost_id costId, COALESCE(sum(e.amount),0) amount from logistic e" +
            " where e.technician_id = :technician_id and " +
            " (:beginDate IS NULL OR e.date >=  TO_DATE(:beginDate, 'YYYY-MM-DD')) " +
            " AND (:endDate IS NULL OR e.date <=  TO_DATE(:endDate, 'YYYY-MM-DD')) "+
            " group by e.cost_id order by e.cost_id", nativeQuery = true)
    List<LogisticDao> getAllByTechnicianId(@Param("technician_id") int technicianId,@Param("beginDate") String beginDate,
                                           @Param("endDate") String endDate);


}
