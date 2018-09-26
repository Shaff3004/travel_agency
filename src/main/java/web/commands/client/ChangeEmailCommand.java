package web.commands.client;

import constants.paths.Path;
import db.DBManager;
import db.entity.User;
import exception.ApplicationException;
import org.apache.log4j.Logger;
import web.commands.Command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChangeEmailCommand extends Command {
    private static final Logger LOG = Logger.getLogger(ChangeEmailCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ApplicationException {
        LOG.debug("command starts");
        String email = request.getParameter("email");
        int userId = Integer.parseInt(request.getParameter("userID"));

        LOG.trace("Request parameter: email --> " + email);
        LOG.trace("Request parameter: userId --> " + userId);

        String path = Path.ERROR_PAGE;

        if(validateEmail(email)) {
            HttpSession session = request.getSession();
            String language = (String) session.getAttribute("language");
            Locale locale = null;
            if (language.equals("ru")) {
                locale = new Locale("ru");
            } else {
                locale = new Locale("en");
            }
            ResourceBundle rb = ResourceBundle.getBundle("resources", locale);

            path = Path.COMMAND_REDIRECT_SECURITY_SETTINGS;
            DBManager dbManager = DBManager.getInstance();
            if (dbManager.checkIfEmailExists(email)) {
                LOG.debug("Current email doesn't exist in DB");
                dbManager.changeEmailById(userId, email);

                LOG.info("Email was changed. New email: " + email);
                session.removeAttribute("action");
                session.setAttribute("action", rb.getString("securitySettings.email.info"));
                LOG.trace("Set session attribute: action --> " + rb.getString("securitySettings.email.info"));
                User user = dbManager.getUserByID(userId);
                session.setAttribute("user", user);

                path = Path.COMMAND_REDIRECT_SECURITY_SETTINGS;
            } else {
                session.removeAttribute("action");
                session.setAttribute("action", rb.getString("securitySettings.email.info2"));
                LOG.trace("Set session attribute: action --> " + rb.getString("securitySettings.email.info2"));
            }
        }

        LOG.debug("Command finished");

        return path;
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
