package web.commands.client;

import constants.Emails;
import constants.paths.Path;
import db.DBManager;
import db.entity.Order;
import db.entity.Tour;
import db.entity.User;
import email.MailAgent;
import exception.ApplicationException;
import org.apache.log4j.Logger;
import web.commands.Command;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Buy tour command
 *
 * @author Serhii Volodin
 */
public class BuyCommand extends Command {
    private static final Logger LOG = Logger.getLogger(BuyCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ApplicationException {
        String path = Path.ERROR_PAGE;
        LOG.debug("command starts");
        HttpSession session = request.getSession();
        String tourId = request.getParameter("tourID");
        LOG.trace("Request parameter: tourId --> " + tourId);
        User user = (User) session.getAttribute("user");

        LOG.trace("Session attribute: user" + user.toString());

        DBManager dbManager = DBManager.getInstance();
        if(dbManager.addNewOrder(Integer.parseInt(tourId), user.getId())){
            LOG.info("order was successfully added");
            Tour tour = dbManager.getTourByID(Integer.parseInt(tourId));

            dbManager.updateTourSum(tour.getId(), tour.getSum());
            LOG.trace("Sum field was updated");

            Order order = dbManager.getLatestOrderByFullName(user.getFullName());
            LOG.trace("Found in DB: order --> " + order);
            LOG.trace("Try to send email to user");
            Runnable task = () -> {
                try {
                    MailAgent.sendEmail(Emails.ORDER_REGISTERED, order, user);
                } catch (IOException | MessagingException e) {
                    LOG.error("Cannot send email to user", e);

                }
                LOG.trace("Email was successfully sent");
            };
            new Thread(task).start();

            path = Path.COMMAND_PREPARE_PAGE;
        }
        LOG.debug("command finished");
        return path;
    }
}
