package web.commands;

import constants.Emails;
import constants.paths.Path;
import db.ConnectionPool;
import db.DBManager;
import db.entity.User;
import email.MailAgent;
import exception.ApplicationException;
import org.apache.log4j.Logger;
import security.Password;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Register new user command
 *
 * @author Serhii Volodin
 */
public class  RegistrationCommand extends Command {
    private static final Logger LOG = Logger.getLogger(RegistrationCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ApplicationException {
        String path = Path.ERROR_PAGE;
        LOG.debug("command starts");

        HttpSession session = request.getSession();

        session.removeAttribute("message");


        String login = request.getParameter("login");
        String fullName = request.getParameter("fullName");
        String password = request.getParameter("password");
        String email = request.getParameter("email");


        session.setAttribute("login", login);
        session.setAttribute("fullName", fullName);
        session.setAttribute("email", email);

        LOG.trace("Request parameter: login --> " + login);
        LOG.trace("Request parameter: fullName --> " + fullName);
        LOG.trace("Request parameter: email --> " + email);

        if (validate(login, fullName, password, email)) {
            LOG.trace("validation successful");


            DBManager dbManager = DBManager.getInstance();

            String language = (String) session.getAttribute("language");
            Locale locale = null;
            if (language.equals("ru")) {
                locale = new Locale("ru");
            } else {
                locale = new Locale("en");
            }
            ResourceBundle rb = ResourceBundle.getBundle("resources", locale);

            User checkUser = dbManager.getUserByLogin(login);
            boolean checkEmail = dbManager.checkIfEmailExists(email);
            System.out.println(checkUser.toString());
            if (checkUser.getLogin() != null) {
                session.setAttribute("message2", rb.getString("registration.message.error1"));
                return Path.REGISTRATION_PAGE;
            }
            if (!checkEmail) {
                session.setAttribute("message2", rb.getString("registration.message.error2"));
                return Path.REGISTRATION_PAGE;
            } else {
                session.removeAttribute("loginP");
                User user;
                try {
                    password = Password.hash(password);
                } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
                    LOG.error("Cannot hash password");
                }
                if (dbManager.registerNewUser(login, fullName, password, email)) {
                    session.removeAttribute("user");
                    LOG.info("new user was registered");

                    user = dbManager.getUserByLogin(login);
                    LOG.trace("Found in DB: " + user);
                    session.setAttribute("user", user);
                    LOG.trace("set session attribute: user --> " + user.toString());
                    session.setMaxInactiveInterval(30 * 60);

                    path = Path.COMMAND_PREPARE_PAGE;


                    Runnable task = () -> {
                        try {
                            LOG.debug("Try to send email message to user");
                            MailAgent.sendEmail(Emails.REGISTRATION, user);
                            LOG.debug("Email was successfully sent");
                        } catch (IOException | MessagingException e) {
                            LOG.error("Cannot send email to user");
                        }
                    };
                    new Thread(task).start();
                }
            }
        }

        session.removeAttribute("login");
        session.removeAttribute("fullName");
        session.removeAttribute("email");

        LOG.debug("command finished");
        return path;
    }

    private static boolean validate(String login, String fullName, String password, String email) {
        return validateLogin(login) && validateFullName(fullName) && validatePassword(password);
    }

    private static boolean validateEmail(String email) {
        String regexp = "^([a-z0-9.-]+)@([a-z0-9.-]+).([a-z.]{2,6})$";
        return email.length() > 0 && checkWithRegexp(regexp, email);
    }

    private static boolean validateLogin(String login) {
        String regexp = "^[a-zA-Z]+$";
        return login.length() != 0 && login.length() >= 4 && login.length() <= 16 && checkWithRegexp(regexp, login);
    }


    private static boolean validateFullName(String fullName) {
        String regexp = "^[a-zA-Zа-яА-Я-'ёЁ]+[ ]{1}[a-zA-Zа-яА-Я'-ёЁ]+$";
        return fullName.length() != 0 && fullName.length() >= 5 && fullName.length() <= 40 && checkWithRegexp(regexp, fullName);
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
