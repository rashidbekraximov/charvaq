package uz.cluster.controllers.lb;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.cluster.dao.lb.BarDao;
import uz.cluster.dao.lb.LBPurchaseDao;
import uz.cluster.dao.purchase.DocumentFilter;
import uz.cluster.dao.purchase.PurchaseDao;
import uz.cluster.payload.response.ApiResponse;
import uz.cluster.services.lb.LBPurchaseService;

import java.util.List;

@RestController
@RequestMapping("api/")
@Tag(name = "LB Purchase", description = "Sotuv")
@RequiredArgsConstructor
public class LBPurchaseController {

    private final LBPurchaseService lbPurchaseService;

    @GetMapping("lb-purchases")
    public ResponseEntity<List<LBPurchaseDao>> getList() {
        return ResponseEntity.ok(lbPurchaseService.getList());
    }

    @PostMapping(value = "lb-purchase/filter")
    public ResponseEntity<?> getListDocuments(@RequestBody DocumentFilter documentFilter){
        return ResponseEntity.ok(lbPurchaseService.getPurchasesByPage(documentFilter));
    }

    @GetMapping("lb-purchased/last-line")
    public ResponseEntity<List<LBPurchaseDao>> getLastLineList(){
        return ResponseEntity.ok(lbPurchaseService.getLastRows());
    }

    @GetMapping("lb-purchased/bar")
    public ResponseEntity<List<Long>> getForBarChart(){
        return ResponseEntity.ok(lbPurchaseService.getBarList());
    }

    @GetMapping("lb-nasos/bar")
    public ResponseEntity<List<Long>> getForNasos(){
        return ResponseEntity.ok(lbPurchaseService.getBarListForNasos());
    }

    @GetMapping("lb-nasos/bar-last/{time}")
    public ResponseEntity<List<Double>> getForNasosBarLast(@PathVariable String time){
        if(time.equals("DAILY")){
            return ResponseEntity.ok(lbPurchaseService.getBarListDaily());
        }else{
            return ResponseEntity.ok(lbPurchaseService.getBarListMonthly());
        }
    }

    @GetMapping("lb-purchased/bar-last/{time}")
    public ResponseEntity<List<BarDao>> getForBarLast(@PathVariable String time){
        return ResponseEntity.ok(lbPurchaseService.getBarLast(time));
    }

    @GetMapping("lb-purchase/test/{mark}/{amount}")
    public ResponseEntity<?> getListForSelect(@PathVariable int mark,@PathVariable double amount) {
        return ResponseEntity.ok(lbPurchaseService.getByAmountAndMark(mark, amount));
    }

    @GetMapping("lb-purchase/{id}")
    public ResponseEntity<?> getById(@PathVariable int id) {
        LBPurchaseDao dao = lbPurchaseService.getById(id);
        if (dao == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(dao);
    }

    @PostMapping("lb-purchase/save")
    public ResponseEntity<?> save(@RequestBody LBPurchaseDao dao) {
        ApiResponse apiResponse = lbPurchaseService.add(dao);
        if (!apiResponse.isSuccess())
            return ResponseEntity.status(HttpStatus.CONFLICT).body(apiResponse);
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @GetMapping("lb-purchase/delete/{id}")
    public ResponseEntity<?> save(@PathVariable int id) {
        ApiResponse apiResponse = lbPurchaseService.delete(id);
        if (!apiResponse.isSuccess())
            return ResponseEntity.status(HttpStatus.CONFLICT).body(apiResponse);
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
}
