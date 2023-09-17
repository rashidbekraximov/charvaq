package uz.cluster.controllers.purchase;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.cluster.entity.purchase.Estinguish;
import uz.cluster.payload.response.ApiResponse;
import uz.cluster.services.purchase.EstinguishService;

@RestController
@RequestMapping("/api/estinguish")
@RequiredArgsConstructor
public class EstinguishController {

    private final EstinguishService estinguishService;

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody Estinguish estinguish) {
        ApiResponse apiResponse = estinguishService.add(estinguish);
        if (!apiResponse.isSuccess())
            return ResponseEntity.status(HttpStatus.CONFLICT).body(apiResponse);
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }
}
