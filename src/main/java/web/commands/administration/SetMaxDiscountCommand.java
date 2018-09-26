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
 * Command that set max discount for tours
 *
 * @author Serhii Volodin
 */
public class SetMaxDiscountCommand extends Command {
    private static final Logger LOG = Logger.getLogger(SetMaxDiscountCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ApplicationException {
        String path = Path.ERROR_PAGE;
        LOG.debug("command starts");

        double maxDiscount = Double.parseDouble(request.getParameter("maxDiscountInput")) / 100;
        LOG.trace("Request parameter: maxDiscount --> " + maxDiscount);

        DBManager dbManager = DBManager.getInstance();
        List<Integer> listID = dbManager.getAllTourID();

        if (dbManager.setMaxDiscount(listID, maxDiscount)) {
            LOG.info("max discount for all tours was changed. Max discount now: " + maxDiscount);
            path = Path.COMMAND_PREPARE_PAGE;
        }
        LOG.debug("command finished");
        return path;
    }
}
