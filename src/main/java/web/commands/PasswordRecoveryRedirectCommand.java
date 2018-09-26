package web.commands;

import constants.paths.Path;
import exception.ApplicationException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class PasswordRecoveryRedirectCommand extends Command {

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ApplicationException {
        HttpSession session = request.getSession();
        session.removeAttribute("message");
        session.removeAttribute("loginP");
        session.removeAttribute("recoverMessage");
        return Path.PASSWORD_RECOVERY_PAGE;
    }
}
