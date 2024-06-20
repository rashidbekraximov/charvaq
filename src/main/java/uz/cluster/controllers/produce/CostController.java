package uz.cluster.controllers.produce;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.cluster.dao.logistic.DashboardLogistic;
import uz.cluster.dao.produce.CostDao;
import uz.cluster.payload.response.ApiResponse;
import uz.cluster.services.produce.CostService;

import java.util.List;

@RestController
@RequestMapping("/api")
@Tag(name = "Ishlab chiqarish xarajatlari", description = "Ishlab chiqarish")
@RequiredArgsConstructor
public class CostController {

    private final CostService costService;

    @GetMapping("costs")
    public ResponseEntity<List<CostDao>> getList() {
        return ResponseEntity.ok(costService.getList());
    }

    @GetMapping("cost/dashboard/{beginDate}/{endDate}/{sex}")
    public ResponseEntity<List<DashboardLogistic>> getDashboardData(@PathVariable String beginDate, @PathVariable String endDate, @PathVariable String sex) {
        return ResponseEntity.ok(costService.getDashboardDataDaily(sex, beginDate, endDate));
    }

    @GetMapping("cost/{id}")
    public ResponseEntity<?> getById(@PathVariable int id) {
        CostDao costDao = costService.getById(id);
        if (costDao == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(costDao);
    }

    @PostMapping("cost/save")
    public ResponseEntity<?> save(@RequestBody CostDao cost) {
        ApiResponse apiResponse = costService.add(cost);
        if (!apiResponse.isSuccess())
            return ResponseEntity.status(HttpStatus.CONFLICT).body(apiResponse);
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @GetMapping("cost/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable int id) {
        ApiResponse apiResponse = costService.delete(id);
        if (!apiResponse.isSuccess())
            return ResponseEntity.status(HttpStatus.CONFLICT).body(apiResponse);
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
}
