package uz.cluster.repository.general;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.cluster.entity.general.Warehouse;
import uz.cluster.enums.ItemEnum;

import java.util.List;
import java.util.Optional;

@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse,Long> {


    Optional<Warehouse> findBySparePartType_IdAndPrice(int sparePartType_id,double price);

    Optional<Warehouse> findByFuelType_IdAndPrice(int fuelType_id,double price);

    List<Warehouse> findAllByItemAndSparePartType_Id(ItemEnum item,int sparePartType_id);

    List<Warehouse> findAllByItemAndFuelType_Id(ItemEnum item,int fuelType_id);

    List<Warehouse> findAllByItem(ItemEnum item);


}
