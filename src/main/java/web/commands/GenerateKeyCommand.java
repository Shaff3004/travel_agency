package web.commands;

import constants.Emails;
import constants.paths.Path;
import db.DBManager;
import email.MailAgent;
import exception.ApplicationException;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GenerateKeyCommand extends Command {
    private static final Logger LOG = Logger.getLogger(GenerateKeyCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ApplicationException {
        String path = Path.ERROR_PAGE;
        LOG.debug("Command starts");

        String email = request.getParameter("email");
        LOG.trace("Request parameter: email --> " + email);
        if (validate(email)) {
            String key = DigestUtils.md5Hex(generateKey() + email);
            LOG.trace("Modify key: " + key);

            HttpSession session = request.getSession();
            session.setAttribute("email", email);
            session.setAttribute("generateKey", key);

            LOG.trace("Set session attribute: email --> " + email);
            LOG.trace("Set session attribute: generateKey --> " + key);

            Runnable task = () -> {
                try {
                    LOG.debug("Try to send email message");
                    MailAgent.sendEmail(Emails.RECOVERY, email, key);
                    LOG.debug("Message was successful sent");
                } catch (IOException | MessagingException e) {
                    LOG.error("Cannot send email", e);
                }
            };
            new Thread(task).start();


            LOG.trace("Try to add new password recovery request");
            if (DBManager.getInstance().addNewPasswordRecoveryRequest(email)) {
                LOG.info("New request was successfully added");
                String language = (String) session.getAttribute("language");
                Locale locale = null;
                if (language.equals("ru")) {
                    locale = new Locale("ru");
                } else {
                    locale = new Locale("en");
                }
                ResourceBundle rb = ResourceBundle.getBundle("resources", locale);


                session.setAttribute("recoverMessage", rb.getString("recovery.message.sent"));
                path = Path.PASSWORD_RECOVERY_PAGE;
            }
        }

        LOG.debug("Command finished");
        return path;
    }


    private static String generateKey() {
        LOG.trace("Generate key");
        String uuid = UUID.randomUUID().toString();
        LOG.trace("Generated key: " + uuid);
        return uuid;
    }

    private static boolean validate(String email) {
        String regexp = "^([a-z0-9.-]+)@([a-z0-9.-]+).([a-z.]{2,6})$";
        return email.length() > 0 && checkWithRegexp(regexp, email);
    }

    private static boolean checkWithRegexp(String regexp, String string) {
        Pattern p = Pattern.compile(regexp);
        Matcher m = p.matcher(string);
        return m.matches();
    }
}
