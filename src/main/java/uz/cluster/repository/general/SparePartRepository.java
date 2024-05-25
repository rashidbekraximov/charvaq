package uz.cluster.repository.general;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.cluster.entity.general.SparePart;

import java.util.List;
import java.util.Optional;

@Repository
public interface SparePartRepository extends JpaRepository<SparePart,Long> {

    List<SparePart> findAllByOrderByDateDesc();

    Optional<SparePart> findBySparePartType_Id(int sparePartType_id);
}
