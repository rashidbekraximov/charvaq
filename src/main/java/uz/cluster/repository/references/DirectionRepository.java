package uz.cluster.repository.references;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.cluster.entity.references.model.Direction;

@Repository
public interface DirectionRepository extends JpaRepository<Direction,Integer> {

}
