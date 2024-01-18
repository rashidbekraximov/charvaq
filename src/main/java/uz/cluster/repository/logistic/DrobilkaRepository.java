package uz.cluster.repository.logistic;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.cluster.entity.logistic.Drobilka;

import java.util.Optional;

@Repository
public interface DrobilkaRepository extends JpaRepository<Drobilka,Integer> {

    Optional<Drobilka> findByProductType_IdAndDrobilkaType_Id(int productType_id,int drobilkaType_Id);
}
