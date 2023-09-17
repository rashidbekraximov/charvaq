package uz.cluster.repository.logistic;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.cluster.entity.logistic.BringDrobilkaProduct;

import java.util.List;


@Repository
public interface BringDrobilkaProductRepository extends JpaRepository<BringDrobilkaProduct,Integer> {


    @Query(value = "select COALESCE(sum(income),0) from bring_drobilka_product ", nativeQuery = true)
    List<Double> getAllIncome();


    @Query(value = "select COALESCE(sum(income),0) from bring_drobilka_product " +
            "where technician_id = :technician_id", nativeQuery = true)
    List<Double> getAllIncomeTechnicianId(@Param("technician_id") int technicianId);

}
