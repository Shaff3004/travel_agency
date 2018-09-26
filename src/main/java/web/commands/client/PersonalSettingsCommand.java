package web.commands.client;

import comparators.AccountsComparator;
import comparators.OrdersComparator;
import constants.paths.Path;
import db.DBManager;
import db.entity.Account;
import db.entity.Order;
import db.entity.User;
import exception.ApplicationException;
import org.apache.log4j.Logger;
import web.commands.Command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Command that shows user personal settings
 * @author Serhii Volodin
 */
public class PersonalSettingsCommand extends Command {
    private static final Logger LOG = Logger.getLogger(PersonalSettingsCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ApplicationException {
        LOG.debug("command starts");
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        session.removeAttribute("action");

        LOG.trace("get session attribute: user --> " + user);

        DBManager dbManager = DBManager.getInstance();
        List<Order> orders = dbManager.getAllUserOrdersByID(user.getId());
        List<Account> accounts = dbManager.getAllUserAccountsById(user.getId());
        int balance = dbManager.getUserBalanceById(user.getId());
        String avatarPath = dbManager.getAvatarPathById(user.getId());
        LOG.trace("Found in DB: all orders of current user --> " + orders);
        LOG.trace("Found in DB: all accounts of current user --> " + accounts);
        LOG.trace("Found in DB: user balance --> " + balance);
        LOG.trace("Found in DB: avatar path --> " + avatarPath);


        Collections.sort(orders, new OrdersComparator());
        Collections.sort(accounts, new AccountsComparator());
        List<Order> ready = new ArrayList<>();
        for(Order o: orders){
            if(o.getStatus().equals("paid")){
                ready.add(o);
            }
        }
        request.setAttribute("orders", orders);
        request.setAttribute("accounts", accounts);
        request.setAttribute("balance", balance);
        request.setAttribute("ready", ready);
        session.setAttribute("avatar", avatarPath);

        LOG.trace("set request attribute: orders");
        LOG.trace("set request attribute: accounts");
        LOG.trace("set request attribute: balance");
        LOG.debug("command finished");

        return Path.PERSONAL_SETTINGS_PAGE;
    }
}
