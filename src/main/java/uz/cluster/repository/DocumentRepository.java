package uz.cluster.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.cluster.entity.Document;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {

    Optional<Document> findByDate(LocalDate date);

    @Query(value = "select * from documents where date between :begin_date and :end_date and direction_id = :direction_id ", nativeQuery = true)
    List<Document> getAllByDateAndDirectionId(@Param("begin_date") Date beginDate, @Param("end_date") Date endDate, @Param("direction_id") int directionId);

}
