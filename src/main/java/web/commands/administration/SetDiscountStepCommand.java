package web.commands.administration;

import constants.paths.Path;
import db.DBManager;
import exception.ApplicationException;
import org.apache.log4j.Logger;
import web.commands.Command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Command that set discount step
 *
 * @author Serhii Volodin
 */
public class SetDiscountStepCommand extends Command {
    private static Logger LOG = Logger.getLogger(SetDiscountStepCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ApplicationException {
        String path = Path.ERROR_PAGE;
        LOG.debug("command starts");

        double discountStep = Double.parseDouble(request.getParameter("discountStep")) / 100;
        LOG.trace("Request parameter: discountStep --> " + discountStep);
        DBManager dbManager = DBManager.getInstance();
        List<Integer> listID = dbManager.getAllTourID();

        if(dbManager.setDiscountStep(listID, discountStep)){
            LOG.info("discount step was changed for all tours. Step: " + discountStep);
            path = Path.COMMAND_PREPARE_PAGE;
        }
        LOG.debug("command finished");
        return path;
    }
}
