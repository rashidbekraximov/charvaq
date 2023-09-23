package uz.cluster.controllers.purchase;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.cluster.dao.purchase.AllDebtDao;
import uz.cluster.dao.purchase.DocumentFilter;
import uz.cluster.dao.purchase.Notification;
import uz.cluster.dao.purchase.PurchaseDao;
import uz.cluster.payload.response.ApiResponse;
import uz.cluster.services.purchase.EstinguishService;
import uz.cluster.services.purchase.PurchaseService;
import uz.cluster.util.GlobalParams;

import java.util.List;


@RestController
@RequestMapping("api/")
@Tag(name = "References Sotish", description = "Sotish")
@RequiredArgsConstructor
public class PurchaseController {

    private final PurchaseService purchaseService;

    private final EstinguishService estinguishService;

    @GetMapping("purchases")
    public ResponseEntity<List<PurchaseDao>> getList(){
        return ResponseEntity.ok(purchaseService.getList());
    }

    @GetMapping("purchase/debts")
    public ResponseEntity<List<AllDebtDao>> getDebtList(){
        return ResponseEntity.ok(estinguishService.getDebtList());
    }

    @GetMapping("purchase/debts/searched")
    public ResponseEntity<List<AllDebtDao>> getSearchList(@RequestParam String client){
        return ResponseEntity.ok(estinguishService.getSearchList(client));
    }
//
//    @GetMapping("purchase/debt")
//    public ResponseEntity<List<Notification>> getNotificationList(){
//        return ResponseEntity.ok(purchaseService.getNotifications());
//    }

    @PostMapping(value = "purchase/filter")
    public ResponseEntity<?> getListDocuments(@RequestBody DocumentFilter documentFilter){
        return ResponseEntity.ok(purchaseService.getPurchasesByPage(documentFilter));
    }

    @GetMapping("purchase/{id}")
    public ResponseEntity<?> getById(@PathVariable int id){
        PurchaseDao purchaseDao = purchaseService.getById(id);
        if (purchaseDao == null){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(purchaseDao);
    }

    @PostMapping("purchase/add")
    public ResponseEntity<?> save(@RequestBody PurchaseDao purchaseDao){
        ApiResponse apiResponse = purchaseService.add(purchaseDao);
        if (!apiResponse.isSuccess())
            return ResponseEntity.status(HttpStatus.CONFLICT).body(apiResponse);
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @GetMapping("purchase/delete/{id}")
    public ResponseEntity<?> save(@PathVariable int id){
        ApiResponse apiResponse = purchaseService.delete(id);
        if (!apiResponse.isSuccess())
            return ResponseEntity.status(HttpStatus.CONFLICT).body(apiResponse);
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @GetMapping("purchase/delete-debt/{id}")
    public ResponseEntity<?> deleteDebt(@PathVariable int id){
        ApiResponse apiResponse = purchaseService.deleteDebt(id);
        if (!apiResponse.isSuccess())
            return ResponseEntity.status(HttpStatus.CONFLICT).body(apiResponse);
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
}
