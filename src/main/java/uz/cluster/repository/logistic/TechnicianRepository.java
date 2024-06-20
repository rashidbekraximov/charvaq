package uz.cluster.repository.logistic;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.cluster.entity.logistic.Technician;
import uz.cluster.enums.Status;

import java.util.List;
import java.util.Optional;

@Repository
public interface TechnicianRepository extends JpaRepository<Technician,Integer> {

    List<Technician> findAllByStatus(Status status);

}
