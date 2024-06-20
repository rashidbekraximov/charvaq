package uz.cluster.services.excel.cost;


import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import uz.cluster.entity.lb.LBPurchase;
import uz.cluster.entity.purchase.CheckoutCost;
import uz.cluster.services.excel.style.CustomXSSFRow;
import uz.cluster.services.excel.style.ExcelStyle;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CostReport {

    CustomXSSFRow rowInTable;

    XSSFCellStyle cellStyleCenterBackgroundGreenWithBorder;
    XSSFCellStyle cellStyleCenterOnlyFontWithBorderWithBold;
    XSSFCellStyle cellStyleCenterBackgroundOrangeWithBorder;

    XSSFCell cell1;
    XSSFCell cell2;
    XSSFCell cell3;
    XSSFCell cell4;

    int counter = 0;

    public void costReportSheet(XSSFWorkbook workbook, List<CheckoutCost> checkoutCosts) {

        counter = 0;
        try {

            XSSFSheet sheet = workbook.createSheet("Xarajat");

            cellStyleCenterBackgroundGreenWithBorder = ExcelStyle.cellStyleCenterBackgroundGreenWithBorder(workbook);
            cellStyleCenterOnlyFontWithBorderWithBold = ExcelStyle.cellStyleCenterOnlyFontWithBorderWithBold(workbook);
            cellStyleCenterBackgroundOrangeWithBorder = ExcelStyle.cellStyleCenterBackgroundOrangeWithBorder(workbook);

            int currColumnIndex = 1;
            for (int i = 0; i < 10; i++) {
                    sheet.setColumnWidth(currColumnIndex++, 256 * 25);
            }
            counter++;

            rowInTable = new CustomXSSFRow(sheet.createRow(counter), cellStyleCenterBackgroundGreenWithBorder);
            rowInTable.setHeight((short) 450);
            cell1 = rowInTable.createCell(CellType.STRING);
            cell1.setCellValue("№");
            cell2 = rowInTable.createCell(CellType.STRING);
            cell2.setCellValue("Sana");
            cell3 = rowInTable.createCell(CellType.STRING);
            cell3.setCellValue("Xarajat turi");
            cell4 = rowInTable.createCell(CellType.STRING);
            cell4.setCellValue("Summa");
            counter++;

            int count = 0;
            for (CheckoutCost checkoutCost : checkoutCosts) {
                count++;
                rowInTable = new CustomXSSFRow(sheet.createRow(counter), cellStyleCenterOnlyFontWithBorderWithBold);
                cell1 = rowInTable.createCell(CellType.STRING);
                cell1.setCellValue(count + "");
                cell2 = rowInTable.createCell(CellType.STRING);
                cell2.setCellValue(checkoutCost.getDate().toString());
                cell3 = rowInTable.createCell(CellType.STRING);
                cell3.setCellValue(checkoutCost.getCostType().getName().getActiveLanguage());
                cell4 = rowInTable.createCell(CellType.NUMERIC);
                cell4.setCellValue(checkoutCost.getAmount());
                counter++;
            }

            sheet.addMergedRegion(new CellRangeAddress(counter, counter, 1, 3));
            rowInTable = new CustomXSSFRow(sheet.createRow(counter), cellStyleCenterBackgroundGreenWithBorder);
            rowInTable.setHeight((short) 450);
            cell1 = rowInTable.createCell(CellType.STRING);
            cell1.setCellValue("Jami");
            cell2 = rowInTable.createCell(CellType.STRING);
            cell3 = rowInTable.createCell(CellType.STRING);
            cell4 = rowInTable.createCell(CellType.FORMULA);
            cell4.setCellFormula("SUM(E3:E" + (checkoutCosts.size() + 2) + ")");
            counter++;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
