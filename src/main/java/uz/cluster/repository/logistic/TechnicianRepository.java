package uz.cluster.repository.logistic;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.cluster.entity.logistic.Technician;

import java.util.Optional;

@Repository
public interface TechnicianRepository extends JpaRepository<Technician,Integer> {

    Optional<Technician> findByEmployee_Id(int employee_id);

}
