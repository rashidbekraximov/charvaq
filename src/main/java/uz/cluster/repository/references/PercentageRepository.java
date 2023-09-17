package uz.cluster.repository.references;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.cluster.entity.references.model.Percentage;

import java.util.List;

@Repository
public interface PercentageRepository extends JpaRepository<Percentage,Integer> {

    void deleteAllByEmployeeId(int employeeId);

    List<Percentage> findAllByEmployeeId(int employeeId);

}
