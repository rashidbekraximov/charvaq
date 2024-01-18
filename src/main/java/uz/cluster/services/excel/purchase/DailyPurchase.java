package uz.cluster.services.excel.purchase;

import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import uz.cluster.dao.purchase.DocumentFilter;
import uz.cluster.entity.purchase.Purchase;
import uz.cluster.entity.purchase.PurchasedProduct;
import uz.cluster.enums.purchase.PaymentEnum;
import uz.cluster.enums.purchase.PurchaseEnum;
import uz.cluster.repository.purchase.PurchasedProductRepository;
import uz.cluster.services.excel.style.CustomXSSFRow;
import uz.cluster.services.excel.style.ExcelStyle;
import uz.cluster.services.purchase.PurchaseService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DailyPurchase {

    CustomXSSFRow rowInTable;

    XSSFCellStyle cellStyleCenterBackgroundGreenWithBorder;
    XSSFCellStyle cellStyleCenterOnlyFontWithBorderWithBold;
    XSSFCellStyle cellStyleCenterBackgroundOrangeWithBorder;

    XSSFCell cell1;
    XSSFCell cell2;
    XSSFCell cell3;
    XSSFCell cell4;
    XSSFCell cell5;
    XSSFCell cell6;
    XSSFCell cell7;
    XSSFCell cell8;
    XSSFCell cell9;
    XSSFCell cell10;
    XSSFCell cell11;
    XSSFCell cell12;
    XSSFCell cell13;
    XSSFCell cell14;
    XSSFCell cell15;
    XSSFCell cell16;
    XSSFCell cell17;
    XSSFCell cell18;
    XSSFCell cell19;
    XSSFCell cell20;
    XSSFCell cell21;

    int counter = 0;

    private final PurchaseService purchaseService;

    private final PurchasedProductRepository purchasedProductRepository;

    public void purchaseDailyReportSheet(XSSFWorkbook workbook, DocumentFilter documentFilter) {

        counter = 0;
        try {
            XSSFSheet sheet = workbook.createSheet("Sotuvlar ro'yxati");

            cellStyleCenterBackgroundGreenWithBorder = ExcelStyle.cellStyleCenterBackgroundGreenWithBorder(workbook);
            cellStyleCenterOnlyFontWithBorderWithBold = ExcelStyle.cellStyleCenterOnlyFontWithBorderWithBold(workbook);
            cellStyleCenterBackgroundOrangeWithBorder = ExcelStyle.cellStyleCenterBackgroundOrangeWithBorder(workbook);

            int currColumnIndex = 1;
            List<Purchase> purchases = purchaseService.getPurchasesByPage(documentFilter);
            for (int i = 0; i < 22; i++) {
                if (i <= 14) {
                    sheet.setColumnWidth(currColumnIndex++, 256 * 10);
                } else if (i == 18) {
                    sheet.setColumnWidth(currColumnIndex++, 256 * 35);
                } else {
                    sheet.setColumnWidth(currColumnIndex++, 256 * 17);
                }
            }
            counter++;

            sheet.addMergedRegion(new CellRangeAddress(counter, counter, 1, 3));
            rowInTable = new CustomXSSFRow(sheet.createRow(counter), cellStyleCenterOnlyFontWithBorderWithBold);
            rowInTable.setHeight((short) 400);
            cell1 = rowInTable.createCell(CellType.STRING);
            cell2 = rowInTable.createCell(CellType.STRING);
            cell3 = rowInTable.createCell(CellType.STRING);
            cell3.setCellValue(LocalDate.now());
            counter++;

            for (int i = 2; i <= 14; i++) {
                if (i % 2 == 0) {
                    sheet.addMergedRegion(new CellRangeAddress(counter, counter, i, i + 1));
                }
            }
            rowInTable = new CustomXSSFRow(sheet.createRow(counter), cellStyleCenterBackgroundGreenWithBorder);
            rowInTable.setHeight((short) 480);
            sheet.addMergedRegion(new CellRangeAddress(counter, counter + 1, 1, 1));
            cell1 = rowInTable.createCell(CellType.STRING);
            cell1.setCellValue("№ Tall.");
            cell2 = rowInTable.createCell(CellType.STRING);
            cell2.setCellValue("KLINES м³");
            cell3 = rowInTable.createCell(CellType.STRING);
            cell4 = rowInTable.createCell(CellType.STRING);
            cell4.setCellValue("SHEBEN м³");
            cell5 = rowInTable.createCell(CellType.STRING);
            cell6 = rowInTable.createCell(CellType.STRING);
            cell6.setCellValue("CHINOZ м³");
            cell7 = rowInTable.createCell(CellType.STRING);
            cell8 = rowInTable.createCell(CellType.STRING);
            cell8.setCellValue("POYMA");
            cell9 = rowInTable.createCell(CellType.STRING);
            cell10 = rowInTable.createCell(CellType.STRING);
            cell10.setCellValue("SHAG'AL");
            cell11 = rowInTable.createCell(CellType.STRING);
            cell12 = rowInTable.createCell(CellType.STRING);
            cell12.setCellValue("KIRPICH");
            cell13 = rowInTable.createCell(CellType.STRING);
            cell14 = rowInTable.createCell(CellType.STRING);
            cell14.setCellValue("Sh.BLOK");
            cell15 = rowInTable.createCell(CellType.STRING);
            cell16 = rowInTable.createCell(CellType.STRING);
            cell16.setCellValue("Naqd");
            cell17 = rowInTable.createCell(CellType.STRING);
            cell17.setCellValue("Qarz");
            cell18 = rowInTable.createCell(CellType.STRING);
            cell18.setCellValue("Avto");
            cell19 = rowInTable.createCell(CellType.STRING);
            cell19.setCellValue("BUYURTMACHI/MIJOZ MANZILI");
            sheet.addMergedRegion(new CellRangeAddress(counter, counter, 20, 21));
            cell20 = rowInTable.createCell(CellType.STRING);
            cell20.setCellValue("YO'L HAQI");
            cell21 = rowInTable.createCell(CellType.STRING);
            counter++;

            rowInTable = new CustomXSSFRow(sheet.createRow(counter), cellStyleCenterBackgroundGreenWithBorder);
            cell1 = rowInTable.createCell(CellType.STRING);
            for (int i = 2; i <= 15; i++) {
                if (i % 2 == 0) {
                    cell2 = rowInTable.createCell(CellType.STRING);
                    cell2.setCellValue("N");
                } else {
                    cell3 = rowInTable.createCell(CellType.STRING);
                    cell3.setCellValue("Q");
                }
            }
            cell16 = rowInTable.createCell(CellType.STRING);
            cell17 = rowInTable.createCell(CellType.STRING);
            cell18 = rowInTable.createCell(CellType.STRING);
            cell19 = rowInTable.createCell(CellType.STRING);
            cell20 = rowInTable.createCell(CellType.STRING);
            cell20.setCellValue("N");
            cell21 = rowInTable.createCell(CellType.STRING);
            cell21.setCellValue("Q");
            counter++;

            for (Purchase purchase : purchases) {
                rowInTable = new CustomXSSFRow(sheet.createRow(counter), cellStyleCenterOnlyFontWithBorderWithBold);
                cell1 = rowInTable.createCell(CellType.STRING);
                cell1.setCellValue(purchase.getId() + "");
                for (int i = 1; i <= 7; i++) {
                    Optional<PurchasedProduct> product = purchasedProductRepository.findByProductType_IdAndPurchaseId(i, purchase.getId());
                    cell2 = rowInTable.createCell(CellType.STRING);
                    cell3 = rowInTable.createCell(CellType.STRING);
                    if (product.isPresent()) {
                        if (purchase.getPaidTotalValue() != 0) {
                            cell2.setCellValue(product.get().getWeight());
                        } else {
                            cell3.setCellValue(product.get().getWeight());
                        }
                    } else {
                        cell2.setCellValue("");
                        cell3.setCellValue("");
                    }
                }
                cell16 = rowInTable.createCell(CellType.NUMERIC);
                cell16.setCellValue(purchase.getPaidTotalValue() - purchase.getFare());
                cell17 = rowInTable.createCell(CellType.NUMERIC);
                if (purchase.getDebtTotalValue() == 0){
                    cell17.setCellValue(purchase.getDebtTotalValue());
                }else{
                    cell17.setCellValue(purchase.getDebtTotalValue() - purchase.getFare());
                }
                cell18 = rowInTable.createCell(CellType.STRING);
                if (purchase.getTechnician() == null){
                    cell18.setCellValue("");
                }else{
                    cell18.setCellValue(purchase.getTechnician().getTechniqueType().getName().getActiveLanguage());
                }
                cell19 = rowInTable.createCell(CellType.STRING);
                if (!purchase.getLocation().equals("")){
                    cell19.setCellValue(purchase.getClient() + "/" + purchase.getLocation());
                }else{
                    cell19.setCellValue(purchase.getClient());
                }
                cell20 = rowInTable.createCell(CellType.NUMERIC);
                cell21 = rowInTable.createCell(CellType.NUMERIC);
                if (purchase.getPaidTotalValue() != 0){
                    cell20.setCellValue(purchase.getFare());
                    cell21.setCellValue(0);
                }else{
                    cell21.setCellValue(purchase.getFare());
                    cell20.setCellValue(0);
                }
                counter++;
            }
//            sheet.addMergedRegion(new CellRangeAddress(counter, counter, 1, 14));
//            rowInTable = new CustomXSSFRow(sheet.createRow(counter), cellStyleCenterBackgroundGreenWithBorder);
//            rowInTable.setHeight((short) 450);
//            cell1 = rowInTable.createCell(CellType.STRING);
//            cell1.setCellValue("");
//            cell2 = rowInTable.createCell(CellType.STRING);
//            cell2.setCellValue("");
//            cell3 = rowInTable.createCell(CellType.STRING);
//            cell3.setCellValue("");
//            cell4 = rowInTable.createCell(CellType.STRING);
//            cell4.setCellValue("");
//            cell5 = rowInTable.createCell(CellType.STRING);
//            cell5.setCellValue("");
//            cell6 = rowInTable.createCell(CellType.STRING);
//            cell6.setCellValue("");
//            cell7 = rowInTable.createCell(CellType.STRING);
//            cell7.setCellValue("");
//            cell8 = rowInTable.createCell(CellType.STRING);
//            cell8.setCellValue("");
//            cell9 = rowInTable.createCell(CellType.STRING);
//            cell9.setCellValue("");
//            cell10 = rowInTable.createCell(CellType.STRING);
//            cell10.setCellValue("");
//            cell11 = rowInTable.createCell(CellType.STRING);
//            cell11.setCellValue("");
//            cell12 = rowInTable.createCell(CellType.STRING);
//            cell12.setCellValue("");
//            cell13 = rowInTable.createCell(CellType.STRING);
//            cell13.setCellValue("");
//            cell14 = rowInTable.createCell(CellType.STRING);
//            cell14.setCellValue("");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
