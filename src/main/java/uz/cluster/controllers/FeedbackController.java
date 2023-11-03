package uz.cluster.controllers;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.cluster.services.FeedbackService;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedbackService feedbackService;

    @GetMapping("/feedback/{star}")
    public void feedback(@PathVariable byte star){
        feedbackService.save(star);
    }

}
