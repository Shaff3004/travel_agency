package web.commands;

import constants.paths.Path;
import db.DBManager;
import db.entity.User;
import exception.ApplicationException;
import org.apache.log4j.Logger;
import security.Password;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
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
import net.tanesha.recaptcha.ReCaptchaImpl;
import net.tanesha.recaptcha.ReCaptchaResponse;

/**
 * Login user command
 * @author Serhii Volodin
 */
public class LoginCommand extends Command{
    private static final Logger LOG = Logger.getLogger(LoginCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ApplicationException {
        String path = Path.ERROR_PAGE;
        LOG.debug("command starts");

        String login = request.getParameter("login");
        String password = request.getParameter("password");

        String remoteAddr = request.getRemoteAddr();
        ReCaptchaImpl reCaptcha = new ReCaptchaImpl();
        reCaptcha.setPrivateKey("6LdNJ0QUAAAAANY2gjaMDsbC-5ibXnRljKvzbgTG");

        String challenge = request.getParameter("recaptcha_challenge_field");
        String uresponse = request.getParameter("recaptcha_response_field");
        ReCaptchaResponse reCaptchaResponse = reCaptcha.checkAnswer(remoteAddr, challenge, uresponse);





        LOG.trace("Request parameter: login --> " + login);

        if(validate(login,password)) {

            DBManager dbManager = DBManager.getInstance();
            User user = dbManager.getUserByLogin(login);
            HttpSession session = request.getSession();
            session.setAttribute("loginP", login);

            if(user == null){
                throw new ApplicationException("User with current login doesn't exist");
            }

            String language = (String) session.getAttribute("language");
            Locale locale = null;
            if (language.equals("ru")) {
                locale = new Locale("ru");
            } else {
                locale = new Locale("en");
            }
            ResourceBundle rb = ResourceBundle.getBundle("resources", locale);
            try {
                if(Password.hash(password).equals(user.getPassword())){

                    session.removeAttribute("message");
                    session.setAttribute("user", user);
                    LOG.trace("set session attribute: user --> " + user);
                    session.setMaxInactiveInterval(30 * 60);

                    if (reCaptchaResponse.isValid()) {
                        //out.print("Answer was entered correctly!");
                        path = Path.COMMAND_PREPARE_PAGE;
                    } else {
                        //out.print("Answer is wrong");
                        session.setAttribute("message", rb.getString("login.message.error2"));
                        path = Path.LOGIN_PAGE;
                    }

                } else {
                    session.setAttribute("message", rb.getString("login.message.error"));
                    path = Path.LOGIN_PAGE;

                }
            } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        LOG.debug("command finished");

        return path;

    }


    /**
     * validate users login and password
     * @param login String login
     * @param password String password
     * @return boolean
     */
    private static boolean validate(String login, String password){
        return validateLogin(login) && validatePassword(password);
    }

    private static boolean validateLogin(String login){
        String regexp = "^[a-zA-Z]+$";
        return login.length() != 0 && login.length() >= 4 && login.length() <= 16 && checkWithRegexp(regexp, login);
    }

    private static boolean validatePassword(String password){
        String regexp = "^[a-zA-Z0-9]+$";
        return password.length() != 0 && password.length() >= 8 && password.length() <= 16 && checkWithRegexp(regexp, password);
    }

    private static boolean checkWithRegexp(String regexp, String string){
        Pattern p = Pattern.compile(regexp);
        Matcher m = p.matcher(string);
        return m.matches();
    }
}
