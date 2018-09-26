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
 * Command that cancels order
 * @author Serhii Volodin
 */
public class CancelOrderCommand extends Command {
    private static final Logger LOG = Logger.getLogger(CancelOrderCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ApplicationException {
        String path = Path.ERROR_PAGE;
        LOG.debug("command starts");
        int orderId = Integer.parseInt(request.getParameter("orderID"));
        int customerId = Integer.parseInt(request.getParameter("customerID"));
        int price = Integer.parseInt(request.getParameter("price"));
        LOG.trace("Request parameter: orderID --> " + orderId);
        LOG.trace("Request parameter: customerID --> " + customerId);
        LOG.trace("Request parameter: price --> " + price);

        DBManager dbManager = DBManager.getInstance();


        Order order = dbManager.getOrderById(orderId);
        User user = dbManager.getUserByID(customerId);
        LOG.trace("Found in DB: order --> " + order);
        LOG.trace("Found in DB: user --> " + user);

        if(dbManager.cancelOrderByID(orderId) && dbManager.rechargeBalanceById(customerId, price)){
            LOG.info("order was cancelled");

            Runnable task = () -> {
                LOG.debug("Try to send email message");
                try {
                    MailAgent.sendEmail(Emails.ORDER_CANCELED, order, user);
                } catch (IOException | MessagingException e) {
                    LOG.error("Cannot send message to user", e);
                }
                LOG.debug("Message was successfully sent");
            };
            new Thread(task).start();
            path = Path.COMMAND_PREPARE_PAGE;

        }
        LOG.debug("Command finished");
        return path;
    }
}
