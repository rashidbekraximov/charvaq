package uz.cluster.repository.forrms;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.cluster.entity.forms.Salary;

import java.util.List;

@Repository
public interface SalaryRepository extends JpaRepository<Salary,Long> {

    List<Salary> findAllByDocumentId(long documentId);

    void deleteAllByDocumentId(long documentId);
}
