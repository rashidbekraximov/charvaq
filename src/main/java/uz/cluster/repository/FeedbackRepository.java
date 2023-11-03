package uz.cluster.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.cluster.entity.Feedback;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback,Integer> {
}
