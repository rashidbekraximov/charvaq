package uz.cluster.controllers.general;


import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.cluster.dao.general.SparePartDao;
import uz.cluster.dao.general.WarehouseDao;
import uz.cluster.payload.response.ApiResponse;
import uz.cluster.services.general.WarehouseService;

@RestController
@RequestMapping("/api/")
@Tag(name = "Omborxona")
@RequiredArgsConstructor
public class WarehouseController {


    private final WarehouseService warehouseService;


    @GetMapping("warehouse")
    public ResponseEntity<?> getById() {
        return ResponseEntity.status(HttpStatus.OK).body(warehouseService.getList());
    }

    @GetMapping("warehouse/price/{id}")
    public ResponseEntity<?> getBySparePartTypeId(@PathVariable int id) {
        return ResponseEntity.status(HttpStatus.OK).body(warehouseService.getBySparePartType(id));
    }

    @GetMapping("warehouse/{id}")
    public ResponseEntity<?> getById(@PathVariable int id) {
        WarehouseDao partDao = warehouseService.getById(id);
        if (partDao == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(partDao);
    }

    @DeleteMapping("warehouse/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable int id) {
        ApiResponse apiResponse = warehouseService.delete(id);
        if (!apiResponse.isSuccess())
            return ResponseEntity.status(HttpStatus.CONFLICT).body(apiResponse);
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

}
