package uz.cluster.controllers;


import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import uz.cluster.dao.purchase.DocumentFilter;
import uz.cluster.services.excel.Driver;

import javax.servlet.http.HttpServletResponse;
import java.sql.Date;

@RestController
@RequestMapping("api/report")
@Tag(name = "Tabel", description = "Tabel Xisobot")
@RequiredArgsConstructor
public class ReportController {

    private final Driver driver;

    @GetMapping("/tabel/{beginDate}/{endDate}/{id}")
    public void downloadTabelTotal(HttpServletResponse response, @PathVariable String beginDate, @PathVariable String endDate,@PathVariable int id) throws Exception {
        driver.excelTabelSheet(response, Date.valueOf(beginDate),Date.valueOf(endDate),id);
    }

    @PostMapping("/purchase")
    public void downloadPurchaseTotal(HttpServletResponse response, @RequestBody DocumentFilter documentFilter) throws Exception {
        driver.excelPurchaseSheet(response, documentFilter);
    }

    @PostMapping("/purchase/daily")
    public void downloadPurchaseDailyTotal(HttpServletResponse response, @RequestBody DocumentFilter documentFilter) throws Exception {
        driver.excelDailyPurchaseSheet(response, documentFilter);
    }
}
