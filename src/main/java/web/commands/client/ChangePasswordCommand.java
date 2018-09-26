package web.commands.client;

import constants.paths.Path;
import db.DBManager;
import db.entity.User;
import exception.ApplicationException;
import org.apache.log4j.Logger;
import security.Password;
import web.commands.Command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChangePasswordCommand extends Command {
    private static final Logger LOG = Logger.getLogger(ChangePasswordCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ApplicationException {
        LOG.debug("Command starts");

        String oldPassword = request.getParameter("oldPass");
        String newPassword = request.getParameter("newPass");

        String path = Path.ERROR_PAGE;

        if(validatePassword(newPassword)) {
            LOG.debug("Validation successful");
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("user");

            try {
                oldPassword = Password.hash(oldPassword);
                newPassword = Password.hash(newPassword);
                LOG.debug("Hashing passwords");
            } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            String language = (String) session.getAttribute("language");
            Locale locale = null;
            if (language.equals("ru")) {
                locale = new Locale("ru");
            } else {
                locale = new Locale("en");
            }
            ResourceBundle rb = ResourceBundle.getBundle("resources", locale);

            LOG.debug("Compare passwords");
            if (user.getPassword().equals(oldPassword)) {
                LOG.debug("Compare successful");
                DBManager dbManager = DBManager.getInstance();
                if (dbManager.changePasswordById(user.getId(), newPassword)) {
                    LOG.info("Password was changed");
                    session.setAttribute("action", rb.getString("securitySettings.password.info1"));
                    path = Path.COMMAND_REDIRECT_SECURITY_SETTINGS;
                }
            } else {
                session.setAttribute("action", rb.getString("securitySettings.password.info2"));
                path = Path.COMMAND_REDIRECT_SECURITY_SETTINGS;
            }
        }

        LOG.debug("Command finished");
        return path;
    }

    private static boolean validatePassword(String password) {
        String regexp = "^[a-zA-Z0-9]+$";
        return password.length() != 0 && password.length() >= 8 && password.length() <= 16 && checkWithRegexp(regexp, password);
    }


    private static boolean checkWithRegexp(String regexp, String string) {
        Pattern p = Pattern.compile(regexp);
        Matcher m = p.matcher(string);
        return m.matches();
    }
}
