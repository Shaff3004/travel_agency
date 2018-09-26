package web.commands;

import constants.paths.Path;
import exception.ApplicationException;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class GoHomeCommand extends Command {
    private static final Logger LOG = Logger.getLogger(GoHomeCommand.class);
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ApplicationException {
        LOG.debug("Command starts");
        HttpSession session = request.getSession();
        session.removeAttribute("resultMessage");
        LOG.debug("Command finished");
        return Path.COMMAND_PREPARE_PAGE;
    }
}
