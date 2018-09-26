package web.commands.client;

import constants.Emails;
import constants.paths.Path;
import email.MailAgent;
import exception.ApplicationException;
import org.apache.log4j.Logger;
import web.commands.Command;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class FeedbackCommand extends Command {
    private static final Logger LOG = Logger.getLogger(FeedbackCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ApplicationException {
        LOG.debug("Command starts");
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String message = request.getParameter("message");

        LOG.trace("Request parameter: name --> " + name);
        LOG.trace("Request parameter: email --> " + email);
        LOG.trace("Request parameter: message --> " + message);

        Runnable task = () -> {
            try {
                MailAgent.sendEmail(Emails.FEEDBACK, email, name + ":\n" + message);
            } catch (IOException | MessagingException e) {
                LOG.error("Cannot send message", e);
            }
        };
        new Thread(task).start();


        LOG.debug("Command finished");
        return Path.ABOUT_US_REDIRECT_COMMAND;
    }
}
