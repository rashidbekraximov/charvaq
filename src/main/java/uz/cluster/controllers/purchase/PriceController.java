package uz.cluster.controllers.purchase;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.cluster.dao.purchase.PriceDao;
import uz.cluster.payload.response.ApiResponse;
import uz.cluster.services.purchase.PriceService;

import java.util.List;

@RestController
@RequestMapping("api/")
@Tag(name = "References Mahsulot narxlari", description = "Mahsulot narxlari")
@RequiredArgsConstructor
public class PriceController {

    private final PriceService priceService;

    @GetMapping("prices")
    public ResponseEntity<List<PriceDao>> getOrderList() {
        return ResponseEntity.ok(priceService.getPriceList());
    }

    @GetMapping("price/{id}")
    public ResponseEntity<?> getById(@PathVariable int id) {
        PriceDao priceDao = priceService.getById(id);
        if (priceDao == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(priceDao);
    }

    @GetMapping("price-input/{id}")
    public ResponseEntity<?> getPriceById(@PathVariable int id) {
        return ResponseEntity.status(HttpStatus.OK).body(priceService.getPriceById(id));
    }

    @PostMapping("price/save")
    public ResponseEntity<?> save(@RequestBody PriceDao priceDao) {
        ApiResponse apiResponse = priceService.add(priceDao);
        if (!apiResponse.isSuccess())
            return ResponseEntity.status(HttpStatus.CONFLICT).body(apiResponse);
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }
}
