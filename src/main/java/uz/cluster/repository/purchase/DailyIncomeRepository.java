package uz.cluster.repository.purchase;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.cluster.entity.purchase.DailyIncome;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DailyIncomeRepository extends JpaRepository<DailyIncome,Long> {

    Optional<DailyIncome> findByDate(LocalDate date);

    @Query(value = "select * from daily_income d order by d.date DESC , d.status ASC ", nativeQuery = true)
    List<DailyIncome> findAllByOrderByStatusAsc();
}
