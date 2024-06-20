package uz.cluster.repository.nasos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.cluster.entity.general.SpendingSparePart;
import uz.cluster.entity.nasos.Nasos;

import java.util.List;

@Repository
public interface NasosRepository extends JpaRepository<Nasos,Long> {


    @Query(value = "SELECT COALESCE(sum(e.total_summ),0) allAmount FROM nasos e " +
            "WHERE (:beginDate IS NULL OR e.date >=  TO_DATE(:beginDate, 'YYYY-MM-DD')) " +
            "AND (:endDate IS NULL OR e.date <=  TO_DATE(:endDate, 'YYYY-MM-DD')) ",
            nativeQuery = true)
    List<Double> findAllByBetweenDate(
            @Param("beginDate") String beginDate,
            @Param("endDate") String endDate
    );

}
