package uz.cluster.controllers.nasos;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.cluster.dao.nasos.NasosDao;
import uz.cluster.dao.purchase.OrderDao;
import uz.cluster.entity.nasos.Nasos;
import uz.cluster.payload.response.ApiResponse;
import uz.cluster.services.nasos.NasosService;

@RestController
@RequestMapping("/api/")
@Tag(name = "Nasos", description = "Nasos")
@RequiredArgsConstructor
public class NasosController {

    private final NasosService nasosService;

    @GetMapping("nasos-bar/{beginDate}/{endDate}")
    public ResponseEntity<?> getNasosBar(@PathVariable String beginDate, @PathVariable String endDate) {
        return ResponseEntity.ok(nasosService.getDataList(beginDate,endDate));
    }

    @GetMapping("nasos-services")
    public ResponseEntity<?> getOrderList() {
        return ResponseEntity.ok(nasosService.getNasos());
    }

    @PostMapping("nasos-service/save")
    public ResponseEntity<?> save(@RequestBody NasosDao nasos) {
        ApiResponse apiResponse = nasosService.add(nasos);
        if (!apiResponse.isSuccess())
            return ResponseEntity.status(HttpStatus.CONFLICT).body(apiResponse);
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @GetMapping("nasos-service/{id}")
    public ResponseEntity<?> getById(@PathVariable long id) {
        NasosDao nasosDao = nasosService.getById(id);
        if (nasosDao == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(nasosDao);
    }

    @GetMapping("nasos-service/delete/{id}")
    public ResponseEntity<?> save(@PathVariable int id) {
        ApiResponse apiResponse = nasosService.delete(id);
        if (!apiResponse.isSuccess())
            return ResponseEntity.status(HttpStatus.CONFLICT).body(apiResponse);
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
}

