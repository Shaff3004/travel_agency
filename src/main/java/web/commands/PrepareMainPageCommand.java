package web.commands;

import constants.paths.Path;
import db.DBManager;
import db.entity.Account;
import db.entity.Order;
import db.entity.Tour;
import db.entity.User;
import exception.ApplicationException;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 *Command prepares main page info
 *
 * @author Volodin Serhii
 */
public class PrepareMainPageCommand extends Command {
    private static final Logger LOG = Logger.getLogger(PrepareMainPageCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ApplicationException {
        LOG.debug("command starts");

        HttpSession session = request.getSession();
        DBManager dbManager = DBManager.getInstance();

        User user = (User)session.getAttribute("user");
        LOG.trace("get session attribute: user --> " + user);

        List<Tour> tours = dbManager.getAllTours();
        Set<String> types = new HashSet<>();
        Set<String> hotels = new HashSet<>();
        Set<Integer> peoples = new HashSet<>();
        Set<Tour> hotTours = new HashSet<>();

        for(Tour t: tours){
            types.add(t.getType());
            hotels.add(t.getHotel());
            peoples.add(t.getPersons());
            if(t.getHot() == 1){
                hotTours.add(t);
            }
        }


        String page = Path.ERROR_PAGE;
        if(user.getRoleId() == 1){
            request.setAttribute("role", 1);
            List<Order> orders = dbManager.getAllOrders();
            List<User> users = dbManager.getAllUsers();
            List<Account> accounts = dbManager.getAllAccounts();
            double discountStep = dbManager.getDiscountStep();
            double maxDiscount = dbManager.getMaxDiscount();
            request.setAttribute("orders", orders);
            request.setAttribute("users", users);
            request.setAttribute("allTours", tours);
            request.setAttribute("tourTypes", types);
            request.setAttribute("hotelTypes", hotels);
            request.setAttribute("allAccounts", accounts);
            request.setAttribute("discountStep", discountStep);
            request.setAttribute("maxDiscount", maxDiscount);
            page = Path.ADMIN_PAGE;
        }
        else if(user.getRoleId() == 2){
            if(!user.isStatus()){
                throw new ApplicationException("User is blocked");
            }
            request.setAttribute("role", 2);
            List<Order> orders = dbManager.getAllOrders();
            double discountStep = dbManager.getDiscountStep();
            double maxDiscount = dbManager.getMaxDiscount();
            request.setAttribute("orders", orders);
            request.setAttribute("allTours", tours);
            request.setAttribute("discountStep", discountStep);
            request.setAttribute("maxDiscount", maxDiscount);
            page = Path.MODER_PAGE;
        }
        else if(user.getRoleId() == 3){
            if(!user.isStatus()){
                throw new ApplicationException("User is blocked");
            }
            List<String> emails = dbManager.getAllEmailsNewsletter();
            if(!emails.contains(user.getEmail())){
                request.setAttribute("newsletter", 1);
            }
            request.setAttribute("role", 3);
            request.setAttribute("userDiscount",user.getDiscount());
            request.setAttribute("tourTypes", types);
            request.setAttribute("hotelTypes", hotels);
            request.setAttribute("personsCount", peoples);
            request.setAttribute("allTours", tours);
            request.setAttribute("hotTours", hotTours);

            page = Path.CLIENT_PAGE;
        }

        LOG.debug("command finished");
        return  page;
    }
}
