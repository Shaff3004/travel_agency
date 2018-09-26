package web.commands.administration.admin;

import constants.Emails;
import constants.paths.Path;
import db.DBManager;
import db.entity.Account;
import db.entity.User;
import email.MailAgent;
import exception.ApplicationException;
import org.apache.log4j.Logger;
import web.commands.Command;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ConfirmReplenishmentCommand extends Command {
    private static final Logger LOG = Logger.getLogger(ConfirmReplenishmentCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ApplicationException {
        LOG.debug("Command starts");
        System.out.println(request.getParameter("userID"));
        int userId = Integer.parseInt(request.getParameter("userID"));
        int accountId = Integer.parseInt(request.getParameter("accountID"));
        ;
        int money = Integer.parseInt(request.getParameter("money"));

        LOG.trace("Request parameter: userId --> " + userId);
        LOG.trace("Request parameter: accountId --> " + accountId);
        LOG.trace("Request parameter: money --> " + money);

        DBManager dbManager = DBManager.getInstance();

        String path = Path.ERROR_PAGE;
        if (dbManager.changeAccountStatusAndUpdateUserBalance(userId, accountId, money)) {
            LOG.info("Replenishment was confirmed");
            path = Path.COMMAND_PREPARE_PAGE;
        }

        Account account = dbManager.getAccountById(accountId);
        User user = dbManager.getUserByID(userId);

        LOG.trace("Found in DB: account --> " + account);
        LOG.trace("Found in DB: user --> " + user);
        LOG.debug("Try to send email message");

        Runnable task = () -> {
            try {
                MailAgent.sendEmail(Emails.REPLENISHMENT_CONFIRMED, account, user);
            } catch (IOException | MessagingException e) {
                LOG.error("Cannot send email to user", e);
            }
        };
        new Thread(task).start();

        LOG.debug("Command finished");
        return path;
    }
}
