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

    @Query(value = "select cost_id costId, COALESCE(sum(amount),0) amount from logistic " +
            "group by cost_id order by cost_id", nativeQuery = true)
    List<LogisticDao> getAllByCost();


    @Query(value = "select cost_id costId, COALESCE(sum(amount),0) amount from logistic " +
            "where technician_id = :technician_id group by cost_id order by cost_id", nativeQuery = true)
    List<LogisticDao> getAllByTechnicianId(@Param("technician_id") int technicianId);


}
