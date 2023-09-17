package uz.cluster.controllers.logistic;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.cluster.dao.logistic.BringDrobilkaProductDao;
import uz.cluster.payload.response.ApiResponse;
import uz.cluster.services.logistic.BringDrobilkaProductService;

import java.util.List;

@RestController
@RequestMapping("api/")
@Tag(name = "References Bring Drobilka qoldiqlari", description = "Bring Drobilka qoldiqlari")
@RequiredArgsConstructor
public class BringDrobilkaProductController {

    private final BringDrobilkaProductService bringDrobilkaProductService;

    @GetMapping("bring-drobilka-products")
    public ResponseEntity<List<BringDrobilkaProductDao>> getOrderList() {
        return ResponseEntity.ok(bringDrobilkaProductService.getRemainderList());
    }

    @GetMapping("bring-drobilka-product/{id}")
    public ResponseEntity<?> getById(@PathVariable int id) {
        BringDrobilkaProductDao drobilkaDao = bringDrobilkaProductService.getById(id);
        if (drobilkaDao == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(drobilkaDao);
    }

    @PostMapping("bring-drobilka-product/save")
    public ResponseEntity<?> save(@RequestBody BringDrobilkaProductDao drobilkaDao) {
        ApiResponse apiResponse = bringDrobilkaProductService.add(drobilkaDao);
        if (!apiResponse.isSuccess())
            return ResponseEntity.status(HttpStatus.CONFLICT).body(apiResponse);
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @GetMapping("bring-drobilka-product/delete/{id}")
    public ResponseEntity<?> save(@PathVariable int id) {
        ApiResponse apiResponse = bringDrobilkaProductService.delete(id);
        if (!apiResponse.isSuccess())
            return ResponseEntity.status(HttpStatus.CONFLICT).body(apiResponse);
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
}

