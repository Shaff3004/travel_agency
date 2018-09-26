package web.commands.administration.admin;

import constants.Emails;
import constants.paths.Path;
import db.DBManager;
import email.MailAgent;
import exception.ApplicationException;
import org.apache.log4j.Logger;
import web.commands.Command;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SendEmailCommand extends Command {
    private static final Logger LOG = Logger.getLogger(SendEmailCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ApplicationException {
        LOG.debug("Command starts");
        String email = request.getParameter("email");
        String message = request.getParameter("message");
        String isNewsletter = request.getParameter("newsletter");

        LOG.trace("Request parameter: email --> " + email);
        LOG.trace("Request parameter: message --> " + message);
        LOG.trace("Request parameter: isNewsletter --> " + isNewsletter);

        String path = Path.ERROR_PAGE;

        if(validate(email, message)) {
            LOG.trace("Validation successful");
            if (isNewsletter != null && isNewsletter.equals("on")) {
                DBManager dbManager = DBManager.getInstance();
                List<String> emails = dbManager.getAllEmailsNewsletter();
                path = Path.COMMAND_REDIRECT_STATISTICS;
                Runnable task = () -> {
                    LOG.trace("Start newsletter");
                    for (String e : emails) {
                        try {
                            MailAgent.sendEmail(Emails.MESSAGE, e, message);
                        } catch (IOException | MessagingException e1) {
                            LOG.error("Cannot send email message", e1);

                        }
                    }
                    LOG.trace("Newsletter finished");
                };
                new Thread(task).start();
            } else {
                LOG.trace("Try to send message");
                path = Path.COMMAND_REDIRECT_STATISTICS;
                Runnable task = () -> {
                    try {
                        MailAgent.sendEmail(Emails.MESSAGE, email, message);
                    } catch (IOException | MessagingException e) {
                        LOG.error("Cannot send email message", e);
                    }
                    LOG.trace("Message was successfully sent");
                };
                new Thread(task).start();
            }

            HttpSession session = request.getSession();
            String language = (String) session.getAttribute("language");
            Locale locale = null;
            if (language.equals("ru")) {
                locale = new Locale("ru");
            } else {
                locale = new Locale("en");
            }
            ResourceBundle rb = ResourceBundle.getBundle("resources", locale);

            session.setAttribute("resultMessage", rb.getString("statistics.newsletter.OK"));
        }

        LOG.debug("Command finished");
        return path;
    }


    private static boolean validate(String email, String message){
        if(validateMessage(message)) {
            if (email != null) {
                if(validateEmail(email)){
                    return true;
                }
            } else {
                return true;
            }
        }
        return false;
    }
    private static boolean validateMessage(String message){
        return message.length() > 20;
    }
    private static boolean validateEmail(String email) {
        String regexp = "^([a-z0-9.-]+)@([a-z0-9.-]+).([a-z.]{2,6})$";
        return email.length() > 0 && checkWithRegexp(regexp, email);
    }

    private static boolean checkWithRegexp(String regexp, String string) {
        Pattern p = Pattern.compile(regexp);
        Matcher m = p.matcher(string);
        return m.matches();
    }
}
