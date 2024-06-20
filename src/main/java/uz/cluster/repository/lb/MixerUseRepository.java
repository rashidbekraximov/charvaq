package uz.cluster.repository.lb;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.cluster.entity.lb.MixerUse;

@Repository
public interface MixerUseRepository extends JpaRepository<MixerUse,Long> {
}
