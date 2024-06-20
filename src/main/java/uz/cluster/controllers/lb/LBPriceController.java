package uz.cluster.controllers.lb;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.cluster.dao.lb.LBPriceDao;
import uz.cluster.dao.purchase.PriceDao;
import uz.cluster.payload.response.ApiResponse;
import uz.cluster.services.lb.LBPriceService;
import uz.cluster.services.purchase.PriceService;

import java.util.List;

@RestController
@RequestMapping("api/")
@Tag(name = "References LB Mahsulot narxlari", description = "LB Mahsulot narxlari")
@RequiredArgsConstructor
public class LBPriceController {

    private final LBPriceService priceService;

    @GetMapping("lb-prices")
    public ResponseEntity<List<LBPriceDao>> getOrderList() {
        return ResponseEntity.ok(priceService.getPriceList());
    }

    @GetMapping("lb-price/{id}")
    public ResponseEntity<?> getById(@PathVariable int id) {
        LBPriceDao priceDao = priceService.getById(id);
        if (priceDao == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(priceDao);
    }

    @PostMapping("lb-price/save")
    public ResponseEntity<?> save(@RequestBody LBPriceDao priceDao) {
        ApiResponse apiResponse = priceService.add(priceDao);
        if (!apiResponse.isSuccess())
            return ResponseEntity.status(HttpStatus.CONFLICT).body(apiResponse);
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }
}

