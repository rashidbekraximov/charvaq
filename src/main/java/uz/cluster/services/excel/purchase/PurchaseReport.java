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

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PurchaseReport {

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

    int counter = 0;

    private final PurchaseService purchaseService;

    private final PurchasedProductRepository purchasedProductRepository;

    public void purchaseReportSheet(XSSFWorkbook workbook, DocumentFilter documentFilter) {

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
            rowInTable.setHeight((short) 450);
            cell1 = rowInTable.createCell(CellType.STRING);
            cell1.setCellValue("Id");
            cell2 = rowInTable.createCell(CellType.STRING);
            cell2.setCellValue("Sana");
            cell3 = rowInTable.createCell(CellType.STRING);
            cell3.setCellValue("Mijoz");
            cell4 = rowInTable.createCell(CellType.STRING);
            cell4.setCellValue("Telefon raqami");
            cell5 = rowInTable.createCell(CellType.STRING);
            cell5.setCellValue("Mahsulot nomi");
            cell6 = rowInTable.createCell(CellType.STRING);
            cell6.setCellValue("Hajmi");
            cell7 = rowInTable.createCell(CellType.STRING);
            cell7.setCellValue("Narxi");
            cell8 = rowInTable.createCell(CellType.STRING);
            cell8.setCellValue("Qiymati");
            cell9 = rowInTable.createCell(CellType.STRING);
            cell9.setCellValue("Texnika");
            cell10 = rowInTable.createCell(CellType.STRING);
            cell10.setCellValue("Yo'l haqi");
            cell11 = rowInTable.createCell(CellType.STRING);
            cell11.setCellValue("Nasos");
            cell12 = rowInTable.createCell(CellType.STRING);
            cell12.setCellValue("To'lov turi");
            cell13 = rowInTable.createCell(CellType.STRING);
            cell13.setCellValue("Muddati");
            cell14 = rowInTable.createCell(CellType.STRING);
            cell14.setCellValue("Izoh");

            counter++;

            for (Purchase purchase : purchases) {
                List<PurchasedProduct> products = purchasedProductRepository.findAllByPurchaseIdOrderByProductType(purchase.getId());
                for (PurchasedProduct purchasedProduct : products){
                    rowInTable = new CustomXSSFRow(sheet.createRow(counter), cellStyleCenterOnlyFontWithBorderWithBold);
                    cell1 = rowInTable.createCell(CellType.STRING);
                    cell1.setCellValue(purchase.getId() + "");
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
                    if (purchase.getTechnician() != null){
                        cell9.setCellValue(purchase.getTechnician().getTechniqueType().getName().getActiveLanguage());
                    }else {
                        cell9.setCellValue("-");
                    }
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
                    counter++;
                }
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
