package web.commands;

import constants.paths.Path;
import exception.ApplicationException;
import org.apache.log4j.Logger;
import sun.rmi.runtime.Log;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Logout user command
 * @author Serhii Volodin
 */
public class LogoutCommand extends Command {
    private static final Logger LOG = Logger.getLogger(LogoutCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ApplicationException {
        LOG.debug("command started");

        HttpSession session = request.getSession();
        if(session != null){
            session.invalidate();
            LOG.info("user make logout");
        }
        LOG.debug("command finished");
        return Path.LOGIN_PAGE;
    }
}
