package web.commands;

import constants.paths.Path;
import exception.ApplicationException;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class ChangeLocaleCommand extends Command {
    private static final Logger LOG = Logger.getLogger(ChangeLocaleCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ApplicationException {
        String action = "controller?";

        HttpSession session = request.getSession();
        session.removeAttribute("message");

        // get parameters
        String url = request.getParameter("url");
        String lang = request.getParameter("language");
        LOG.trace("url: " + url + ", lang: " + lang);

        // set language
        session.setAttribute("language", lang);
        LOG.trace("Language has been changed to " + lang);

        // if language changes on login page
        if (url.equals("command=logout") || url.equals("")) {
            return Path.LOGIN_PAGE;
        }

        return action + url;
    }
}
