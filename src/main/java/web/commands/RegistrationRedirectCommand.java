package web.commands;

import constants.paths.Path;
import exception.ApplicationException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class RegistrationRedirectCommand extends Command {

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ApplicationException {
        HttpSession session = request.getSession();
        session.removeAttribute("message");
        session.removeAttribute("message2");
        session.removeAttribute("loginP");
        session.removeAttribute("login");
        session.removeAttribute("fullName");
        session.removeAttribute("email");
        return Path.REGISTRATION_PAGE;
    }
}
