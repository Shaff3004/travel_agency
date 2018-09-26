package constants.SQL;

/**
 * Class contains all SQL queries
 */
public class SQL {


    //private constructor
    private SQL(){}


    public static final String FIND_ALL_USERS = "SELECT * FROM travel_agency.users";
    public static final String FIND_ALL_USERS_SORT = "SELECT * FROM travel_agency.users WHERE role_id=2 OR role_id=3 ORDER BY full_name";
    public static final String FIND_USER_BY_LOGIN_AND_PASSWORD = "SELECT * FROM travel_agency.users WHERE login =? AND password=?";
    public static final String GET_ALL_TOURS = "SELECT * FROM travel_agency.tours";
    public static final String ADD_NEW_USER = "INSERT INTO travel_agency.users VALUES(default, 3, ?, ?, ?,?, 0,0, 1)";
    public static final String FIND_USER_BY_LOGIN = "SELECT * FROM travel_agency.users WHERE login =?";
    public static final String ADD_NEW_ORDER = "INSERT INTO travel_agency.orders VALUES(default, ?, ?, ?, ?, ?, ?,?,'registered')";
    public static final String GET_ALL_ORDERS = "SELECT * FROM travel_agency.orders";
    public static final String GET_USER_BY_ID = "SELECT * FROM travel_agency.users WHERE id = ?";
    public static final String GET_TOUR_BY_ID = "SELECT * FROM travel_agency.tours WHERE id = ?";
    public static final String CONFIRM_ORDER_BY_ID = "UPDATE travel_agency.orders SET status = 'paid' WHERE id = ?";
    public static final String CANCEL_ORDER_BY_ID = "UPDATE travel_agency.orders SET status = 'canceled' WHERE id = ?";
    public static final String BLOCK_USER_BY_ID = "UPDATE travel_agency.users SET status = 2 WHERE id = ?";
    public static final String UNBLOCK_USER_BY_ID = "UPDATE travel_agency.users SET status = 1 WHERE id = ?";
    public static final String SET_TOUR_HOT = "UPDATE travel_agency.tours SET hot = '1' WHERE id = ?";
    public static final String SET_TOUR_NORMAL = "UPDATE travel_agency.tours SET hot = '0' WHERE id = ?";
    public static final String DELETE_TOUR_BY_ID ="DELETE FROM travel_agency.tours WHERE id = ?";
    public static final String GET_ALL_TOUR_TYPES = "SELECT DISTINCT type FROM travel_agency.tours";
    public static final String GET_ALL_HOTEL_TYPES = "SELECT DISTINCT hotel FROM travel_agency.tours";
    public static final String UPDATE_TOUR_INFO = "UPDATE travel_agency.tours SET type = ?, hotel=?,persons=?,date=?,price=? WHERE id=?";
    public static final String ADD_NEW_TOUR = "INSERT INTO travel_agency.tours VALUES(default,?,?,?,?,?,?,?,?,?,'0','0')";
    public static final String GET_ALL_TOURS_ID = "SELECT id FROM travel_agency.tours";
    public static final String SET_TOUR_MAX_DISCOUNT_BY_ID = "UPDATE travel_agency.tours SET max_discount = ? WHERE id = ?";
    public static final String SET_TOUR_DISCOUNT_STEP_BY_ID = "UPDATE travel_Agency.tours SET discount_step = ? WHERE id = ?";
    public static final String GET_DISCOUNT_STEP = "SELECT DISTINCT discount_step FROM travel_agency.tours";
    public static final String GET_MAX_DISCOUNT = "SELECT DISTINCT max_discount FROM travel_agency.tours";
    public static final String SET_USER_DISCOUNT_BY_ID = "UPDATE travel_agency.users SET discount = ? WHERE id = ?";
    public static final String GET_USER_DISCOUNT_BY_ID = "SELECT discount FROM travel_agency.users WHERE id = ?";
    public static final String GET_ALL_USER_ORDERS_BY_ID = "SELECT * FROM travel_agency.orders WHERE user_id = ?";
    public static final String ADD_NEW_REPLENISHMENT = "INSERT INTO travel_agency.accounts VALUES(default,?,?,?,?,'registered')";
    public static final String GET_ALL_ACCOUNTS = "SELECT * FROM travel_agency.accounts";
    public static final String CONFIRM_REPLENISHMENT = "UPDATE travel_agency.accounts SET status = 'accepted' WHERE id =?";
    public static final String GET_USER_BALANCE_BY_ID = "SELECT balance FROM travel_agency.users WHERE id = ?";
    public static final String UPDATE_USER_BALANCE_BY_ID = "UPDATE travel_agency.users SET balance = ? WHERE id = ?";
    public static final String CANCEL_REPLENISHMENT_BY_ID = "UPDATE travel_agency.accounts SET status = 'canceled' WHERE id =?";
    public static final String GET_LATEST_USER_ORDER = "SELECT * FROM travel_agency.orders WHERE id =(SELECT MAX(id) FROM orders) AND customer = ?";
    public static final String GET_ORDER_BY_ID = "SELECT * FROM travel_agency.orders WHERE id = ?";
    public static final String GET_ACCOUNT_BY_ID = "SELECT * FROM travel_agency.accounts WHERE id =?";
    public static final String GET_LATEST_USER_REPLENISHMENT = "SELECT * FROM travel_agency.accounts WHERE id=(SELECT MAX(id) FROM accounts) AND customer =?";
    public static final String ADD_NEW_PASSWORD_RECOVERY_REQUEST = "INSERT INTO travel_agency.recovery(email, datetime) VALUES(?, ?)";
    public static final String GET_TIMESTAMP = "SELECT datetime FROM travel_agency.recovery WHERE email = ?";
    public static final String DROP_OLD_PASSWORD = "UPDATE travel_agency.users SET password = ? WHERE email =?";
    public static final String CHECK_IF_EMAIL_EXISTS = "SELECT email FROM travel_agency.users WHERE email =?";
    public static final String CHANGE_EMAIL_BY_ID = "UPDATE travel_agency.users SET email =? WHERE id =?";
    public static final String CHANGE_PASSWORD_BY_ID = "UPDATE travel_agency.users SET password =? WHERE id =?";
    public static final String GET_ALL_USER_ACCOUNTS_BY_ID = "SELECT * FROM travel_agency.accounts WHERE user_id =?";
    public static final String INSERT_AVATAR_LINK = "INSERT INTO travel_agency.avatars VALUES(default,?,?)";
    public static final String GET_AVATAR_PATH_BY_ID = "SELECT link FROM travel_agency.avatars WHERE user_id=?";
    public static final String CHECK_IF_AVATAR_EXISTS = "SELECT link FROM travel_agency.avatars WHERE user_id =?";
    public static final String UPDATE_AVATAR_LINK = "UPDATE travel_agency.avatars SET link=? WHERE user_id =?";
    public static final String GET_ORDER_STATISTICS = "SELECT customer, user_id, COUNT(id) AS count, SUM(price) AS total_price FROM travel_agency.orders WHERE status ='paid'\n" +
                                                        "GROUP BY customer\n" +
                                                        "ORDER BY SUM(price) DESC";
    public static final String GET_ACCOUNTS_STATISTICS = "SELECT customer, user_id, COUNT(id) AS count, SUM(price) AS total_price FROM travel_agency.accounts WHERE status ='accepted'\n" +
                                                            "GROUP BY customer\n" +
                                                            "ORDER BY SUM(price) DESC";
    public static final String GET_ORDER_STATISTICS_BY_DATE = "SELECT date, COUNT(id) AS count, SUM(price) AS total_price FROM travel_agency.orders WHERE status ='paid'\n" +
                                                        "GROUP BY date DESC";
    public static final String GET_ACCOUNT_STATISTICS_BY_DATE = "SELECT date, COUNT(id) AS count, SUM(price) AS total_price FROM travel_agency.accounts WHERE status ='accepted'\n" +
                                                        "GROUP BY date DESC";
    public static final String GET_ALL_EMAILS_NEWSLETTER = "SELECT email FROM travel_agency.newsletter";
    public static final String INSERT_EMAIL_IN_NEWSLETTER = "INSERT INTO travel_agency.newsletter VALUES(default,?)";
    public static final String UPDATE_TOUR_SUM = "UPDATE travel_agency.tours SET sum = ? WHERE id = ?";
}
