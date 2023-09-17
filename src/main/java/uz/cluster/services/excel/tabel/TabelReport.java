package uz.cluster.services.excel.tabel;


import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import uz.cluster.dao.forms.Tabel;
import uz.cluster.services.excel.style.CustomXSSFRow;
import uz.cluster.services.excel.style.ExcelStyle;
import uz.cluster.services.salary.SalaryService;

import java.sql.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TabelReport {

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

    int counter = 0;
    private final SalaryService salaryService;

    public void tabelReportSheet(XSSFWorkbook workbook, Date beginDate, Date endDate,int directionId) {

        counter = 0;
        try {

            XSSFSheet sheet = workbook.createSheet("Tabel");

            cellStyleCenterBackgroundGreenWithBorder = ExcelStyle.cellStyleCenterBackgroundGreenWithBorder(workbook);
            cellStyleCenterOnlyFontWithBorderWithBold = ExcelStyle.cellStyleCenterOnlyFontWithBorderWithBold(workbook);
            cellStyleCenterBackgroundOrangeWithBorder = ExcelStyle.cellStyleCenterBackgroundOrangeWithBorder(workbook);

            int currColumnIndex = 1;

            for (int i = 0; i < 10; i++) {
                if (i == 3){
                    sheet.setColumnWidth(currColumnIndex++, 256 * 22);
                }else if(i == 2){
                    sheet.setColumnWidth(currColumnIndex++, 256 * 28);
                }else {
                    sheet.setColumnWidth(currColumnIndex++, 256 * 20);
                }
            }
            counter++;

            List<String> dates = salaryService.getHeaderList(beginDate,endDate,directionId);
            List<Tabel> tabels = salaryService.getEmployeeList(beginDate,endDate,directionId);

            sheet.addMergedRegion(new CellRangeAddress(counter, counter, 3, 7));
            rowInTable = new CustomXSSFRow(sheet.createRow(counter), cellStyleCenterOnlyFontWithBorderWithBold);
            rowInTable.setHeight((short) 500);
            for (int i = 1; i < 4; i++) {
                cell1 = rowInTable.createCell(CellType.STRING);
            }
            cell2 = rowInTable.createCell(CellType.STRING);
            cell2.setCellValue(beginDate.toString() + " dan " + endDate.toString() + " gacha");
            cell3 = rowInTable.createCell(CellType.STRING);
            cell4 = rowInTable.createCell(CellType.STRING);
            cell5 = rowInTable.createCell(CellType.STRING);
            counter++;

            rowInTable = new CustomXSSFRow(sheet.createRow(counter), cellStyleCenterBackgroundGreenWithBorder);
            rowInTable.setHeight((short) 750);
            cell1 = rowInTable.createCell(CellType.STRING);
            cell1.setCellValue("Raqam");
            cell2 = rowInTable.createCell(CellType.STRING);
            cell2.setCellValue("Ism va familiya");
            cell3 = rowInTable.createCell(CellType.STRING);
            cell3.setCellValue("Lavozim");
            for (String date : dates){
                cell4 = rowInTable.createCell(CellType.STRING);
                cell4.setCellValue(date);
            }
            counter++;

            int count = 0;
            for (Tabel tabel : tabels){
                count++;
                rowInTable = new CustomXSSFRow(sheet.createRow(counter), cellStyleCenterOnlyFontWithBorderWithBold);
                rowInTable.setHeight((short) 600);
                cell1 = rowInTable.createCell(CellType.STRING);
                cell1.setCellValue(count + "");
                cell2 = rowInTable.createCell(CellType.STRING);
                cell2.setCellValue(tabel.getName());
                cell3 = rowInTable.createCell(CellType.STRING);
                cell3.setCellValue(tabel.getPosition().getName().getActiveLanguage());
                for (Integer hour : tabel.getHourDays()){
                    cell4 = rowInTable.createCell(CellType.STRING);
                    cell4.setCellValue(hour + "");
                }
                counter++;
            }
//
//            sheet.addMergedRegion(new CellRangeAddress(counter, counter, 1, 3));
//            rowInTable = new CustomXSSFRow(sheet.createRow(counter), cellStyleCenterBackgroundOrangeWithBorder);
//            rowInTable.setHeight((short) 500);
//            cell1 = rowInTable.createCell(CellType.STRING);
//            cell1.setCellValue("Jami");
//            cell2 = rowInTable.createCell(CellType.STRING);
//            cell3 = rowInTable.createCell(CellType.STRING);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
