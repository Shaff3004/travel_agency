package web.commands.client;

import constants.Emails;
import constants.paths.Path;
import db.DBManager;
import db.entity.Order;
import email.MailAgent;
import exception.ApplicationException;
import org.apache.log4j.Logger;
import pdf.PDFMaker;
import web.commands.Command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SendTicketCommand extends Command {
    private final static Logger LOG = Logger.getLogger(SendTicketCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ApplicationException {
        LOG.debug("Command starts");

        int orderId = Integer.parseInt(request.getParameter("orderID"));
        int userId = Integer.parseInt(request.getParameter("userID"));

        LOG.trace("Request parameter: orderId --> " + orderId);
        LOG.trace("Request parameter: userId --> " + userId);

        DBManager dbManager = DBManager.getInstance();
        Order order = dbManager.getOrderById(orderId);

        Runnable task = () -> {
            try {
                LOG.trace("Try to make ticket pdf");
                PDFMaker.makeTicketPDF(order);
                LOG.info("Ticket made successfully");
                LOG.trace("Try to send email message");
                MailAgent.sendEmail(Emails.TICKET, order.getCustomer().getEmail());
                LOG.trace("Message sent");
            } catch (Exception e) {
                LOG.error("Cannot make ticket pdf and send it to email");
                e.printStackTrace();
            }
        };
        new Thread(task).start();


        LOG.debug("Command finished");
        return Path.COMMAND_REDIRECT_PERSONAL_SETTINGS;
    }
}
