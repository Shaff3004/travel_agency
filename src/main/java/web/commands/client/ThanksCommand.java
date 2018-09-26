package web.commands.client;

import constants.paths.Path;
import exception.ApplicationException;
import web.commands.Command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ThanksCommand extends Command {
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ApplicationException {

        return Path.THANKS_PAGE;
    }
}
