package web.commands.administration.admin;

import constants.paths.Path;
import db.DBManager;
import exception.ApplicationException;
import org.apache.log4j.Logger;
import web.commands.Command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Delete tour command
 * @author Serhii Volodin
 */
public class DeleteTourCommand extends Command {
    private static final Logger LOG = Logger.getLogger(DeleteTourCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ApplicationException {
        String path = Path.ERROR_PAGE;

        LOG.debug("command starts");
        String tourID = request.getParameter("tourID");

        LOG.trace("Request parameter: tourID --> " + tourID);
        DBManager dbManager = DBManager.getInstance();

        if(dbManager.deleteTourByID(Integer.parseInt(tourID))){
            LOG.info("tour was successfully deleted");
            path = Path.COMMAND_PREPARE_PAGE;
        }
        LOG.debug("command finished");
        return path;
    }
}
