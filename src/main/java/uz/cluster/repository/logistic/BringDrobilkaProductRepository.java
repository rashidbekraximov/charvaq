package uz.cluster.repository.logistic;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.cluster.entity.logistic.BringDrobilkaProduct;

import java.util.List;


@Repository
public interface BringDrobilkaProductRepository extends JpaRepository<BringDrobilkaProduct,Integer> {


    @Query(value = "select COALESCE(sum(e.income),0) from bring_drobilka_product e"
            + " WHERE (:beginDate IS NULL OR e.date >=  TO_DATE(:beginDate, 'YYYY-MM-DD')) " +
            " AND (:endDate IS NULL OR e.date <=  TO_DATE(:endDate, 'YYYY-MM-DD')) " , nativeQuery = true)
    List<Double> getAllIncome(@Param("beginDate") String beginDate,
                              @Param("endDate") String endDate);


    @Query(value = "select COALESCE(sum(income),0) from bring_drobilka_product e" +
            " where technician_id = :technician_id"
            + " and (:beginDate IS NULL OR e.date >=  TO_DATE(:beginDate, 'YYYY-MM-DD')) " +
            " AND (:endDate IS NULL OR e.date <=  TO_DATE(:endDate, 'YYYY-MM-DD')) "
            , nativeQuery = true)
    List<Double> getAllIncomeTechnicianId(@Param("technician_id") int technicianId,@Param("beginDate") String beginDate,
                                          @Param("endDate") String endDate);

}
