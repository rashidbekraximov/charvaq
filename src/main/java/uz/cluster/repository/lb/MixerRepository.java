package uz.cluster.repository.lb;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.cluster.entity.lb.Mixer;

@Repository
public interface MixerRepository extends JpaRepository<Mixer,Integer> {
}
