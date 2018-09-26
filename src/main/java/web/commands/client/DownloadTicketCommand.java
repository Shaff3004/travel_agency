package web.commands.client;

import constants.paths.Path;
import db.DBManager;
import db.entity.Order;
import exception.ApplicationException;
import org.apache.log4j.Logger;
import pdf.PDFMaker;
import web.commands.Command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

public class DownloadTicketCommand extends Command {
    private static final Logger LOG = Logger.getLogger(DownloadTicketCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ApplicationException{
        LOG.debug("Command starts");

        String path = Path.ERROR_PAGE;

        int orderId = Integer.parseInt(request.getParameter("orderID"));
        LOG.trace("Request parameter: orserId --> " + orderId);

        DBManager dbManager = DBManager.getInstance();
        Order order = dbManager.getOrderById(orderId);

        try {
            PDFMaker.makeTicketPDF(order);
        } catch (Exception e) {
            LOG.error("Cannot make pdf file", e);
        }

        String filePath = "D://travel_agency19_01/web/style/pdf/";
        String fileName = "ticket.pdf";

        response.setContentType("text/html");
        try {
            PrintWriter out = response.getWriter();
            response.setContentType("APPLICATION/OCTET-STREAM");
            response.setHeader("Content-Disposition","attachment; filename=\"" + fileName + "\"");
            FileInputStream fis = new FileInputStream(filePath + fileName);
            int i;
            while((i = fis.read()) != -1){
                out.write(i);
            }

            fis.close();
            out.close();
            path = Path.COMMAND_REDIRECT_PERSONAL_SETTINGS;
        } catch (IOException e) {
            LOG.error("Cannot give file", e);
        }

        return path;
    }
}
