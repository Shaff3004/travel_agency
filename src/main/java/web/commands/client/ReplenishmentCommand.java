package web.commands.client;

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
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ReplenishmentCommand extends Command {
    private static final Logger LOG = Logger.getLogger(ReplenishmentCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ApplicationException {
        LOG.debug("Command starts");

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        java.util.Date utilDate = new java.util.Date();
        java.sql.Date date = new java.sql.Date(utilDate.getTime());
        String number = request.getParameter("credit_number");
        int money = Integer.parseInt(request.getParameter("money"));

        LOG.trace("Session get attribute: user --> " + user);
        LOG.trace("Request parameter: credit_number --> " + number);
        LOG.trace("Request parameter: money --> " + money);

        String path = Path.ERROR_PAGE;


        if(validate(number, money)) {
            LOG.trace("Validation successful");
            DBManager dbManager = DBManager.getInstance();

            if (dbManager.addNewReplenishment(user.getId(), user.getFullName(), date, money)) {
                LOG.info("New replenishment was added");
                path = Path.THANKS_PAGE;
            }

            Account account = dbManager.getLatestUserAccountByFullName(user.getFullName());
            LOG.trace("Found in DB: account --> " + account);

            Runnable task = () -> {
                try {
                    LOG.debug("Try to send email message");
                    MailAgent.sendEmail(Emails.REPLENISHMENT_REGISTERED, account, user);
                } catch (IOException | MessagingException e) {
                    LOG.error("Cannot send message to user", e);

                }
                LOG.debug("Email was successfully sent");
            };
            new Thread(task).start();
        }
        LOG.debug("command finished");
        return path;
    }



    private static boolean validate(String number, int money){
        return validateCreditNumber(number) && validateAmount(money);
    }
    private static boolean validateCreditNumber(String number){
        if(number.length() == 0 || number.length() > 16){
            return false;
        }
        String regexp = "^[0-9]+$";
        return checkWithRegexp(regexp, number);
    }

    private static boolean validateAmount(int money){
        return money >= 0 && money <= 30000;

    }
    private static boolean checkWithRegexp(String regexp, String string){
        Pattern p = Pattern.compile(regexp);
        Matcher m = p.matcher(string);
        return m.matches();
    }
}
