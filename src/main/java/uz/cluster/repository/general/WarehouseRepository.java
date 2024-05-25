package uz.cluster.repository.general;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.cluster.entity.general.Warehouse;

@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse,Long> {
}
