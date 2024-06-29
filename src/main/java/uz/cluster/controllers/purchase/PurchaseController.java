package uz.cluster.controllers.purchase;

import com.lowagie.text.DocumentException;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.cluster.dao.purchase.*;
import uz.cluster.entity.purchase.Purchase;
import uz.cluster.payload.response.ApiResponse;
import uz.cluster.repository.purchase.PurchaseRepository;
import uz.cluster.repository.purchase.PurchasedProductRepository;
import uz.cluster.services.pdf.PdfGenerator;
import uz.cluster.services.purchase.EstinguishService;
import uz.cluster.services.purchase.PurchaseService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("api/")
@Tag(name = "References Sotish", description = "Sotish")
@RequiredArgsConstructor
public class PurchaseController {

    private final PurchaseService purchaseService;

    private final PurchaseRepository purchaseRepository;

    private final PurchasedProductRepository purchasedProductRepository;

    private final EstinguishService estinguishService;

    @GetMapping("purchases")
    public ResponseEntity<List<PurchaseDao>> getList() {
        return ResponseEntity.ok(purchaseService.getList());
    }

    @GetMapping("check-tallon")
    public ResponseEntity<List<PurchaseDao>> checkForTallonNumber() {
        return ResponseEntity.ok(purchaseService.getLastARow());
    }

    @GetMapping("purchase/debts")
    public ResponseEntity<List<AllDebtDao>> getDebtList() {
        return ResponseEntity.ok(estinguishService.getDebtList());
    }

    @GetMapping("purchase/line")
    public ResponseEntity<List<LineChartDao>> getLineList() {
        return ResponseEntity.ok(purchaseService.getLineList());
    }

    @GetMapping("purchased/last-line")
    public ResponseEntity<List<PurchaseDao>> getLastLineList() {
        return ResponseEntity.ok(purchaseService.getLastRows());
    }

    @PostMapping("/purchase/nakladnoy")
    public void generatePdfFile(@RequestBody Integer id, HttpServletResponse response) throws DocumentException, IOException {
        response.setContentType("application/pdf");
        DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-DD:HH:MM:SS");
        String currentDateTime = dateFormat.format(new Date());
        String headerkey = "Content-Disposition";
        String headervalue = "attachment; filename=student" + currentDateTime + ".pdf";
        response.setHeader(headerkey, headervalue);
        PdfGenerator generator = new PdfGenerator();
        PurchaseDao optionalPurchase = purchaseService.getById((int) id);
        generator.generate(optionalPurchase, response);
    }

    @GetMapping("daily-confirm/{date}")
    public ResponseEntity<List<PurchaseDao>> getByDateForConfirm(@PathVariable String date) {
        return ResponseEntity.ok(purchaseService.getByDateForConfirm(java.sql.Date.valueOf(date)));
    }

    @GetMapping("purchased/bar")
    public ResponseEntity<List<Long>> getForBarChart() {
        return ResponseEntity.ok(purchaseService.getBarList());
    }

    @GetMapping("purchased/bar-last/{beginDate}/{endDate}")
    public ResponseEntity<List<PurchaseData>> getForBarLast(@PathVariable String beginDate, @PathVariable String endDate) {
        return ResponseEntity.ok(purchaseService.getBarLastMonthly(beginDate,endDate));
    }

    @GetMapping("purchase/debts/searched")
    public ResponseEntity<List<AllDebtDao>> getSearchList(@RequestParam String client) {
        return ResponseEntity.ok(estinguishService.getSearchList(client));
    }

    @GetMapping("purchase/debt/{rows}")
    public ResponseEntity<List<Notification>> getNotificationList(@PathVariable int rows) {
        return ResponseEntity.ok(purchaseService.getNotifications(rows));
    }

    @PostMapping(value = "purchase/filter")
    public ResponseEntity<?> getListDocuments(@RequestBody DocumentFilter documentFilter) {
        return ResponseEntity.ok(purchaseService.getPurchasesByPage(documentFilter));
    }

    @GetMapping("purchase/{id}")
    public ResponseEntity<?> getById(@PathVariable int id) {
        PurchaseDao purchaseDao = purchaseService.getById(id);
        if (purchaseDao == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(purchaseDao);
    }

    @PostMapping("purchase/add")
    public ResponseEntity<?> save(@RequestBody PurchaseDao purchaseDao) {
        ApiResponse apiResponse = purchaseService.add(purchaseDao);
        if (!apiResponse.isSuccess())
            return ResponseEntity.status(HttpStatus.CONFLICT).body(apiResponse);
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @GetMapping("purchase/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable int id) {
        ApiResponse apiResponse = purchaseService.delete(id);
        if (!apiResponse.isSuccess())
            return ResponseEntity.status(HttpStatus.CONFLICT).body(apiResponse);
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @GetMapping("purchase/delete-debt/{id}")
    public ResponseEntity<?> deleteDebt(@PathVariable int id) {
        ApiResponse apiResponse = purchaseService.deleteDebt(id);
        if (!apiResponse.isSuccess())
            return ResponseEntity.status(HttpStatus.CONFLICT).body(apiResponse);
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
}
