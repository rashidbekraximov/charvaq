package uz.cluster.repository.references;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.cluster.entity.references.model.Employee;

import java.sql.Date;
import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee,Integer> {

    List<Employee> findAllByDirection_Id(int direction_id);

    @Query(value = "select all_hour from documents d left join salary s on s.document_id = d.id  where d.date between :begin_date and :end_date and s.employee_id = :employee_id order by date", nativeQuery = true)
    List<Integer> getAllByDateAndEmployeeId(@Param("begin_date") Date beginDate, @Param("end_date") Date endDate, @Param("employee_id") int employee_id);

}
