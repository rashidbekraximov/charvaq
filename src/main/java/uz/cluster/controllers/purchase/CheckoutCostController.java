package uz.cluster.controllers.purchase;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.cluster.entity.purchase.CheckoutCost;
import uz.cluster.payload.response.ApiResponse;
import uz.cluster.services.purchase.CheckoutCostService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api")
@Tag(name = "Xarajatlari", description = "Ishlab chiqarish")
@RequiredArgsConstructor
public class CheckoutCostController {

    private final CheckoutCostService costService;

    @GetMapping("checkout-costs")
    public ResponseEntity<List<CheckoutCost>> getList() {
        return ResponseEntity.ok(costService.getList());
    }

    @GetMapping("checkout-costs/daily/{date}")
    public ResponseEntity<List<CheckoutCost>> getList(@PathVariable String date) {
        return ResponseEntity.ok(costService.getDailyList(LocalDate.parse(date)));
    }

    @GetMapping("checkout-cost/{id}")
    public ResponseEntity<CheckoutCost> getById(@PathVariable long id) {
        return ResponseEntity.ok(costService.getById(id));
    }

    @PostMapping("checkout-cost/save")
    public ResponseEntity<?> save(@RequestBody CheckoutCost cost) {
        ApiResponse apiResponse = costService.add(cost);
        if (!apiResponse.isSuccess())
            return ResponseEntity.status(HttpStatus.CONFLICT).body(apiResponse);
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @GetMapping("checkout-cost/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable int id) {
        ApiResponse apiResponse = costService.delete(id);
        if (!apiResponse.isSuccess())
            return ResponseEntity.status(HttpStatus.CONFLICT).body(apiResponse);
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
}
