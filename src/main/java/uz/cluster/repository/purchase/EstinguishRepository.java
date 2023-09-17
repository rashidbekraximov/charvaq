package uz.cluster.repository.purchase;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.cluster.entity.purchase.Estinguish;

@Repository
public interface EstinguishRepository extends JpaRepository<Estinguish,Integer> {
}
