package web.commands.client;

import comparators.HotTourComparator;
import constants.paths.Path;
import db.DBManager;
import db.entity.Tour;
import exception.ApplicationException;
import org.apache.log4j.Logger;
import web.commands.Command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * Command that can find tour by parameters
 * @author Serhii Volodin
 */
public class FindTourCommand extends Command {
    private static final Logger LOG = Logger.getLogger(FindTourCommand.class);
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ApplicationException {

        LOG.debug("command starts");

        List<Tour> resultTour = new ArrayList<>();

        String type = request.getParameter("type");
        String peoples = request.getParameter("peoples");
        String hotel = request.getParameter("hotel");
        String price = request.getParameter("price");

        LOG.trace("Request parameter: type --> " + type);
        LOG.trace("Request parameter: peoples --> " + peoples);
        LOG.trace("Request parameter: hotel --> " + hotel);
        LOG.trace("Request parameter: price --> " + price);

        DBManager dbManager = DBManager.getInstance();
        List<Tour> tours = dbManager.getAllTours();

        for(Tour t: tours){
            if(type.equals(t.getType()) && peoples.equals("" + t.getPersons()) && hotel.equals(t.getHotel())
                    && t.getPrice() <= Integer.valueOf(price)){
                resultTour.add(t);
            }
        }
        LOG.trace("Found in DB: " + resultTour.toString());

        Collections.sort(resultTour, new HotTourComparator());
        HttpSession session = request.getSession();
        session.removeAttribute("message");
        if(resultTour.isEmpty()){
            String language = (String) session.getAttribute("language");
            Locale locale = null;
            if (language.equals("ru")) {
                locale = new Locale("ru");
            } else {
                locale = new Locale("en");
            }
            ResourceBundle rb = ResourceBundle.getBundle("resources", locale);
            session.setAttribute("message", rb.getString("client.badSearch"));
        }
        session.setAttribute("resultTour", resultTour);
        LOG.trace("set the session attribute: resultTour --> " + resultTour);

        LOG.debug("command finished");
        return Path.COMMAND_PREPARE_PAGE;
    }
}
