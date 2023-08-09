package uz.cluster.repository.references;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.cluster.entity.references.model.Form;

import java.util.List;
import java.util.Optional;

@Repository
public interface FormRepository extends JpaRepository<Form, Integer> {

    @Query(value = "select * from r_forms t where t.status = 'ACTIVE' order by order_number ", nativeQuery = true)
    List<Form> getForms();

    Optional<Form> findByFormNumber(String formNumber);

    @Query("select f from Form f order by f.orderNumber asc ")
    List<Form> findAllSort();
}