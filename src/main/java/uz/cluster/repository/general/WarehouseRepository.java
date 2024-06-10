package uz.cluster.repository.general;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.cluster.entity.general.Warehouse;

import java.util.List;
import java.util.Optional;

@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse,Long> {


    Optional<Warehouse> findBySparePartType_IdAndPrice(int sparePartType_id,double price);

    List<Warehouse> findAllBySparePartType_Id(int sparePartType_id);

}
