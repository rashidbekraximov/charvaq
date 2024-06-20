package uz.cluster.services.excel.warehouse;

import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import uz.cluster.dao.general.SparePartDao;
import uz.cluster.dao.general.SpendingSparePartDao;
import uz.cluster.entity.purchase.CheckoutCost;
import uz.cluster.enums.ItemEnum;
import uz.cluster.services.excel.style.CustomXSSFRow;
import uz.cluster.services.excel.style.ExcelStyle;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SpendingFuelAndSparePartReport {

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

    int counter = 0;

    public void costReportSheet(XSSFWorkbook workbook, List<SpendingSparePartDao> checkoutCosts) {

        counter = 0;
        try {

            XSSFSheet sheet = workbook.createSheet("Sarf Xarajatlar");

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
            cell3.setCellValue("Texnika");
            cell4 = rowInTable.createCell(CellType.STRING);
            cell4.setCellValue("Auto raqami");
            cell5 = rowInTable.createCell(CellType.STRING);
            cell5.setCellValue("Mahsulot");
            cell6 = rowInTable.createCell(CellType.STRING);
            cell6.setCellValue("Narxi");
            cell7 = rowInTable.createCell(CellType.STRING);
            cell7.setCellValue("Miqdori");
            cell8 = rowInTable.createCell(CellType.STRING);
            cell8.setCellValue("Summa");
            counter++;

            int count = 0;
            for (SpendingSparePartDao sparePartDao : checkoutCosts) {
                count++;
                rowInTable = new CustomXSSFRow(sheet.createRow(counter), cellStyleCenterOnlyFontWithBorderWithBold);
                cell1 = rowInTable.createCell(CellType.STRING);
                cell1.setCellValue(count + "");
                cell2 = rowInTable.createCell(CellType.STRING);
                cell2.setCellValue(sparePartDao.getDate().toString());
                cell3 = rowInTable.createCell(CellType.STRING);
                cell3.setCellValue(sparePartDao.getTechnician().getTechniqueType().getName().getUz_lat());
                cell4 = rowInTable.createCell(CellType.STRING);
                cell4.setCellValue(sparePartDao.getTechnician().getAutoNumber());
                cell5 = rowInTable.createCell(CellType.STRING);
                cell5.setCellValue(sparePartDao.getItem().equals(ItemEnum.SPARE_PART) ? sparePartDao.getSparePartType().getName().getUz_lat() : sparePartDao.getFuelType().getName().getUz_lat());
                cell6 = rowInTable.createCell(CellType.NUMERIC);
                cell6.setCellValue(sparePartDao.getPrice());
                cell7 = rowInTable.createCell(CellType.NUMERIC);
                cell7.setCellValue(sparePartDao.getQty());
                cell8 = rowInTable.createCell(CellType.NUMERIC);
                cell8.setCellValue(sparePartDao.getValue());
                counter++;
            }

            sheet.addMergedRegion(new CellRangeAddress(counter, counter, 1, 7));
            rowInTable = new CustomXSSFRow(sheet.createRow(counter), cellStyleCenterBackgroundGreenWithBorder);
            rowInTable.setHeight((short) 450);
            cell1 = rowInTable.createCell(CellType.STRING);
            cell1.setCellValue("Jami");
            cell2 = rowInTable.createCell(CellType.STRING);
            cell3 = rowInTable.createCell(CellType.STRING);
            cell4 = rowInTable.createCell(CellType.STRING);
            cell5 = rowInTable.createCell(CellType.STRING);
            cell6 = rowInTable.createCell(CellType.STRING);
            cell7 = rowInTable.createCell(CellType.STRING);
            cell8 = rowInTable.createCell(CellType.FORMULA);
            cell8.setCellFormula("SUM(I3:I" + (checkoutCosts.size() + 2) + ")");
            counter++;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
