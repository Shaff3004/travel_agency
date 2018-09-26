package web.commands.client;

import constants.paths.Path;
import db.DBManager;
import db.entity.User;
import exception.ApplicationException;
import org.apache.log4j.Logger;
import web.commands.Command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

public class SubscribeNewsletterCommand extends Command {
    private static final Logger LOG = Logger.getLogger(SubscribeNewsletterCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ApplicationException {
        LOG.debug("Command starts");
        String path = Path.COMMAND_PREPARE_PAGE;
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        DBManager dbManager = DBManager.getInstance();
        List<String> emails = dbManager.getAllEmailsNewsletter();

        if(!emails.contains(user.getEmail())){
            dbManager.insertEmailInNewsletter(user.getEmail());
        }
        LOG.debug("Command finished");
        return path;
    }
}
