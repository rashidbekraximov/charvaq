package uz.cluster.controllers.produce;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.cluster.dao.produce.ReadyProductDao;
import uz.cluster.dao.purchase.RemainderDao;
import uz.cluster.entity.produce.ReadyProduct;
import uz.cluster.payload.response.ApiResponse;
import uz.cluster.services.produce.ReadyProductService;
import uz.cluster.services.purchase.RemainderService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("api/")
@Tag(name = "Ishlab chiqarish mahsulot qoldiqlari", description = "mahsulot qoldiqlari")
@RequiredArgsConstructor
public class ReadyProductController {

    private final ReadyProductService remainderService;

    @GetMapping("produced-bar/dates/{sex}")
    public ResponseEntity<List<String>> getBarDateList(@PathVariable String sex) {
        return ResponseEntity.ok(remainderService.getListDate(sex));
    }

    @GetMapping("produced-bar/amounts/{sex}")
    public ResponseEntity<List<Double>> getBarAmountList(@PathVariable String sex) {
        return ResponseEntity.ok(remainderService.getListAmount(sex));
    }

    @GetMapping("ready-products")
    public ResponseEntity<List<ReadyProductDao>> getOrderList() {
        return ResponseEntity.ok(remainderService.getList());
    }

    @GetMapping("ready-product/{id}")
    public ResponseEntity<?> getById(@PathVariable int id) {
        ReadyProductDao readyProduct = remainderService.getById(id);
        if (readyProduct == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(readyProduct);
    }

    @PostMapping("ready-product/save")
    public ResponseEntity<?> save(@RequestBody ReadyProductDao remainderDao) {
        ApiResponse apiResponse = remainderService.add(remainderDao);
        if (!apiResponse.isSuccess())
            return ResponseEntity.status(HttpStatus.CONFLICT).body(apiResponse);
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }
}
