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
import uz.cluster.repository.purchase.PurchasedProductRepository;
import uz.cluster.services.excel.style.CustomXSSFRow;
import uz.cluster.services.excel.style.ExcelStyle;
import uz.cluster.services.purchase.PurchaseService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DailyPurchase {

    CustomXSSFRow rowInTable;

    XSSFCellStyle cellStyleCenterBackgroundGreenWithBorder;
    XSSFCellStyle cellStyleCenterOnlyFontWithBorderWithBold;
    XSSFCellStyle cellStyleLeftOnlyFontBoldWithBorder;
    XSSFCellStyle cellStyleCenterOnlyFontWithBorderWithoutBold;
    XSSFCellStyle cellStyleLeftOnlyFontWithoutBoldWithBorder;
    XSSFCellStyle cellStyleCenterBackgroundOrangeWithBorder;
    XSSFCellStyle cellStyleCenterBackgroundGreenWithoutBorder;
    XSSFCellStyle cellStyleCenterBackgroundPurpleWithBorder;
    XSSFCellStyle cellStyleLeftBackgroundGreenWithBorder;
    XSSFCellStyle cellStyleCenterDarkBackgroundGreenWithBorder;
    XSSFCellStyle cellStyleCenterBackgroundYellowWithBorder;
    XSSFCellStyle cellStyleBackgroundRedWithoutBorder;
    XSSFCellStyle cellStyleCenterBackgroundSeaGreenWithBorder;

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
            for (int i = 0; i < 15; i++) {
                if (i == 3){
                    sheet.setColumnWidth(currColumnIndex++, 256 * 22);
                }else if (i == 1){
                    sheet.setColumnWidth(currColumnIndex++, 256 * 15);
                }else if (i == 14){
                    sheet.setColumnWidth(currColumnIndex++, 256 * 27);
                }else {
                    sheet.setColumnWidth(currColumnIndex++, 256 * 19);
                }
            }
            counter++;

            sheet.addMergedRegion(new CellRangeAddress(counter, counter, 1, 14));
            rowInTable = new CustomXSSFRow(sheet.createRow(counter), cellStyleCenterOnlyFontWithBorderWithBold);
            rowInTable.setHeight((short) 400);
            for (int i = 1; i < 15; i++) {
                cell1 = rowInTable.createCell(CellType.STRING);
            }
            counter++;

            rowInTable = new CustomXSSFRow(sheet.createRow(counter), cellStyleCenterBackgroundGreenWithBorder);
            rowInTable.setHeight((short) 650);
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
            cell20 = rowInTable.createCell(CellType.STRING);
            cell20.setCellValue("YO'L HAQI");
            cell21 = rowInTable.createCell(CellType.STRING);
            counter++;

            rowInTable = new CustomXSSFRow(sheet.createRow(counter), cellStyleCenterBackgroundGreenWithBorder);
            rowInTable.setHeight((short) 450);
            cell1 = rowInTable.createCell(CellType.STRING);
            for (int i = 2; i < 12; i++) {
                if (i % 2 == 0){
                    cell2 = rowInTable.createCell(CellType.STRING);
                    cell2.setCellValue("N");
                }else{
                    cell3 = rowInTable.createCell(CellType.STRING);
                    cell2.setCellValue("Q");
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
                List<PurchasedProduct> products = purchasedProductRepository.findAllByPurchaseIdOrderByProductType(purchase.getId());
                for (PurchasedProduct purchasedProduct : products){
                    cell2 = rowInTable.createCell(CellType.STRING);
                    cell2.setCellValue(purchase.getDate().toString());
                    cell3 = rowInTable.createCell(CellType.STRING);
                    cell3.setCellValue(purchase.getClient());
                    cell4 = rowInTable.createCell(CellType.STRING);
                    cell4.setCellValue(purchase.getPhoneNumber());
                    cell5 = rowInTable.createCell(CellType.STRING);
                    cell5.setCellValue(purchasedProduct.getProductType().getName().getActiveLanguage());
                    cell6 = rowInTable.createCell(CellType.NUMERIC);
                    cell6.setCellValue(purchasedProduct.getWeight());
                    cell7 = rowInTable.createCell(CellType.NUMERIC);
                    cell7.setCellValue(purchasedProduct.getPrice());
                    cell8 = rowInTable.createCell(CellType.NUMERIC);
                    cell8.setCellValue(purchasedProduct.getValue());
                    cell9 = rowInTable.createCell(CellType.STRING);
                    cell9.setCellValue(purchase.getTechnician().getTechniqueType().getName().getActiveLanguage());
                    cell10 = rowInTable.createCell(CellType.NUMERIC);
                    cell10.setCellValue(purchase.getFare() / products.size());
                    cell11 = rowInTable.createCell(CellType.NUMERIC);
                    cell11.setCellValue(purchase.getNasos());
                    cell12 = rowInTable.createCell(CellType.STRING);
                    cell12.setCellValue(purchase.getPaymentType().getName().getActiveLanguage());
                    cell13 = rowInTable.createCell(CellType.STRING);
                    cell13.setCellValue(purchase.getExpiryDate().toString());
                    cell14 = rowInTable.createCell(CellType.STRING);
                    cell14.setCellValue(purchase.getDescription());
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
