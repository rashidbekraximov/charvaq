package uz.cluster.repository.general;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.cluster.entity.general.SpendingSparePart;
import uz.cluster.entity.general.Warehouse;
import uz.cluster.entity.purchase.CheckoutCost;

import java.util.List;
import java.util.Optional;

@Repository
public interface SpendingSparePartRepository extends JpaRepository<SpendingSparePart,Long> {


    Optional<SpendingSparePart> findBySparePartType_Id(int sparePartType_id);

    @Query(value = "SELECT * FROM spending_spare_part e " +
            "WHERE (:technician_id IS NULL OR :technician_id = 0 OR e.technician_id = :technician_id) " +
            "AND (:beginDate IS NULL OR e.date >=  TO_DATE(:beginDate, 'YYYY-MM-DD')) " +
            "AND (:endDate IS NULL OR e.date <=  TO_DATE(:endDate, 'YYYY-MM-DD')) order by e.date desc",
            nativeQuery = true)
    List<SpendingSparePart> findByParams(
            @Param("technician_id") int technician_id,
            @Param("beginDate") String beginDate,
            @Param("endDate") String endDate
    );

    @Query(value = "SELECT * FROM spending_spare_part e " +
            "WHERE (:technician_id IS NULL OR :technician_id = 0 OR e.technician_id = :technician_id)",
            nativeQuery = true)
    List<SpendingSparePart> findAllByTechnician_Id(@Param("technician_id") int technician_id);

}
