package web.commands.administration.admin;

import constants.paths.Path;
import db.DBManager;
import db.entity.Tour;
import exception.ApplicationException;
import org.apache.log4j.Logger;
import web.commands.Command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Command that prepares change info page
 * @author Serhii Volodin
 */
public class PrepareChangeCommand extends Command {
    private static final Logger LOG = Logger.getLogger(PrepareChangeCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ApplicationException {
        LOG.debug("command starts");
        String tourID = request.getParameter("tourID");

        LOG.trace("Request parameter: tourID --> " + tourID);

        DBManager dbManager = DBManager.getInstance();
        Tour tour = dbManager.getTourByID(Integer.parseInt(tourID));

        LOG.trace("Found in DB: tour --> " + tour.toString());

        List<String> tourTypes = dbManager.getAllTourTypes();
        List<String> hotelTypes = dbManager.getAllHotelTypes();



        request.setAttribute("currentTour", tour);
        request.setAttribute("tourTypes", tourTypes);
        request.setAttribute("hotelTypes", hotelTypes);
        LOG.trace("set request attribute 'currentTour' --> " + tour);
        LOG.trace("set request attribute 'tourTypes' --> "+ tourTypes);
        LOG.trace("set request attribute 'hotelTypes'" + hotelTypes);

        LOG.debug("command finished");

        return Path.CHANGE_TOUR_INFO_PAGE;
    }
}
