package web.commands.administration.admin;

import constants.paths.Path;
import db.DBManager;
import exception.ApplicationException;
import org.apache.log4j.Logger;
import web.commands.Command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Change tour info command
 * @author Serhii Volodin
 */
public class ChangeTourInfoCommand extends Command {
    private static final Logger LOG = Logger.getLogger(ChangeTourInfoCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ApplicationException {
        String path = Path.ERROR_PAGE;
        LOG.debug("command starts");

        int tourID = Integer.parseInt(request.getParameter("tourID"));
        String type = request.getParameter("type");
        String hotel = request.getParameter("hotel");
        int persons = Integer.parseInt(request.getParameter("persons"));
        String date = request.getParameter("date");
        int price = Integer.parseInt(request.getParameter("price"));

        LOG.trace("Request parameter: tourID --> " + tourID);
        LOG.trace("Request parameter: type --> " + type);
        LOG.trace("Request parameter: hotel --> " + hotel);
        LOG.trace("Request parameter: persons --> " + persons);
        LOG.trace("Request parameter: date --> " + date);
        LOG.trace("Request parameter: price --> " + price);


        if(validate(price)) {
            DBManager dbManager = DBManager.getInstance();

            if (dbManager.changeTourInfo(tourID, type, hotel, persons, date, price)) {
                LOG.info("tour info was successfully changed");
                path = Path.COMMAND_PREPARE_PAGE;
            }
        }
        LOG.debug("command finished");
        return path;
    }


    private static boolean validate(int price){
        return price > 0 && price <= 100000;
    }
}
