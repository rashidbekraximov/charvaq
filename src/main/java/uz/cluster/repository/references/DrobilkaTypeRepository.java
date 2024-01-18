package uz.cluster.repository.references;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.cluster.entity.references.model.DrobilkaType;

@Repository
public interface DrobilkaTypeRepository extends JpaRepository<DrobilkaType,Integer> {
}
