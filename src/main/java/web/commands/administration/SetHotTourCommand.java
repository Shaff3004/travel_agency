package web.commands.administration;

import constants.paths.Path;
import db.DBManager;
import exception.ApplicationException;
import org.apache.log4j.Logger;
import web.commands.Command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Command that makes tour hot
 *
 * @author Serhii Volodin
 */
public class SetHotTourCommand extends Command {
    private static final Logger LOG = Logger.getLogger(SetHotTourCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ApplicationException {
        String path = Path.ERROR_PAGE;
        LOG.debug("command starts");

        String tourID = request.getParameter("tourID");
        LOG.trace("Request parameter: tourID --> " + tourID);

        DBManager dbManager = DBManager.getInstance();
        if(dbManager.setHotTourByID(Integer.parseInt(tourID))){
            LOG.info("Tour with ID = " + tourID + " now hot");
            path = Path.COMMAND_PREPARE_PAGE;
        }

        LOG.debug("command finished");
        return path;
    }
}
