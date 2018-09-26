package constants.paths;

import java.util.Arrays;
import java.util.List;

/**
 * Class contains all constants that provide path to jsp pages
 */
public class Path {

    //private constructor
    private Path(){}

    public static final String LOGIN_PAGE = "Login.jsp";
    public static final String REGISTRATION_PAGE = "Registration.jsp";
    public static final String ERROR_PAGE ="WEB-INF/jsp/ErrorPage.jsp";
    public static final String PASSWORD_RECOVERY_PAGE = "PasswordRecovery.jsp";
    public static final String SECURITY_SETTINGS_PAGE = "WEB-INF/jsp/client/SecuritySettings.jsp";
    public static final String COMMAND_PREPARE_PAGE ="/controller?command=prepare-main-page";
    public static final String PERSONAL_SETTINGS_PAGE = "WEB-INF/jsp/client/PersonalSettings.jsp";
    public static final String CHANGE_TOUR_INFO_PAGE = "WEB-INF/jsp/admin/Change.jsp";
    public static final String ADMIN_PAGE = "WEB-INF/jsp/admin/AdminPage.jsp";
    public static final String MODER_PAGE = "WEB-INF/jsp/moder/ModerPage.jsp";
    public static final String CLIENT_PAGE = "WEB-INF/jsp/client/ClientPage.jsp";
    public static final String COMMAND_REDIRECT_PERSONAL_SETTINGS = "/controller?command=personal_settings";

    public static final String THANKS_PAGE ="WEB-INF/jsp/client/Thanks.jsp";
    public static final String THANKS_COMMAND ="/controller?command=thanks";
    public static final String COMMAND_REDIRECT_SECURITY_SETTINGS = "/controller?command=security_settings";
    public static final String COMMAND_REDIRECT_REGISTRATION = "/controller?command=registration_page";
    public static final String COMMAND_REDIRECT_STATISTICS = "/controller?command=statistics";
    public static final String STATISTICS_PAGE = "WEB-INF/jsp/admin/StatisticsPage.jsp";
    public static final String ABOUT_US_PAGE = "WEB-INF/jsp/client/AboutUs.jsp";
    public static final String ABOUT_US_REDIRECT_COMMAND = "/controller?command=about_us";

}
