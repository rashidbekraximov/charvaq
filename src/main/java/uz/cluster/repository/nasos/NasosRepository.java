package uz.cluster.repository.nasos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.cluster.entity.nasos.Nasos;

@Repository
public interface NasosRepository extends JpaRepository<Nasos,Integer> {
}
