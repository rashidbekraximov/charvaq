package uz.cluster.services.pdf;

import com.lowagie.text.*;
import com.lowagie.text.pdf.CMYKColor;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import uz.cluster.dao.purchase.PurchaseDao;
import uz.cluster.dao.purchase.PurchasedProductDao;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class PdfGenerator {

    public void generate(PurchaseDao purchase, HttpServletResponse response) throws DocumentException, IOException {
        // Creating the Object of Document
        Document document = new Document(PageSize.A5);
        document.setDocumentLanguage("ru");
        // Getting instance of PdfWriter
        PdfWriter.getInstance(document, response.getOutputStream());
        // Opening the created document to change it
        document.open();
        // Creating font
        // Setting font style and size
        Font fontTitle1 = FontFactory.getFont(FontFactory.COURIER);
        fontTitle1.setSize(13);
        Font fontTitle2 = FontFactory.getFont(FontFactory.COURIER);
        fontTitle2.setSize(20);
        // Creating paragraph

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        // Formatting LocalDate to String
        String formattedDate = purchase.getDate().format(formatter);
        Paragraph paragraph1 = new Paragraph(formattedDate + "y", fontTitle1);
        // Aligning the paragraph in the document
        paragraph1.setAlignment(Paragraph.ALIGN_RIGHT);
        // Adding the created paragraph in the document
        document.add(paragraph1);
        // Creating paragraph
        Paragraph paragraph2 = new Paragraph("YUK XATI " + purchase.getDocumentCode(), fontTitle2);
        // Aligning the paragraph in the document
        paragraph2.setAlignment(Paragraph.ALIGN_CENTER);

        paragraph2.setSpacingAfter(10.5f);
        // Adding the created paragraph in the document
        document.add(paragraph2);
        // Creating paragraph
        Paragraph paragraph3 = new Paragraph("Kimdan: \" Chorbog' sayohat maskani \" MCHJ ", fontTitle1);
        // Aligning the paragraph in the document
        paragraph3.setAlignment(Paragraph.ALIGN_LEFT);
        // Adding the created paragraph in the document
        document.add(paragraph3);
        // Creating paragraph
        Paragraph paragraph4 = new Paragraph("Kimga: " + purchase.getClient(), fontTitle1);
        // Aligning the paragraph in the document
        paragraph4.setAlignment(Paragraph.ALIGN_LEFT);
        // Adding the created paragraph in the document
        document.add(paragraph4);
        // Creating paragraph
        Paragraph paragraph6 = new Paragraph("Dov: __________ dan _________ gacha ________", fontTitle1);
        // Aligning the paragraph in the document
        paragraph6.setAlignment(Paragraph.ALIGN_LEFT);
        paragraph6.setSpacingAfter(10.5f);
        // Adding the created paragraph in the document
        document.add(paragraph6);
        // Creating a table of the 4 columns
        PdfPTable table = new PdfPTable(6);
        // Setting width of the table, its columns and spacing
        table.setWidthPercentage(100f);
        table.setWidths(new int[] {2,3,3,2,3,3});
        table.setSpacingBefore(5);
        // Create Table Cells for the table header
        PdfPCell cell = new PdfPCell();
        // Setting the background color and padding of the table cell
        cell.setBackgroundColor(CMYKColor.WHITE);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(5);
        // Creating font
        // Setting font style and size
        Font font = FontFactory.getFont(FontFactory.COURIER);
        font.setColor(CMYKColor.BLACK);
        // Adding headings in the created table cell or  header
        // Adding Cell to table
        cell.setPhrase(new Phrase("T/R", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Nomi", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("O'lchov birliki", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Soni ", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Narxi ", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Summasi ", font));
        table.addCell(cell);
        // Iterating the list of students
        byte i = 0;
        for (PurchasedProductDao product: purchase.getPurchasedProductList()) {
            i++;
            table.addCell(String.valueOf(i));
            table.addCell(product.getProductType().getName().getActiveLanguage());
            table.addCell("m/kub");
            table.addCell("" + product.getWeight());
            table.addCell("" + product.getPrice());
            table.addCell("" + product.getValue());
        }
        if (purchase.getPurchasedProductList().size() <= 6){
            for (int j = 1; j <= 7; j++) {
                i++;
                table.addCell(String.valueOf(i));
                table.addCell("");
                table.addCell("");
                table.addCell("");
                table.addCell("");
                table.addCell("");
            }
        }
        document.add(table);

        String name = "";
        String auto = ",";

        if(purchase.getTechnician() != null){
            name = purchase.getTechnician().getTechniqueType().getName().getActiveLanguage();
            auto += purchase.getTechnician().getAutoNumber();
        }

        // Creating paragraph
        Paragraph paragraph5 = new Paragraph("Texnika: " + name  + auto, fontTitle1);
        // Aligning the paragraph in the document
        paragraph5.setAlignment(Paragraph.ALIGN_LEFT);
        // Adding the created paragraph in the document
        document.add(paragraph5);
        // Creating paragraph
        Paragraph paragraph7 = new Paragraph("BERDIM _______________ OLDIM ______________ ", fontTitle1);
        // Aligning the paragraph in the document
        paragraph7.setAlignment(Paragraph.ALIGN_LEFT);
        // Adding the created paragraph in the document
        document.add(paragraph7);
        // Closing the document
        document.close();
    }

}