package web.commands.client;

import constants.paths.Path;
import exception.ApplicationException;
import org.apache.log4j.Logger;
import web.commands.Command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SecuritySettingsRedirectCommand extends Command {
    private final static Logger LOG = Logger.getLogger(SecuritySettingsRedirectCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ApplicationException {
        LOG.debug("Command starts");
        LOG.debug("Command finished");
        return Path.SECURITY_SETTINGS_PAGE;
    }
}
