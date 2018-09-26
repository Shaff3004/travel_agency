package web.commands.administration.admin;

import constants.paths.Path;
import db.DBManager;
import db.entity.*;
import exception.ApplicationException;
import org.apache.log4j.Logger;
import web.commands.Command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class RedirectStatisticsPageCommand extends Command {
    private static final Logger LOG = Logger.getLogger(RedirectStatisticsPageCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ApplicationException {
        LOG.debug("Command starts");


        DBManager dbManager = DBManager.getInstance();
        List<OrderStatistics> orderStatistics = dbManager.getAllOrderStatistics();
        List<AccountStatistics> accountStatistics = dbManager.getAllAccountStatistics();
        List<OrderDateStatistic> orderDateStatistics = dbManager.getAllOrderDateStatistics();
        List<AccountDateStatistic> accountDateStatistics = dbManager.getAllAccountDateStatistics();
        List<Tour> tours = dbManager.getAllTours();
        request.setAttribute("order_statistics", orderStatistics);
        request.setAttribute("account_statistics", accountStatistics);
        request.setAttribute("order_date_statistics", orderDateStatistics);
        request.setAttribute("account_date_statistics", accountDateStatistics);
        request.setAttribute("allTours", tours);


        return Path.STATISTICS_PAGE;
    }
}
