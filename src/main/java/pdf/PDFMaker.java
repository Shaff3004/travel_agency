package pdf;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import db.entity.Order;
import db.entity.Tour;
import db.entity.User;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Date;


public class PDFMaker {
    private static int FONT_SIZE_SMALL = 16;
    private static int FONT_SIZE_BIG = 32;


    /*public static void main(String[] args) throws Exception {
        //createTemplate();

        Order order = new Order();
        order.setTour(new Tour());
        order.getTour().setDate(new Date());
        order.getTour().setCity("Volnovaha");
        order.setCustomer(new User());
        order.getCustomer().setFullName("Serhii Volodin");

        makeTicketPDF(order);

    }*/

    public static void createTemplate() throws Exception {
        Document document = new Document();

        Font font1 = new Font(Font.FontFamily.HELVETICA,
                FONT_SIZE_BIG, Font.BOLD);
        Font font2 = new Font(Font.FontFamily.HELVETICA,
                FONT_SIZE_SMALL, Font.ITALIC | Font.UNDERLINE);

        PdfWriter.getInstance(document,
                new FileOutputStream("/travel_agency19_01/web/style/pdf/template.pdf"));

        document.open();

        // отцентрированный параграф
        Paragraph title = new Paragraph("Tickets", font1);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(FONT_SIZE_BIG);
        document.add(title);

        Image ticket = Image.getInstance("/travel_agency19_01/web/style/img/ticket.jpg");
        ticket.setAlignment(Element.ALIGN_CENTER);
        document.add(ticket);

        document.close();
    }

    public static void makeTicketPDF(Order order) throws Exception {

        PdfReader reader = new PdfReader(
                new FileInputStream("D://travel_agency19_01/web/style/pdf/template.pdf"));



        PdfStamper stamper = new PdfStamper(reader,
                new FileOutputStream("D://travel_agency19_01/web/style/pdf/ticket.pdf"));


        PdfContentByte stream = stamper.getOverContent(1);
        stream.beginText();
        stream.setColorFill(BaseColor.BLACK);

        BaseFont font = BaseFont.createFont();
        stream.setFontAndSize(font, FONT_SIZE_SMALL);

        //dynamic context

        float fitSize = 15.5f;
        stream.setFontAndSize(font, fitSize);
        stream.setTextMatrix(110, 640);
        stream.showText(order.getTour().getCity());


        float dataSize = 8f;
        stream.setFontAndSize(font, dataSize);

        String date = String.valueOf(order.getTour().getDate());

        stream.setTextMatrix(85, 605);
        stream.showText(date);


        float timeSize = 16;
        stream.setFontAndSize(font, timeSize);
        stream.setTextMatrix(165, 605);
        stream.showText("01:00");
        stream.setTextMatrix(95, 570);
        stream.showText("2");
        stream.setTextMatrix(95, 535);
        stream.showText("04");
        stream.setTextMatrix(420, 640);
        stream.showText(order.getCustomer().getFullName());
        stream.setTextMatrix(460, 620);
        stream.showText(order.getTour().getCity());
        stream.setFontAndSize(font, dataSize - 1);
        stream.setTextMatrix(436, 595);
        stream.showText(date);
        stream.setFontAndSize(font, timeSize);
        stream.setTextMatrix(496, 595);
        stream.showText("01:00");
        stream.setTextMatrix(446, 567);
        stream.showText("2");
        stream.setTextMatrix(446, 540);
        stream.showText("04");

        stream.endText();
        stamper.setFullCompression();
        stamper.close();
    }
}
