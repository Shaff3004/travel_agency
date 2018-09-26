package web.commands;

import constants.Emails;
import constants.paths.Path;
import db.DBManager;
import email.MailAgent;
import exception.ApplicationException;
import org.apache.log4j.Logger;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;

public class CheckKeyCommand extends Command {
    private static final Logger LOG = Logger.getLogger(CheckKeyCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ApplicationException {
        LOG.debug("Command starts");

        HttpSession session = request.getSession();
        String generateKey = (String) session.getAttribute("generateKey");
        String email = (String) session.getAttribute("email");
        String requestKey = request.getParameter("key");

        String path = Path.ERROR_PAGE;

        LOG.trace("Get session attribute: generateKey --> " + generateKey);
        LOG.trace("Get session attribute: email --> " + email);
        LOG.trace("Request parameter: key --> " + requestKey);
        if (validate(requestKey)) {
            Timestamp dbTime = DBManager.getInstance().getTimestamp(email);
            dbTime.setMinutes(dbTime.getMinutes() + 2);

            LOG.trace("Found in DB: dbTime --> " + dbTime);

            Timestamp currentTime = new Timestamp(new Date().getTime());
            LOG.trace("Current time: " + currentTime);

            String language = (String) session.getAttribute("language");
            Locale locale = null;
            if (language.equals("ru")) {
                locale = new Locale("ru");
            } else {
                locale = new Locale("en");
            }
            ResourceBundle rb = ResourceBundle.getBundle("resources", locale);

            if (currentTime.getMinutes() < dbTime.getMinutes()) {
                if (requestKey.equals(generateKey)) {
                    LOG.trace("Keys are the same");
                    String newPassword = DBManager.getInstance().dropOldPassword(email);
                    LOG.info("Old password was dropped. New password was set");

                    Runnable task = () -> {
                        try {
                            LOG.trace("Try to send email message");
                            MailAgent.sendEmail(Emails.NEW_PASSWORD, email, newPassword);
                        } catch (IOException | MessagingException e) {
                            LOG.error("Cannot send email message");
                            e.printStackTrace();
                        }
                    };
                    new Thread(task).start();


                    session.setAttribute("message", rb.getString("recovery.message.allOK"));
                    session.removeAttribute("email");
                    session.removeAttribute("recoverMessage");
                    path = Path.LOGIN_PAGE;
                } else {
                    LOG.trace("Keys are different");
                    session.setAttribute("recoverMessage", rb.getString("recovery.message.invalid"));
                    path = Path.PASSWORD_RECOVERY_PAGE;
                }
            } else {
                LOG.trace("Key lifetime has expired");
                session.setAttribute("recoverMessage", rb.getString("recovery.message.expired"));
                path = Path.PASSWORD_RECOVERY_PAGE;
            }
        }

        LOG.trace("Command finished");

        return path;
    }

    private static boolean validate(String key) {
        return key.length() > 0;
    }
}
