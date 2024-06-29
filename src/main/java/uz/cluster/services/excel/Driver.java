package uz.cluster.services.excel;

import lombok.RequiredArgsConstructor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import uz.cluster.dao.general.SpendingSparePartDao;
import uz.cluster.dao.purchase.DocumentFilter;
import uz.cluster.entity.purchase.CheckoutCost;
import uz.cluster.services.excel.cost.CostReport;
import uz.cluster.services.excel.lb.LBProduceReportSheet;
import uz.cluster.services.excel.lb.LBPurchaseReportSheet;
import uz.cluster.services.excel.purchase.DailyPurchase;
import uz.cluster.services.excel.purchase.PurchaseReport;
import uz.cluster.services.excel.tabel.TabelReport;
import uz.cluster.services.excel.warehouse.SpendingFuelAndSparePartReport;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class Driver {

    private final TabelReport tabelReport;

    private final PurchaseReport purchaseReport;

    private final DailyPurchase dailyPurchase;

    private final LBPurchaseReportSheet lbPurchaseReportSheet;

    private final LBProduceReportSheet lbProduceReportSheet;

    private final CostReport costReport;

    private final SpendingFuelAndSparePartReport spendingFuelAndSparePartReport;

    public void excelTabelSheet(HttpServletResponse response, Date beginDate, Date endDate,int direction) throws IOException {

        String headerKey = "Content-Disposition";

        String fileName = "Tabel.xlsx";

        response.setContentType("application/octet-stream");
        response.setHeader(headerKey, "attachment; filename=" + java.net.URLEncoder.encode(fileName.replaceAll(" ", "_").toLowerCase(), StandardCharsets.UTF_8));

        XSSFWorkbook workbook = new XSSFWorkbook();

        System.out.println("Tabel starting");
        tabelReport.tabelReportSheet(workbook,beginDate,endDate,direction);

        System.out.println("After excel writing: " + LocalTime.now());
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        System.out.println("Workbook write time: " + LocalTime.now());
        workbook.close();
        System.out.println("Workbook close:      " + LocalTime.now());
        response.getOutputStream().flush();
        response.getOutputStream().close();
        outputStream.flush();
        outputStream.close();
        System.out.println("Output close:        " + LocalTime.now());
    }

    public void excelCostSheet(HttpServletResponse response, List<CheckoutCost> checkoutCosts) throws IOException {

        String headerKey = "Content-Disposition";

        String fileName = "Tabel.xlsx";

        response.setContentType("application/octet-stream");
        response.setHeader(headerKey, "attachment; filename=" + java.net.URLEncoder.encode(fileName.replaceAll(" ", "_").toLowerCase(), StandardCharsets.UTF_8));

        XSSFWorkbook workbook = new XSSFWorkbook();

        System.out.println("Tabel starting");
        costReport.costReportSheet(workbook,checkoutCosts);

        System.out.println("After excel writing: " + LocalTime.now());
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        System.out.println("Workbook write time: " + LocalTime.now());
        workbook.close();
        System.out.println("Workbook close:      " + LocalTime.now());
        response.getOutputStream().flush();
        response.getOutputStream().close();
        outputStream.flush();
        outputStream.close();
        System.out.println("Output close:        " + LocalTime.now());
    }

    public void excelSpendFuelAndSparePartSheet(HttpServletResponse response, List<SpendingSparePartDao> checkoutCosts) throws IOException {

        String headerKey = "Content-Disposition";

        String fileName = "Tabel.xlsx";

        response.setContentType("application/octet-stream");
        response.setHeader(headerKey, "attachment; filename=" + java.net.URLEncoder.encode(fileName.replaceAll(" ", "_").toLowerCase(), StandardCharsets.UTF_8));

        XSSFWorkbook workbook = new XSSFWorkbook();

        System.out.println("Tabel starting");
        spendingFuelAndSparePartReport.costReportSheet(workbook,checkoutCosts);

        System.out.println("After excel writing: " + LocalTime.now());
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        System.out.println("Workbook write time: " + LocalTime.now());
        workbook.close();
        System.out.println("Workbook close:      " + LocalTime.now());
        response.getOutputStream().flush();
        response.getOutputStream().close();
        outputStream.flush();
        outputStream.close();
        System.out.println("Output close:        " + LocalTime.now());
    }

    public void excelPurchaseSheet(HttpServletResponse response, DocumentFilter documentFilter) throws IOException {

        String headerKey = "Content-Disposition";

        String fileName = "Sotuvlar ro'yxati.xlsx";

        response.setContentType("application/octet-stream");
        response.setHeader(headerKey, "attachment; filename=" + java.net.URLEncoder.encode(fileName.replaceAll(" ", "_").toLowerCase(), StandardCharsets.UTF_8));

        XSSFWorkbook workbook = new XSSFWorkbook();

        System.out.println("Purchases");
        purchaseReport.purchaseReportSheet(workbook,documentFilter);

        System.out.println("After excel writing: " + LocalTime.now());
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        System.out.println("Workbook write time: " + LocalTime.now());
        workbook.close();
        System.out.println("Workbook close:      " + LocalTime.now());
        response.getOutputStream().flush();
        response.getOutputStream().close();
        outputStream.flush();
        outputStream.close();
        System.out.println("Output close:        " + LocalTime.now());
    }
    public void excelLBPurchaseSheet(HttpServletResponse response, DocumentFilter documentFilter) throws IOException {

        String headerKey = "Content-Disposition";

        String fileName = "Sotuv LB.xlsx";

        response.setContentType("application/octet-stream");
        response.setHeader(headerKey, "attachment; filename=" + java.net.URLEncoder.encode(fileName.replaceAll(" ", "_").toLowerCase(), StandardCharsets.UTF_8));

        XSSFWorkbook workbook = new XSSFWorkbook();

        System.out.println("Purchases");
        lbPurchaseReportSheet.purchaseReportSheet(workbook,documentFilter);

        System.out.println("After excel writing: " + LocalTime.now());
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        System.out.println("Workbook write time: " + LocalTime.now());
        workbook.close();
        System.out.println("Workbook close:      " + LocalTime.now());
        response.getOutputStream().flush();
        response.getOutputStream().close();
        outputStream.flush();
        outputStream.close();
        System.out.println("Output close:        " + LocalTime.now());
    }

    public void excelLBProduceSheet(HttpServletResponse response, DocumentFilter documentFilter) throws IOException {

        String headerKey = "Content-Disposition";

        String fileName = "Ishlab chiqarish LB.xlsx";

        response.setContentType("application/octet-stream");
        response.setHeader(headerKey, "attachment; filename=" + java.net.URLEncoder.encode(fileName.replaceAll(" ", "_").toLowerCase(), StandardCharsets.UTF_8));

        XSSFWorkbook workbook = new XSSFWorkbook();

        System.out.println("Produce");
        lbProduceReportSheet.purchaseReportSheet(workbook,documentFilter);

        System.out.println("After excel writing: " + LocalTime.now());
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        System.out.println("Workbook write time: " + LocalTime.now());
        workbook.close();
        System.out.println("Workbook close:      " + LocalTime.now());
        response.getOutputStream().flush();
        response.getOutputStream().close();
        outputStream.flush();
        outputStream.close();
        System.out.println("Output close:        " + LocalTime.now());
    }

    public void excelDailyPurchaseSheet(HttpServletResponse response, DocumentFilter documentFilter) throws IOException {

        String headerKey = "Content-Disposition";

        String fileName = "Tabel.xlsx";

        response.setContentType("application/octet-stream");
        response.setHeader(headerKey, "attachment; filename=" + java.net.URLEncoder.encode(fileName.replaceAll(" ", "_").toLowerCase(), StandardCharsets.UTF_8));

        XSSFWorkbook workbook = new XSSFWorkbook();

        System.out.println("Daily Purchases");
        dailyPurchase.purchaseDailyReportSheet(workbook,documentFilter);

        System.out.println("After excel writing: " + LocalTime.now());
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        System.out.println("Workbook write time: " + LocalTime.now());
        workbook.close();
        System.out.println("Workbook close:      " + LocalTime.now());
        response.getOutputStream().flush();
        response.getOutputStream().close();
        outputStream.flush();
        outputStream.close();
        System.out.println("Output close:        " + LocalTime.now());
    }


}
