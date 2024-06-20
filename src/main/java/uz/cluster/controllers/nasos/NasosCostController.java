package uz.cluster.controllers.nasos;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.cluster.dao.nasos.NasosCostDao;
import uz.cluster.payload.response.ApiResponse;
import uz.cluster.services.nasos.NasosCostService;

@RestController
@RequestMapping("/api/")
@Tag(name = "Nasos Cost", description = "Nasos cost")
@RequiredArgsConstructor
public class NasosCostController {

    private final NasosCostService nasosService;

    @GetMapping("nasos-costs")
    public ResponseEntity<?> getOrderList() {
        return ResponseEntity.ok(nasosService.getNasos());
    }

    @PostMapping("nasos-cost/save")
    public ResponseEntity<?> save(@RequestBody NasosCostDao nasos) {
        ApiResponse apiResponse = nasosService.add(nasos);
        if (!apiResponse.isSuccess())
            return ResponseEntity.status(HttpStatus.CONFLICT).body(apiResponse);
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @GetMapping("nasos-cost/{id}")
    public ResponseEntity<?> getById(@PathVariable int id) {
        NasosCostDao nasosDao = nasosService.getById(id);
        if (nasosDao == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(nasosDao);
    }

    @GetMapping("nasos-cost/delete/{id}")
    public ResponseEntity<?> save(@PathVariable int id) {
        ApiResponse apiResponse = nasosService.delete(id);
        if (!apiResponse.isSuccess())
            return ResponseEntity.status(HttpStatus.CONFLICT).body(apiResponse);
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
}

