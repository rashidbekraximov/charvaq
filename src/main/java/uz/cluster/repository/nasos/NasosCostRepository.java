package uz.cluster.repository.nasos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.cluster.entity.nasos.NasosCost;

import java.util.List;

@Repository
public interface NasosCostRepository extends JpaRepository<NasosCost,Integer> {


    @Query(value = "SELECT COALESCE(sum(e.amount),0) amount FROM nasos_cost e " +
            "WHERE (:beginDate IS NULL OR e.date >=  TO_DATE(:beginDate, 'YYYY-MM-DD')) " +
            "AND (:endDate IS NULL OR e.date <=  TO_DATE(:endDate, 'YYYY-MM-DD'))",
            nativeQuery = true)
    List<Double> findAllByBetweenDate(
            @Param("beginDate") String beginDate,
            @Param("endDate") String endDate
    );

}
