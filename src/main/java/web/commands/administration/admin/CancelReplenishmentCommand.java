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

public class CancelReplenishmentCommand extends Command {
    private final static Logger LOG = Logger.getLogger(CancelReplenishmentCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ApplicationException {
        LOG.debug("Command starts");

        int accountId = Integer.parseInt(request.getParameter("accountID"));
        int userId = Integer.parseInt(request.getParameter("userID"));
        LOG.trace("Request parameter: accountId --> " + accountId);
        LOG.trace("Request parameter: userId --> " + userId);

        DBManager dbManager = DBManager.getInstance();

        String path = Path.ERROR_PAGE;
        if(dbManager.cancelReplenishmentById(accountId)){
            LOG.info("Replenishment was successfully canceled");
            path = Path.COMMAND_PREPARE_PAGE;
        }

        Account account = dbManager.getAccountById(accountId);
        User user =dbManager.getUserByID(userId);

        LOG.trace("Found in DB: account --> " + account);
        LOG.trace("Found in DB: user --> " + user);

        Runnable task = () -> {
            LOG.debug("Try to send email message");
            try {
                MailAgent.sendEmail(Emails.REPLENISHMENT_CANCELED, account, user);
            } catch (IOException | MessagingException e) {
                LOG.error("Cannot send email to user", e);
            }
            LOG.debug("Email was successfully sent");
        };
        new Thread(task).start();
        LOG.debug("Command finished");

        return path;
    }
}
