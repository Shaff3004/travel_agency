package web.commands.administration;

import constants.paths.Path;
import db.DBManager;
import exception.ApplicationException;
import org.apache.log4j.Logger;
import web.commands.Command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Set tour status to normal command
 *
 * @author Serhii Volodin
 */
public class SetNormalTourCommand extends Command {
    private static final Logger LOG = Logger.getLogger(SetNormalTourCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ApplicationException {
        String path = Path.ERROR_PAGE;
        LOG.debug("command starts");

        String tourID = request.getParameter("tourID");
        LOG.trace("Request parameter: tourID --> " + tourID);
        DBManager dbManager = DBManager.getInstance();

        if(dbManager.setNormalTourByID(Integer.parseInt(tourID))){
            LOG.info("Tour with ID = " + tourID + " is not hot from this moment");
            path = Path.COMMAND_PREPARE_PAGE;
        }
        LOG.debug("command finished");
        return path;
    }
}
