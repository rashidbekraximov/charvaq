package uz.cluster.controllers.purchase;


import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.cluster.entity.purchase.Distribution;
import uz.cluster.services.purchase.DistributionService;

import java.util.List;

@RestController
@RequestMapping("/api")
@Tag(name = "Taqsimlash", description = "Ulushni taqsimlash")
@RequiredArgsConstructor
public class DistributionController {

    private final DistributionService distributionService;

    @GetMapping("/distribution")
    public ResponseEntity<List<Distribution>> getList() {
        return ResponseEntity.ok(distributionService.getList());
    }

}
