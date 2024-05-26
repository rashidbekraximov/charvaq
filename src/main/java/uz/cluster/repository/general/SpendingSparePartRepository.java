package uz.cluster.repository.general;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.cluster.entity.general.SpendingSparePart;
import uz.cluster.entity.general.Warehouse;

import java.util.Optional;

@Repository
public interface SpendingSparePartRepository extends JpaRepository<SpendingSparePart,Long> {


    Optional<SpendingSparePart> findBySparePartType_Id(int sparePartType_id);


}
