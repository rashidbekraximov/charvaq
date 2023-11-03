package uz.cluster.repository.purchase;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.cluster.entity.purchase.DailyIncome;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DailyIncomeRepository extends JpaRepository<DailyIncome,Long> {

    Optional<DailyIncome> findByDate(LocalDate date);

    Optional<DailyIncome> findByDateAndProductId(LocalDate date,int productId);

    List<DailyIncome> findAllByOrderByStatusAsc();
}
