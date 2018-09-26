package web.commands.administration;

import constants.paths.Path;
import db.DBManager;
import exception.ApplicationException;
import org.apache.log4j.Logger;
import web.commands.Command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Unblock user command
 *
 * @author Serhii Volodin
 */
public class UnblockUserCommand extends Command {
    private static final Logger LOG = Logger.getLogger(UnblockUserCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ApplicationException {
        String path = Path.ERROR_PAGE;
        LOG.debug("command starts");

        String userID = request.getParameter("userID");
        LOG.trace("Request parameter: userID --> " + userID);
        DBManager dbManager = DBManager.getInstance();

        if(dbManager.unblockUserByID(Integer.parseInt(userID))){
            LOG.info("user with ID = " + userID + " is active from this moment");
            path = Path.COMMAND_PREPARE_PAGE;
        }

        LOG.debug("command finished");
        return path;
    }
}
