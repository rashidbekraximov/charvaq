package uz.cluster.services;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.cluster.entity.Feedback;
import uz.cluster.repository.FeedbackRepository;

@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;

    public void save(byte star){
        Feedback feedback = new Feedback();
        feedback.setStar(star);
        feedbackRepository.save(feedback);
    }
}
