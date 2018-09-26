package web.listeners;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;


/**
 * Context listener. Initializes Log4J and CommandContainer.
 *
 * @author Serhii Volodin
 */
public class ContextListener implements ServletContextListener {
    private static final Logger LOG = Logger.getLogger(ContextListener.class);

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        log("Servlet context initialization starts");

        ServletContext servletContext = servletContextEvent.getServletContext();
        initLog4J(servletContext);
        initCommandContainer();

        log("Servlet context initialization finished");
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        log("Servlet context destruction started");
        //NOP
        log("Servlet context destruction was finished");
    }

    /**
     * Initializes log4j framework
     * @param servletContext instance
     */
    private void initLog4J(ServletContext servletContext){
        log("Log4J initialization started");
        try{
            PropertyConfigurator.configure(servletContext.getRealPath("WEB-INF/log4j.properties"));
        } catch (Exception e){
            LOG.error("Cannot configure Log4j", e);
        }
        log("Log4J initialization finished");
        LOG.debug("Log4J has been initialized");
    }

    /**
     * Initializes CommandContainer
     */
    private void initCommandContainer(){
        try{
            Class.forName("web.commands.CommandContainer");
        } catch (ClassNotFoundException e){
            throw new IllegalStateException("Cannot initialize Command Container", e);
        }
    }

    /**
     * Logs action to console
     * @param message that be logged
     */
    private void log(String message){
        System.out.println("[ContextListener] " + message);
    }
}
