package web;

import constants.paths.Path;
import exception.ApplicationException;
import org.apache.log4j.Logger;
import sun.rmi.runtime.Log;
import web.commands.Command;
import web.commands.CommandContainer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * Servlet
 * @author Serhii Volodin
 */

public class Controller extends HttpServlet {
    private static final Logger LOG = Logger.getLogger(Controller.class);


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processing(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processing(req, resp);
    }


    private void processing(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOG.debug("Start processing command in servlet");

        String forward = Path.ERROR_PAGE;
        String commandName = request.getParameter("command");
        LOG.trace("Request parameter: 'command': " + commandName);

        Command command = CommandContainer.getCommand(commandName);
        LOG.trace("Obtained command: " + command);
        try{
            forward = command.execute(request, response);
        } catch (ApplicationException e){
            if(e.getCause() == null){
                request.setAttribute("errorMessage", "Access forbidden. You are blocked.");
            } else {
                request.setAttribute("errorMessage", e.getMessage());
            }
            response.sendRedirect(forward);

        }

        String method = request.getMethod();

        switch (method){
            case "GET":
                LOG.trace("Forward to address: " + forward);
                LOG.debug("Controller processing finished");
                request.getRequestDispatcher(forward).forward(request, response);
                break;
            case "POST":
                LOG.trace("Redirect to address: " + forward);
                LOG.debug("Controller processing finished");
                response.sendRedirect(forward);
                break;
        }


    }
}
