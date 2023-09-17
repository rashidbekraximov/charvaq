package uz.cluster.controllers.logistic;


import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.cluster.services.logistic.LogisticService;

@RestController
@RequestMapping("/api/")
@Tag(name = "References Logistika dashboard", description = "Logisitika dashboard")
@RequiredArgsConstructor
public class LogisticController {

    private final LogisticService logisticService;

    @GetMapping("logistic/all")
    public ResponseEntity<?> getTotals(){
        return ResponseEntity.ok(logisticService.getTotalAllCost());
    }

    @GetMapping("logistic/table")
    public ResponseEntity<?> getByTechniqueList(){
        return ResponseEntity.ok(logisticService.getTechnicianList());
    }

}
