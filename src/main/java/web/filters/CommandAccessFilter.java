package web.filters;

import constants.paths.Path;
import db.entity.User;
import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;

/**
 * Command access filter. Security filter
 *
 * @author Serhii Volodin
 */
public class CommandAccessFilter implements Filter {
    private static final Logger LOG = Logger.getLogger(CommandAccessFilter.class);
    private static Map<Integer, List<String>> mapAccess = new HashMap<>();
    private static List<String> commons = new ArrayList<>();
    private static List<String> outOfControl = new ArrayList<>();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        LOG.debug("Filter initialization starts");

        //roles
        mapAccess.put(1, asList(filterConfig.getInitParameter("admin")));
        mapAccess.put(2, asList(filterConfig.getInitParameter("moder")));
        mapAccess.put(3, asList(filterConfig.getInitParameter("client")));
        LOG.trace("map of access --> " + mapAccess);

        //common
        commons = asList(filterConfig.getInitParameter("common"));
        LOG.trace("Common commands --> " + commons);

        //out of control
        outOfControl = asList(filterConfig.getInitParameter("out_of_control"));
        LOG.trace("Out of control commands --> " + outOfControl);

        LOG.debug("Filter initialization finished");

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        LOG.debug("Filter starts");

        if(accessAllowed(servletRequest)){
            LOG.debug("Filter finished");
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            String errorMessage = "You don't have a permission to access requested resource.";
            servletRequest.setAttribute("errorMessage", errorMessage);
            LOG.trace("Set the request attribute: errorMessage --> " + errorMessage);
            servletRequest.getRequestDispatcher(Path.ERROR_PAGE).forward(servletRequest, servletResponse);
        }
    }

    @Override
    public void destroy() {
        LOG.debug("Filter destruction starts");
        // do nothing
        LOG.debug("Filter destruction finished");
    }

    private boolean accessAllowed(ServletRequest request){
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        String commandName = httpRequest.getParameter("command");
        LOG.trace("Command name --> " + commandName);
        if(commandName == null ||commandName.isEmpty()){
            return false;
        }
        if(outOfControl.contains(commandName)){
            return true;
        }
        HttpSession session = httpRequest.getSession();
        if(session == null){
            return  false;
        }
        User user = (User) session.getAttribute("user");

        if(user == null){
            return false;
        } else {
            int userRole = user.getRoleId();
            return mapAccess.get(userRole).contains(commandName) || commons.contains(commandName);
        }
    }

    private List<String> asList(String str){
        List<String> list = new ArrayList<>();
        StringTokenizer st = new StringTokenizer(str);
        while (st.hasMoreTokens()){
            list.add(st.nextToken());
        }
        return list;
    }
}
