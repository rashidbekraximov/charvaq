package uz.cluster.controllers.purchase;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.cluster.dao.purchase.DailyIncomeDao;
import uz.cluster.entity.purchase.DailyIncome;
import uz.cluster.services.purchase.DailyIncomeService;

import java.util.List;

@RestController
@RequestMapping("api/")
@Tag(name = "Daily Income", description = "Kunlik tasdiqlash")
@RequiredArgsConstructor
public class DailyIncomeController {

    private final DailyIncomeService dailyIncomeService;

    @GetMapping("daily-income/list")
    public ResponseEntity<List<DailyIncome>> getList(){
        return ResponseEntity.ok(dailyIncomeService.getDailyIncomeList());
    }

    @GetMapping("confirm/{id}")
    public ResponseEntity<?> confirm(@PathVariable long id){
        return ResponseEntity.ok(dailyIncomeService.confirm(id));
    }
}
