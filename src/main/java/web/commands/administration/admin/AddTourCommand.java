package web.commands.administration.admin;

import constants.paths.Path;
import db.DBManager;
import exception.ApplicationException;
import org.apache.log4j.Logger;
import web.commands.Command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Command that add new tour
 * @author Serhii Volodin
 */
public class AddTourCommand extends Command {
    private static final Logger LOG = Logger.getLogger(AddTourCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ApplicationException {
        String path = Path.ERROR_PAGE;

        LOG.debug("command starts");
        String country = request.getParameter("country");
        LOG.trace("Request parameter: country --> " + country);
        String city = request.getParameter("city");
        LOG.trace("Request parameter: city --> " + city);
        String tourType = request.getParameter("type");
        LOG.trace("Request parameter: tourType --> " + tourType);
        String hotel = request.getParameter("hotel");
        LOG.trace("Request parameter: hotel --> " + hotel);
        int persons = Integer.parseInt(request.getParameter("persons"));
        LOG.trace("Request parameter: persons --> " + persons);
        String date = request.getParameter("date");
        LOG.trace("Request parameter: date --> " + date);
        int price = Integer.parseInt(request.getParameter("price"));
        LOG.trace("Request parameter: price --> " + price);
        if(validate(price)) {
            DBManager dbManager = DBManager.getInstance();
            double discountStep = dbManager.getDiscountStep();
            LOG.trace("Found in DB: discount step --> " + discountStep);
            double maxDiscount = dbManager.getMaxDiscount();
            LOG.trace("Found in DB: max discount --> " + maxDiscount);
            if (dbManager.addNewTour(country, city, tourType, persons, hotel, date,discountStep, maxDiscount, price)) {
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
