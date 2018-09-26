package web.commands.administration;

import constants.Emails;
import constants.paths.Path;
import db.DBManager;
import db.entity.Order;
import db.entity.User;
import email.MailAgent;
import exception.ApplicationException;
import org.apache.log4j.Logger;
import web.commands.Command;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Confirm order command
 * @author Serhii Volodin
 */
public class ConfirmOrderCommand extends Command {
    private static final Logger LOG = Logger.getLogger(ConfirmOrderCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ApplicationException {
        String path = Path.ERROR_PAGE;

        LOG.debug("command starts");
        int orderID = Integer.parseInt(request.getParameter("orderID"));
        int customerID = Integer.parseInt(request.getParameter("customerID"));

        LOG.trace("Request parameter: orderID --> " + orderID);
        LOG.trace("Request parameter: customerID --> " + customerID);

        DBManager dbManager = DBManager.getInstance();
        if(dbManager.confirmOrderAndSetDiscountByID(orderID, customerID)){
            LOG.info("order was confirmed and discount was added");

            path = Path.COMMAND_PREPARE_PAGE;
        }
        Order order = dbManager.getOrderById(orderID);
        User user = dbManager.getUserByID(customerID);

        LOG.trace("Found in DB: order --> " + order);
        LOG.trace("Found in DB: user -->" + user);

        LOG.debug("Try to send email message on address: " + user.getEmail());
        Runnable task = () -> {
            try {
                MailAgent.sendEmail(Emails.ORDER_CONFIRMED, order, user);
            } catch (IOException | MessagingException e) {
                LOG.error("Cannot send message to user", e);
            }
            LOG.debug("Message was successfully sent");
        };
        new Thread(task).start();
        LOG.debug("command finished");
        return path;
    }
}
