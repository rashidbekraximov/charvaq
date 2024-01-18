package uz.cluster.controllers.logistic;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.cluster.dao.logistic.DrobilkaDao;
import uz.cluster.payload.response.ApiResponse;
import uz.cluster.services.logistic.DrobilkaService;

import java.util.List;

@RestController
@RequestMapping("api/")
@Tag(name = "References Drobilka qoldiqlari", description = "Drobilka qoldiqlari")
@RequiredArgsConstructor
public class DrobilkaController {

    private final DrobilkaService drobilkaService;

    @GetMapping("drobilkas")
    public ResponseEntity<List<DrobilkaDao>> getOrderList() {
        return ResponseEntity.ok(drobilkaService.getRemainderList());
    }

    @GetMapping("drobilka/{id}")
    public ResponseEntity<?> getById(@PathVariable int id) {
        DrobilkaDao drobilkaDao = drobilkaService.getById(id);
        if (drobilkaDao == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(drobilkaDao);
    }

    @GetMapping("drobilka/{drobilkaId}/{productId}")
    public ResponseEntity<?> getByDrobilkaAndProductTypeId(@PathVariable int drobilkaId,@PathVariable int productId) {
        DrobilkaDao drobilkaDao = drobilkaService.getByDrobilkaTypeAndProductTypeId(drobilkaId,productId);
        if (drobilkaDao == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(drobilkaDao);
    }

    @PostMapping("drobilka/save")
    public ResponseEntity<?> save(@RequestBody DrobilkaDao drobilkaDao) {
        ApiResponse apiResponse = drobilkaService.add(drobilkaDao);
        if (!apiResponse.isSuccess())
            return ResponseEntity.status(HttpStatus.CONFLICT).body(apiResponse);
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @GetMapping("drobilka/delete/{id}")
    public ResponseEntity<?> save(@PathVariable int id) {
        ApiResponse apiResponse = drobilkaService.delete(id);
        if (!apiResponse.isSuccess())
            return ResponseEntity.status(HttpStatus.CONFLICT).body(apiResponse);
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
}

