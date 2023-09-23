package uz.cluster.services.excel.lb;

import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import uz.cluster.dao.purchase.DocumentFilter;
import uz.cluster.entity.lb.LBPurchase;
import uz.cluster.repository.purchase.PurchasedProductRepository;
import uz.cluster.services.excel.style.CustomXSSFRow;
import uz.cluster.services.excel.style.ExcelStyle;
import uz.cluster.services.lb.LBPurchaseService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LBPurchaseReportSheet {

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

    int counter = 0;

    private final LBPurchaseService purchaseService;

    private final PurchasedProductRepository purchasedProductRepository;

    public void purchaseReportSheet(XSSFWorkbook workbook, DocumentFilter documentFilter) {

        counter = 0;
        try {

            XSSFSheet sheet = workbook.createSheet("Sotuvlar ro'yxati");

            cellStyleCenterBackgroundGreenWithBorder = ExcelStyle.cellStyleCenterBackgroundGreenWithBorder(workbook);
            cellStyleCenterOnlyFontWithBorderWithBold = ExcelStyle.cellStyleCenterOnlyFontWithBorderWithBold(workbook);
            cellStyleCenterBackgroundOrangeWithBorder = ExcelStyle.cellStyleCenterBackgroundOrangeWithBorder(workbook);

            int currColumnIndex = 1;
            List<LBPurchase> purchases = purchaseService.getPurchasesByPage(documentFilter);
            for (int i = 0; i < 10; i++) {
                if (i == 0) {
                    sheet.setColumnWidth(currColumnIndex++, 256 * 12);
                } else if (i == 8 || i == 1) {
                    sheet.setColumnWidth(currColumnIndex++, 256 * 27);
                } else if (i == 3 || i == 4) {
                    sheet.setColumnWidth(currColumnIndex++, 256 * 15);
                } else {
                    sheet.setColumnWidth(currColumnIndex++, 256 * 20);
                }
            }
            counter++;

            sheet.addMergedRegion(new CellRangeAddress(counter, counter, 1, 9));
            rowInTable = new CustomXSSFRow(sheet.createRow(counter), cellStyleCenterOnlyFontWithBorderWithBold);
            rowInTable.setHeight((short) 400);
            for (int i = 1; i < 10; i++) {
                cell1 = rowInTable.createCell(CellType.STRING);
            }
            counter++;

            rowInTable = new CustomXSSFRow(sheet.createRow(counter), cellStyleCenterBackgroundGreenWithBorder);
            rowInTable.setHeight((short) 450);
            cell1 = rowInTable.createCell(CellType.STRING);
            cell1.setCellValue("№");
            cell2 = rowInTable.createCell(CellType.STRING);
            cell2.setCellValue("Buyurtmachi");
            cell3 = rowInTable.createCell(CellType.STRING);
            cell3.setCellValue("Hudud");
            cell4 = rowInTable.createCell(CellType.STRING);
            cell4.setCellValue("Marka");
            cell5 = rowInTable.createCell(CellType.STRING);
            cell5.setCellValue("Miqdor");
            cell6 = rowInTable.createCell(CellType.STRING);
            cell6.setCellValue("Auto");
            cell7 = rowInTable.createCell(CellType.STRING);
            cell7.setCellValue("Qiymati");
            cell8 = rowInTable.createCell(CellType.STRING);
            cell8.setCellValue("Xodka");
            cell9 = rowInTable.createCell(CellType.STRING);
            cell9.setCellValue("Izoh");

            counter++;

            for (LBPurchase purchase : purchases) {
                rowInTable = new CustomXSSFRow(sheet.createRow(counter), cellStyleCenterOnlyFontWithBorderWithBold);
                cell1 = rowInTable.createCell(CellType.STRING);
                cell1.setCellValue(purchase.getId() + "");
                cell2 = rowInTable.createCell(CellType.STRING);
                cell2.setCellValue(purchase.getCustomer());
                cell3 = rowInTable.createCell(CellType.STRING);
                cell3.setCellValue(purchase.getLocation());
                cell4 = rowInTable.createCell(CellType.NUMERIC);
                cell4.setCellValue(purchase.getMark());
                cell5 = rowInTable.createCell(CellType.NUMERIC);
                cell5.setCellValue(purchase.getAmount());
                cell6 = rowInTable.createCell(CellType.STRING);
                cell6.setCellValue(purchase.getMixer().getAuto());
                cell7 = rowInTable.createCell(CellType.NUMERIC);
                cell7.setCellValue(purchase.getValue());
                cell8 = rowInTable.createCell(CellType.NUMERIC);
                cell8.setCellValue(purchase.getXodka());
                cell9 = rowInTable.createCell(CellType.STRING);
                cell9.setCellValue(purchase.getDescription());
                counter++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
