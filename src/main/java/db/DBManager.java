package db;

import comparators.AccountsComparator;
import comparators.HotTourComparator;
import comparators.OrdersComparator;
import constants.SQL.SQL;
import db.entity.*;
import exception.DBException;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.log4j.Logger;
import security.Password;

import javax.xml.transform.Result;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

/**
 * DB manager works with MySQL DB. Contains all required methods for working with DB
 *
 * @author Serhii Volodin
 */
public class DBManager {

    private static final Logger LOG = Logger.getLogger(DBManager.class);

    private Statement stmt = null;
    private ResultSet rs = null;
    private PreparedStatement pstmt = null;
    private static DBManager instance;

    private DBManager() {
    }

    /**
     * Singleton
     *
     * @return DBManager instance
     */
    public static synchronized DBManager getInstance() {
        if (instance == null) {
            instance = new DBManager();
        }
        return instance;
    }


    /**
     * Returns list of all orders sorted by status
     *
     * @return List of all orders
     * @throws DBException instance
     */
    public List<Order> getAllOrders() throws DBException {
        ResultSet rs = null;
        Connection con = null;
        List<Order> orders = new ArrayList<>();

        try {
            con = ConnectionPool.getConnection();
            stmt = con.createStatement();
            rs = stmt.executeQuery(SQL.GET_ALL_ORDERS);
            while (rs.next()) {
                orders.add(getOrder(rs));
            }

        } catch (SQLException e) {
            LOG.error("Cannot get all orders", e);
            throw new DBException("Cannot get all orders. Reason: " + e.getMessage(), e);
        } finally {
            close(rs);
            close(stmt);
            close(con);
        }
        Collections.sort(orders, new OrdersComparator());

        return orders;
    }

    /**
     * Confirm order by ID and set discount to user
     *
     * @param orderID int
     * @param userID  int
     * @return true if order was confirmed and discount counted
     * @throws DBException instance
     */
    public boolean confirmOrderAndSetDiscountByID(int orderID, int userID) throws DBException {
        Connection con = null;

        try {
            con = ConnectionPool.getConnection();
            con.setAutoCommit(false);
            confirmOrderByID(orderID);
            setUserDiscountByID(userID);
        } catch (SQLException e) {
            rollback(con);
            LOG.error("Cannot confirm order and set discount by ID. Reason: " + e.getMessage(), e);
            throw new DBException("Cannot confirm order and set discount by ID. Reason: " + e.getMessage(), e);
        }
        commit(con);
        close(con);
        return true;
    }


    public boolean addNewReplenishment(int userId, String userName, java.sql.Date date, int money) throws DBException {
        Connection con = null;
        try {
            con = ConnectionPool.getConnection();
            pstmt = con.prepareStatement(SQL.ADD_NEW_REPLENISHMENT);
            pstmt.setInt(1, userId);
            pstmt.setString(2, userName);
            pstmt.setDate(3, date);
            pstmt.setInt(4, money);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DBException("Cannot add new replenishment", e);
        } finally {
            close(pstmt);
            close(con);
        }
        return true;
    }

    public List<Account> getAllAccounts() throws DBException {
        Connection con = null;
        List<Account> accounts = new ArrayList<>();

        con = ConnectionPool.getConnection();
        try {
            stmt = con.createStatement();
            rs = stmt.executeQuery(SQL.GET_ALL_ACCOUNTS);
            while (rs.next()) {
                accounts.add(getAccount(rs));
            }
        } catch (SQLException e) {
            throw new DBException("Cannot get all accounts" + e.getMessage(), e);
        }
        Collections.sort(accounts, new AccountsComparator());
        return accounts;
    }

    /**
     * Confirm order by ID
     *
     * @param id user ID
     * @throws DBException instance
     */
    private void confirmOrderByID(int id) throws DBException {
        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = ConnectionPool.getConnection();
            pstmt = con.prepareStatement(SQL.CONFIRM_ORDER_BY_ID);
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            LOG.error("Cannot confirm order by ID. Reason: " + e.getMessage(), e);
            throw new DBException("cannot confirm order by ID. Reason: " + e.getMessage(), e);
        } finally {
            close(pstmt);
            close(con);
        }
    }


    public void updateTourSum(int id, int sum) throws DBException {
        Connection con = null;
        try {
            con = ConnectionPool.getConnection();
            pstmt = con.prepareStatement(SQL.UPDATE_TOUR_SUM);
            pstmt.setInt(1, sum + 1);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            LOG.error("Cannot update tour sum", e);
            throw new DBException("Cannot update tour sum. Reason: " + e.getMessage(), e);
        } finally {
            close(pstmt);
            close(con);
        }
    }


    public boolean changeAccountStatusAndUpdateUserBalance(int userId, int accountId, int money) throws DBException {
        Connection con = null;


        try {
            con = ConnectionPool.getConnection();
            con.setAutoCommit(false);

            if (confirmReplenishmentById(accountId) && rechargeBalanceById(userId, money)) {
                commit(con);
            }
        } catch (SQLException e) {
            rollback(con);
            throw new DBException("Cannot make transaction", e);
        }

        close(con);

        return true;
    }

    private boolean withdrawMoneyFromBalanceById(int id, int money) throws DBException {
        Connection con = null;


        try {
            int userBalance = getUserBalanceById(id);
            userBalance -= money;
            con = ConnectionPool.getConnection();
            pstmt = con.prepareStatement(SQL.UPDATE_USER_BALANCE_BY_ID);
            pstmt.setInt(1, userBalance);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DBException("Cannot withdraw money from balance", e);
        } finally {
            close(pstmt);
            close(con);
        }
        return true;
    }


    public boolean rechargeBalanceById(int id, int money) throws DBException {
        Connection con = null;
        try {
            int userBalance = getUserBalanceById(id);
            userBalance += money;
            con = ConnectionPool.getConnection();
            pstmt = con.prepareStatement(SQL.UPDATE_USER_BALANCE_BY_ID);
            pstmt.setInt(1, userBalance);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DBException("Cannot update balance" + e.getMessage(), e);
        } finally {
            close(pstmt);
            close(con);
        }
        return true;
    }


    public Account getLatestUserAccountByFullName(String fullName) throws DBException {
        Connection con = null;
        Account account = null;

        con = ConnectionPool.getConnection();
        try {
            pstmt = con.prepareStatement(SQL.GET_LATEST_USER_REPLENISHMENT);
            pstmt.setString(1, fullName);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                account = getAccount(rs);
            }
        } catch (SQLException e) {
            throw new DBException("Cannot get the latest user replenishment", e);
        } finally {
            close(rs);
            close(pstmt);
            close(con);
        }
        return account;
    }

    public Account getAccountById(int id) throws DBException {
        Connection con = null;
        Account account = null;

        con = ConnectionPool.getConnection();
        try {
            pstmt = con.prepareStatement(SQL.GET_ACCOUNT_BY_ID);
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                account = getAccount(rs);
            }
        } catch (SQLException e) {
            throw new DBException("Cannot get account by ID", e);
        } finally {
            close(rs);
            close(pstmt);
            close(con);
        }
        return account;
    }

    private boolean confirmReplenishmentById(int id) throws DBException {
        Connection con = null;

        con = ConnectionPool.getConnection();
        try {
            if (con != null) {
                pstmt = con.prepareStatement(SQL.CONFIRM_REPLENISHMENT);
                pstmt.setInt(1, id);
            }
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DBException("Cannot confirm replenishment", e);
        } finally {
            close(pstmt);
            close(con);
        }
        return true;
    }


    public List<Account> getAllUserAccountsById(int id) throws DBException {
        Connection con = null;
        List<Account> accounts = new ArrayList<>();


        try {
            con = ConnectionPool.getConnection();
            pstmt = con.prepareStatement(SQL.GET_ALL_USER_ACCOUNTS_BY_ID);
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                accounts.add(getAccount(rs));
            }
        } catch (SQLException e) {
            LOG.error("Cannot get all user accounts", e);
            throw new DBException("Cannot get all user accounts", e);
        } finally {
            close(rs);
            close(pstmt);
            close(con);
        }
        return accounts;
    }

    /**
     * Set discount to user by ID
     *
     * @param id user ID
     * @throws DBException instance
     */
    private void setUserDiscountByID(int id) throws DBException {
        Connection con = null;

        double discountStep = getDiscountStep();
        double maxDiscount = getMaxDiscount();
        double userDiscount = getUserDiscountByID(id);
        double currentDiscount = userDiscount + discountStep;

        if (currentDiscount > maxDiscount) {
            currentDiscount = maxDiscount;
        }

        try {
            con = ConnectionPool.getConnection();
            pstmt = con.prepareStatement(SQL.SET_USER_DISCOUNT_BY_ID);
            pstmt.setDouble(1, currentDiscount);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            LOG.error("Cannot set discount to user", e);
            throw new DBException("Cannot set discount to user. Reason: " + e.getMessage(), e);
        } finally {
            close(pstmt);
            close(con);
        }
    }

    public int getUserBalanceById(int id) throws DBException {
        Connection con = null;
        int userBalance = 0;

        con = ConnectionPool.getConnection();
        try {
            pstmt = con.prepareStatement(SQL.GET_USER_BALANCE_BY_ID);
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                userBalance = rs.getInt("balance");
            }

        } catch (SQLException e) {
            throw new DBException("Cannot get user balance", e);
        } finally {
            close(rs);
            close(pstmt);
            close(con);
        }
        return userBalance;
    }

    /**
     * Returns user total discount by ID
     *
     * @param id user ID
     * @return double
     * @throws DBException instance
     */
    private double getUserDiscountByID(int id) throws DBException {
        Connection con = null;
        ResultSet rs = null;
        double userDiscount = 0;

        try {
            con = ConnectionPool.getConnection();
            pstmt = con.prepareStatement(SQL.GET_USER_DISCOUNT_BY_ID);
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                userDiscount = rs.getDouble("discount");
            }
        } catch (SQLException e) {
            LOG.error("Cannot get user discount", e);
            throw new DBException("Cannot get user discount", e);
        } finally {
            close(rs);
            close(pstmt);
            close(con);
        }
        return userDiscount;
    }


    public boolean cancelReplenishmentById(int id) throws DBException {
        Connection con = null;

        con = ConnectionPool.getConnection();
        try {
            pstmt = con.prepareStatement(SQL.CANCEL_REPLENISHMENT_BY_ID);
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DBException("Cannot cancel replenishment" + e.getMessage(), e);
        } finally {
            close(pstmt);
            close(con);
        }
        return true;
    }

    /**
     * Delete tour by ID
     *
     * @param id tour ID
     * @return true if tour was deleted
     * @throws DBException instance
     */
    public boolean deleteTourByID(int id) throws DBException {
        Connection con = null;

        try {
            con = ConnectionPool.getConnection();
            pstmt = con.prepareStatement(SQL.DELETE_TOUR_BY_ID);
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            LOG.error("Cannot delete tour by ID. Reason: " + e.getMessage(), e);
            throw new DBException("cannot delete tour by ID. Reason: " + e.getMessage(), e);
        } finally {
            close(pstmt);
            close(con);
        }
        return true;
    }

    /**
     * Block user by ID
     *
     * @param id user ID
     * @return true if user was blocked
     * @throws DBException instance
     */
    public boolean blockUserByID(int id) throws DBException {
        Connection con = null;

        try {
            con = ConnectionPool.getConnection();
            pstmt = con.prepareStatement(SQL.BLOCK_USER_BY_ID);
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            LOG.error("Cannot block user by ID. Reason: " + e.getMessage(), e);
            throw new DBException("cannot block user by ID. Reason: " + e.getMessage(), e);
        } finally {
            close(pstmt);
            close(con);
        }
        return true;
    }

    /**
     * Changes tour info by current values
     *
     * @param id      tour id
     * @param type    type of rest
     * @param hotel   type of hotel
     * @param persons number of persons
     * @param date    start date
     * @param price   total price
     * @return true if tour info was changed
     * @throws DBException instance
     */
    public boolean changeTourInfo(int id, String type, String hotel, int persons, String date, int price) throws DBException {
        Connection con = null;


        try {
            con = ConnectionPool.getConnection();
            pstmt = con.prepareStatement(SQL.UPDATE_TOUR_INFO);
            pstmt.setString(1, type);
            pstmt.setString(2, hotel);
            pstmt.setInt(3, persons);
            pstmt.setString(4, date);
            pstmt.setInt(5, price);
            pstmt.setInt(6, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            LOG.error("Cannot update tour info by ID. Reason: " + e.getMessage(), e);
            throw new DBException("Cannot update tour info by ID. Reason: " + e.getMessage(), e);
        } finally {
            close(pstmt);
            close(con);
        }
        return true;
    }


    /**
     * Add tour by current values
     *
     * @param country      country name
     * @param city         city name
     * @param type         type of rest
     * @param persons      number of persons
     * @param hotel        type of hotel
     * @param date         start date
     * @param discountStep discount that gives to user when purchase confirmed
     * @param maxDiscount  the highest possible discount
     * @param price        total price
     * @return true if tour was added
     * @throws DBException instance
     */
    public boolean addNewTour(String country, String city, String type, int persons, String hotel,
                              String date, double discountStep, double maxDiscount, int price) throws DBException {
        Connection con = null;


        try {
            con = ConnectionPool.getConnection();
            pstmt = con.prepareStatement(SQL.ADD_NEW_TOUR);
            pstmt.setString(1, country);
            pstmt.setString(2, city);
            pstmt.setString(3, type);
            pstmt.setInt(4, persons);
            pstmt.setString(5, hotel);
            pstmt.setString(6, date);
            pstmt.setDouble(7, discountStep);
            pstmt.setDouble(8, maxDiscount);
            pstmt.setInt(9, price);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            LOG.error("Cannot add new tour with current parameters.", e);
            throw new DBException("Cannot add new tour with current parameters.", e);
        } finally {
            close(pstmt);
            close(con);
        }
        return true;
    }

    /**
     * Return discount step for all tours
     *
     * @return double value
     * @throws DBException instance
     */
    public double getDiscountStep() throws DBException {
        Connection con = null;

        double discountStep = 0;

        try {
            con = ConnectionPool.getConnection();
            stmt = con.createStatement();
            rs = stmt.executeQuery(SQL.GET_DISCOUNT_STEP);
            while (rs.next()) {
                discountStep = rs.getDouble("discount_step");
            }
        } catch (SQLException e) {
            LOG.error("Cannot get discount step", e);
            throw new DBException("Cannot get discount step", e);
        } finally {
            close(rs);
            close(stmt);
            close(con);
        }
        return discountStep;
    }

    /**
     * Return max discount for all tours
     *
     * @return double value
     * @throws DBException instance
     */
    public double getMaxDiscount() throws DBException {
        Connection con = null;


        double maxDiscount = 0;

        try {
            con = ConnectionPool.getConnection();
            stmt = con.createStatement();
            rs = stmt.executeQuery(SQL.GET_MAX_DISCOUNT);
            while (rs.next()) {
                maxDiscount = rs.getDouble("max_discount");
            }
        } catch (SQLException e) {
            LOG.error("Cannot get max discount", e);
            throw new DBException("Cannot get max discount", e);
        } finally {
            close(rs);
            close(stmt);
            close(con);
        }

        return maxDiscount;
    }

    /**
     * Makes tour hot by ID
     *
     * @param id tour ID
     * @return true if status of tour was changed
     * @throws DBException instance
     */
    public boolean setHotTourByID(int id) throws DBException {
        Connection con = null;


        try {
            con = ConnectionPool.getConnection();
            pstmt = con.prepareStatement(SQL.SET_TOUR_HOT);
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            LOG.error("Cannot set tour hot", e);
            throw new DBException("Cannot set tour hot. Reason: " + e.getMessage(), e);
        } finally {
            close(pstmt);
            close(con);
        }
        return true;
    }

    /**
     * Set tour status to normal by ID
     *
     * @param id tour ID
     * @return true if tour status of tour was changed
     * @throws DBException instance
     */
    public boolean setNormalTourByID(int id) throws DBException {
        Connection con = null;


        try {
            con = ConnectionPool.getConnection();
            pstmt = con.prepareStatement(SQL.SET_TOUR_NORMAL);
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            LOG.error("Cannot set tour to normal", e);
            throw new DBException("Cannot set tour to normal. Reason: " + e.getMessage(), e);
        } finally {
            close(pstmt);
            close(con);
        }
        return true;
    }


    /**
     * Unblock user by ID
     *
     * @param id user ID
     * @return true if user was unblocked
     * @throws DBException instance
     */
    public boolean unblockUserByID(int id) throws DBException {
        Connection con = null;


        try {
            con = ConnectionPool.getConnection();
            pstmt = con.prepareStatement(SQL.UNBLOCK_USER_BY_ID);
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            LOG.error("Cannot unblock user by ID", e);
            throw new DBException("Cannot unblock user by ID. Reason: " + e.getMessage(), e);
        } finally {
            close(pstmt);
            close(con);
        }
        return true;
    }

    /**
     * Cancel order by order ID
     *
     * @param id order ID
     * @return true if order was canceled
     * @throws DBException instance
     */
    public boolean cancelOrderByID(int id) throws DBException {
        Connection con = null;

        try {
            con = ConnectionPool.getConnection();
            pstmt = con.prepareStatement(SQL.CANCEL_ORDER_BY_ID);
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            LOG.error("Cannot cancel order by ID", e);
            throw new DBException("cannot cancel order by ID. Reason: " + e.getMessage(), e);
        } finally {
            close(pstmt);
            close(con);
        }
        return true;
    }

    /**
     * Return User object by ID
     *
     * @param id user ID
     * @return User object
     * @throws DBException instance
     */
    public User getUserByID(int id) throws DBException {
        Connection con = null;
        User user = new User();


        try {
            con = ConnectionPool.getConnection();
            pstmt = con.prepareStatement(SQL.GET_USER_BY_ID);
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                user = getUser(rs);
            }
        } catch (SQLException e) {
            LOG.error("Cannpt find user by ID", e);
            throw new DBException("cannot find user by ID", e);
        } finally {
            close(rs);
            close(pstmt);
            close(con);
        }
        return user;

    }

    /**
     * Returns list of all tour types
     *
     * @return List
     * @throws DBException instance
     */
    public List<String> getAllTourTypes() throws DBException {
        Connection con = null;

        List<String> types = new ArrayList<>();

        try {
            con = ConnectionPool.getConnection();
            stmt = con.createStatement();
            rs = stmt.executeQuery(SQL.GET_ALL_TOUR_TYPES);
            while (rs.next()) {
                types.add(rs.getString("type"));
            }

        } catch (SQLException e) {
            LOG.error("Cannot get all tour types", e);
            throw new DBException("Cannot get all tour types.", e);
        } finally {
            close(rs);
            close(stmt);
            close(con);
        }
        return types;
    }

    /**
     * Returns List of all hotel types
     *
     * @return List
     * @throws DBException instance
     */
    public List<String> getAllHotelTypes() throws DBException {
        Connection con = null;

        List<String> hotels = new ArrayList<>();

        try {
            con = ConnectionPool.getConnection();
            stmt = con.createStatement();
            rs = stmt.executeQuery(SQL.GET_ALL_HOTEL_TYPES);
            while (rs.next()) {
                hotels.add(rs.getString("hotel"));
            }
        } catch (SQLException e) {
            LOG.error("Cannot get all hotel types", e);
            throw new DBException("Cannot get all hotel types", e);
        } finally {
            close(rs);
            close(stmt);
            close(con);
        }
        return hotels;
    }

    /**
     * Returns tour by ID
     *
     * @param id tour ID
     * @return Tour instance
     * @throws DBException instance
     */
    public Tour getTourByID(int id) throws DBException {
        Connection con = null;
        Tour tour = new Tour();

        try {
            con = ConnectionPool.getConnection();
            pstmt = con.prepareStatement(SQL.GET_TOUR_BY_ID);
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                tour = getTour(rs);
            }
        } catch (SQLException e) {
            LOG.error("Cannot find tour by ID", e);
            throw new DBException("Cannot find tour by ID" + e.getMessage(), e);
        } finally {
            close(rs);
            close(pstmt);
            close(con);
        }
        return tour;
    }

    /**
     * Return User instance by login
     *
     * @param login user login
     * @return User instance
     * @throws DBException instance
     */
    public User getUserByLogin(String login) throws DBException {
        Connection con = null;
        User user = new User();

        try {
            con = ConnectionPool.getConnection();
            pstmt = con.prepareStatement(SQL.FIND_USER_BY_LOGIN);
            pstmt.setString(1, login);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                user = getUser(rs);
            }
        } catch (SQLException e) {
            LOG.error("Cannot find user with current login", e);
            throw new DBException("Cannot find user with current login", e);
        } finally {
            close(rs);
            close(pstmt);
            close(con);
        }
        return user;
    }

    /**
     * Returns List of all orders that was made by current user
     *
     * @param id user ID
     * @return List
     * @throws DBException instance
     */
    public List<Order> getAllUserOrdersByID(int id) throws DBException {
        ResultSet rs = null;
        Connection con = null;
        List<Order> orders = new ArrayList<>();
        try {
            con = ConnectionPool.getConnection();
            pstmt = con.prepareStatement(SQL.GET_ALL_USER_ORDERS_BY_ID);
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                orders.add(getOrder(rs));
            }
        } catch (SQLException e) {
            LOG.error("Cannot get all user orders", e);
            throw new DBException("Cannot get all user orders. Reason " + e.getMessage(), e);
        } finally {
            close(rs);
            close(pstmt);
            close(con);
        }
        return orders;
    }

    public Order getOrderById(int id) throws DBException {
        ResultSet rs = null;
        Connection con = null;
        Order order = null;

        con = ConnectionPool.getConnection();
        try {
            pstmt = con.prepareStatement(SQL.GET_ORDER_BY_ID);
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                order = getOrder(rs);
            }
        } catch (SQLException e) {
            throw new DBException("Cannot get order by ID" + e.getMessage(), e);
        } finally {
            close(rs);
            close(pstmt);
            close(con);
        }
        return order;
    }

    /**
     * Add new order by current values
     *
     * @param tourID tout ID
     * @param userID user ID
     * @return true if order was added
     * @throws DBException instance
     */
    public boolean addNewOrder(int tourID, int userID) throws DBException {
        Connection con = null;
        Tour tour = getTourByID(tourID);
        User user = getUserByID(userID);
        Order order = new Order();
        int totalPrice = (int) (tour.getPrice() - tour.getPrice() * user.getDiscount());
        int balance = user.getBalance();
        try {
            if (balance < totalPrice) {
                throw new DBException();
            }
        } catch (DBException e) {
            throw new DBException("Low balance", e);
        }

        try {

            con = ConnectionPool.getConnection();
            con.setAutoCommit(false);
            withdrawMoneyFromBalanceById(userID, totalPrice);

            pstmt = con.prepareStatement(SQL.ADD_NEW_ORDER);
            pstmt.setInt(1, user.getId());
            pstmt.setString(2, user.getFullName());
            pstmt.setInt(3, tour.getId());
            pstmt.setString(4, tour.getCountry());
            pstmt.setString(5, tour.getCity());
            pstmt.setInt(6, totalPrice);
            java.util.Date utilDate = new java.util.Date();
            java.sql.Date date = new java.sql.Date(utilDate.getTime());
            pstmt.setDate(7, date);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            rollback(con);
            LOG.error("Cannot add new order with current parameters");
            throw new DBException("Cannot add new order with current parameters" + e.getMessage(), e);
        }
        commit(con);

        close(pstmt);
        close(con);

        return true;
    }

    public List<String> getAllEmailsNewsletter() throws DBException {
        Connection con = null;
        ResultSet rs = null;
        List<String> result = new ArrayList<>();

        try {
            con = ConnectionPool.getConnection();
            rs = con.createStatement().executeQuery(SQL.GET_ALL_EMAILS_NEWSLETTER);
            while(rs.next()){
                result.add(rs.getString("email"));
            }
        } catch (SQLException e) {
            LOG.error("Cannot get all emails newsletter");
            throw new DBException("Cannot get all emails newsletter", e);
        } finally {
            close(rs);
            close(con);
        }
        return result;
    }


    public boolean insertEmailInNewsletter(String email) throws DBException {
        Connection con = null;

        con = ConnectionPool.getConnection();
        try {
            pstmt = con.prepareStatement(SQL.INSERT_EMAIL_IN_NEWSLETTER);
            pstmt.setString(1, email);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DBException("Cannot insert new row in table", e);
        } finally {
            close(pstmt);
            close(con);
        }
        return true;
    }

    public Order getLatestOrderByFullName(String fullName) throws DBException {
        Connection con = null;
        ResultSet rs = null;
        Order order = null;


        try {
            con = ConnectionPool.getConnection();
            pstmt = con.prepareStatement(SQL.GET_LATEST_USER_ORDER);
            pstmt.setString(1, fullName);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                order = getOrder(rs);
            }
        } catch (SQLException e) {
            throw new DBException("Cannot get the latest order of this user" + e.getMessage(), e);
        } finally {
            close(rs);
            close(pstmt);
            close(con);
        }
        return order;
    }


    /**
     * Register new user by current values
     *
     * @param login    user login
     * @param fullName user full name
     * @param password user password
     * @return true if new user was registered
     * @throws DBException instance
     */
    public boolean registerNewUser(String login, String fullName, String password, String email) throws DBException {
        Connection con = null;
        try {
            con = ConnectionPool.getConnection();
            pstmt = con.prepareStatement(SQL.ADD_NEW_USER);
            pstmt.setString(1, login);
            pstmt.setString(2, password);
            pstmt.setString(3, fullName);
            pstmt.setString(4, email);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            LOG.error("Cannot register new user");
            throw new DBException("Cannot register new user. Reason: " + e.getMessage(), e);
        } finally {
            close(pstmt);
            close(con);
        }

        return true;
    }

    /**
     * Returns List of all tours
     *
     * @return List
     * @throws DBException instance
     */
    public List<Tour> getAllTours() throws DBException {
        Connection con = null;
        ArrayList<Tour> tours = new ArrayList<>();

        try {
            con = ConnectionPool.getConnection();
            stmt = con.createStatement();
            rs = stmt.executeQuery(SQL.GET_ALL_TOURS);
            while (rs.next()) {
                tours.add(getTour(rs));
            }
        } catch (SQLException e) {
            LOG.error("cannot get all tours", e);
            throw new DBException("cannot get all tours", e);
        } finally {
            close(stmt, con, rs);
        }
        Collections.sort(tours, new HotTourComparator());

        return tours;
    }


    /**
     * Returns User instance by login and password
     *
     * @param login    user login
     * @param password user password
     * @return User instance
     * @throws DBException instance
     */
    public User getUserByLoginAndPassword(String login, String password) throws DBException {
        Connection con = null;
        User user;

        try {
            con = ConnectionPool.getConnection();
            pstmt = con.prepareStatement(SQL.FIND_USER_BY_LOGIN_AND_PASSWORD);
            pstmt.setString(1, login);
            pstmt.setString(2, password);
            rs = pstmt.executeQuery();
            rs.next();

            user = getUser(rs);
        } catch (SQLException e) {
            LOG.error("User with current login/password doesn't exist");
            throw new DBException("User with current login/password doesn't exist", e);
        } finally {
            close(rs);
            close(pstmt);
            close(con);


        }
        return user;
    }

    public boolean checkIfEmailExists(String email) throws DBException {
        Connection con = null;


        try {
            con = ConnectionPool.getConnection();
            pstmt = con.prepareStatement(SQL.CHECK_IF_EMAIL_EXISTS);
            pstmt.setString(1, email);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return false;
            }
        } catch (SQLException e) {
            throw new DBException("Current email already in use", e);
        } finally {
            close(rs);
            close(pstmt);
            close(con);
        }
        return true;
    }

    public boolean changePasswordById(int id, String password) throws DBException {
        Connection con = null;
        try {
            con = ConnectionPool.getConnection();
            pstmt = con.prepareStatement(SQL.CHANGE_PASSWORD_BY_ID);
            pstmt.setString(1, password);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            LOG.error("Cannot change password", e);
            throw new DBException("Cannot change password", e);
        } finally {
            close(pstmt);
            close(con);
        }
        return true;
    }

    public boolean changeEmailById(int id, String email) throws DBException {
        Connection con = null;


        try {
            con = ConnectionPool.getConnection();
            pstmt = con.prepareStatement(SQL.CHANGE_EMAIL_BY_ID);
            pstmt.setString(1, email);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            LOG.error("Cannot change email");
            throw new DBException("Cannot change email", e);
        } finally {
            close(pstmt);
            close(con);
        }
        return true;
    }


    public boolean addNewPasswordRecoveryRequest(String email) throws DBException {
        Connection con = null;

        con = ConnectionPool.getConnection();
        try {
            pstmt = con.prepareStatement(SQL.ADD_NEW_PASSWORD_RECOVERY_REQUEST);
            pstmt.setString(1, email);
            pstmt.setTimestamp(2, new Timestamp(new Date().getTime()));
            pstmt.executeUpdate();

        } catch (SQLException e) {
            LOG.error("Cannot add new password recovery request", e);
            throw new DBException("Cannot add new password recovery request", e);
        } finally {
            close(pstmt);
            close(con);
        }
        return true;
    }

    public Timestamp getTimestamp(String email) throws DBException {
        Connection con = null;
        Timestamp timestamp = null;


        try {
            con = ConnectionPool.getConnection();
            pstmt = con.prepareStatement(SQL.GET_TIMESTAMP);
            pstmt.setString(1, email);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyy-MM-dd hh:mm:ss.SSS");
                Date parsedDate = dateFormat.parse(rs.getString(1));
                timestamp = new Timestamp(parsedDate.getTime());
            }
        } catch (SQLException e) {
            LOG.error("Cannot get timestamp");
            throw new DBException("Cannot get timestamp", e);
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            close(rs);
            close(pstmt);
            close(con);
        }
        return timestamp;
    }


    public boolean insertAvatarPathById(int id, String link) throws DBException {
        Connection con = null;


        try {
            con = ConnectionPool.getConnection();
            pstmt = con.prepareStatement(SQL.INSERT_AVATAR_LINK);
            pstmt.setInt(1, id);
            pstmt.setString(2, link);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DBException("Cannot insert avatar link", e);
        } finally {
            close(pstmt);
            close(con);
        }

        return true;
    }

    public String getAvatarPathById(int id) throws DBException {
        Connection con = null;
        ResultSet rs = null;
        String path = null;

        con = ConnectionPool.getConnection();
        try {
            pstmt = con.prepareStatement(SQL.GET_AVATAR_PATH_BY_ID);
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();
            while(rs.next()){
                path = rs.getString("link");
            }
        } catch (SQLException e) {
            LOG.error("Cannpt get avatar path by id", e);
            throw new DBException("Cannot get avatar path by id", e);
        } finally {
            close(rs);
            close(pstmt);
            close(con);
        }
        return path;
    }

    public boolean checkIfAvatarExists(int id) throws DBException {
        Connection con = null;
        ResultSet rs = null;


        try {
            con = ConnectionPool.getConnection();
            pstmt = con.prepareStatement(SQL.CHECK_IF_AVATAR_EXISTS);
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();
            if(rs.next()){
                return false;
            }
        } catch (SQLException e) {
            throw new DBException("Cannot check if avatar exists", e);
        } finally {
            close(rs);
            close(pstmt);
            close(con);
        }
        return true;
    }

    public void updateAvatarLinkById(int id, String link) throws DBException {
        Connection con = null;


        try {
            con = ConnectionPool.getConnection();
            pstmt = con.prepareStatement(SQL.UPDATE_AVATAR_LINK);
            pstmt.setString(1, link);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DBException("Cannot update avatar link", e);
        } finally {
            close(pstmt);
            close(con);
        }
    }

    public String dropOldPassword(String email) throws DBException {
        Connection con = null;
        String newPassword;
        newPassword = RandomStringUtils.randomAlphanumeric(8);

        String copyPassword = newPassword;

        con = ConnectionPool.getConnection();
        try {
            copyPassword = Password.hash(copyPassword);
            pstmt = con.prepareStatement(SQL.DROP_OLD_PASSWORD);
            pstmt.setString(1, copyPassword);
            pstmt.setString(2, email);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DBException("Cannot drop old password", e);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        } finally {
            close(pstmt);
            close(con);
        }
        return newPassword;
    }

    public List<OrderDateStatistic> getAllOrderDateStatistics() throws DBException {
        Connection con = null;
        ResultSet rs = null;
        List<OrderDateStatistic> result = new ArrayList<>();


        try {
            con = ConnectionPool.getConnection();
            rs = con.createStatement().executeQuery(SQL.GET_ORDER_STATISTICS_BY_DATE);
            while(rs.next()){
                result.add(getOrderDateStatistic(rs));
            }
        } catch (SQLException e) {
            throw new DBException("Cannot get all order date statistics", e);
        } finally {
            close(rs);
            close(con);
        }
        return result;
    }

    public List<AccountDateStatistic> getAllAccountDateStatistics() throws DBException {
        Connection con = null;
        ResultSet rs = null;
        List<AccountDateStatistic> result = new ArrayList<>();

        try {
            con = ConnectionPool.getConnection();
            rs = con.createStatement().executeQuery(SQL.GET_ACCOUNT_STATISTICS_BY_DATE);
            while(rs.next()){
                result.add(getAccountDateStatistic(rs));
            }
        } catch (SQLException e) {
            LOG.error("Cannot get account date statistic", e);
            throw new DBException("Cannot get account date statistic", e);
        } finally {
            close(rs);
            close(con);
        }
        return result;
    }


    public List<OrderStatistics> getAllOrderStatistics() throws DBException {
        Connection con = null;
        ResultSet rs = null;
        List<OrderStatistics> result = new ArrayList<>();

        try {
            con = ConnectionPool.getConnection();
            stmt = con.createStatement();
            rs = stmt.executeQuery(SQL.GET_ORDER_STATISTICS);
            while (rs.next()){
                result.add(getOrderStatistics(rs));
            }
        } catch (SQLException e) {
            LOG.error("Cannot get order statistics", e);
            throw new DBException("Cannot get order statistics", e);
        } finally {
            close(rs);
            close(stmt);
            close(con);
        }
        return result;
    }

    public List<AccountStatistics> getAllAccountStatistics() throws DBException {
        Connection con = null;
        ResultSet rs = null;
        List<AccountStatistics> result = new ArrayList<>();

        con = ConnectionPool.getConnection();
        try {
            rs = con.createStatement().executeQuery(SQL.GET_ACCOUNTS_STATISTICS);
            while(rs.next()){
                result.add(getAccountStatistics(rs));
            }
        } catch (SQLException e) {
            LOG.error("Cannot get account statistics");
            throw new DBException("Cannot get accounts statistics", e);
        } finally {
            close(rs);
            close(con);
        }

        return result;
    }


    private AccountDateStatistic getAccountDateStatistic(ResultSet rs) throws SQLException {
        AccountDateStatistic ads = new AccountDateStatistic();
        ads.setDate(rs.getDate("date"));
        ads.setCount(rs.getInt("count"));
        ads.setTotalPrice(rs.getInt("total_price"));
        return ads;
    }

    private OrderDateStatistic getOrderDateStatistic(ResultSet rs) throws SQLException {
        OrderDateStatistic ods = new OrderDateStatistic();

        ods.setDate(rs.getDate("date"));
        ods.setCountOrder(rs.getInt("count"));
        ods.setTotalPrice(rs.getInt("total_price"));

        return ods;
    }
    private OrderStatistics getOrderStatistics(ResultSet rs) throws SQLException {
        OrderStatistics os = new OrderStatistics();
        os.setCustomer(rs.getString("customer"));
        os.setCustomerId(rs.getInt("user_id"));
        os.setOrderCount(rs.getInt("count"));
        os.setSum(rs.getInt("total_price"));
        return os;
    }

    private AccountStatistics getAccountStatistics(ResultSet rs) throws SQLException {
        AccountStatistics as = new AccountStatistics();
        as.setCustomer(rs.getString("customer"));
        as.setCustomerId(rs.getInt("user_id"));
        as.setAccountsCount(rs.getInt("count"));
        as.setSum(rs.getInt("total_price"));
        return as;
    }


    /**
     * Set discount step for all tours
     *
     * @param listId   list of id all tours
     * @param discount discount value
     * @return true if discount was changed
     * @throws DBException instance
     */
    public boolean setDiscountStep(List<Integer> listId, double discount) throws DBException {
        Connection con = null;


        try {
            con = ConnectionPool.getConnection();
            con.setAutoCommit(false);
            for (int id : listId) {
                pstmt = con.prepareStatement(SQL.SET_TOUR_DISCOUNT_STEP_BY_ID);
                pstmt.setDouble(1, discount);
                pstmt.setInt(2, id);
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            rollback(con);
            LOG.error("Cannot set tour discount step", e);
            throw new DBException("Cannot set tour discount step: " + e.getMessage(), e);
        }
        commit(con);

        close(pstmt);
        close(con);

        return true;
    }


    /**
     * Set max discount for all tours
     *
     * @param listId   list of all id of tours
     * @param discount discount value
     * @return true if discount was changed
     * @throws DBException instance
     */
    public boolean setMaxDiscount(List<Integer> listId, double discount) throws DBException {
        Connection con = null;


        try {
            con = ConnectionPool.getConnection();
            con.setAutoCommit(false);
            for (int id : listId) {
                pstmt = con.prepareStatement(SQL.SET_TOUR_MAX_DISCOUNT_BY_ID);
                pstmt.setDouble(1, discount);
                pstmt.setInt(2, id);
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            rollback(con);
            LOG.error("Cannot set tour discount", e);
            throw new DBException("Cannot set tour discount: " + e.getMessage(), e);
        }
        commit(con);

        close(pstmt);
        close(con);

        return true;
    }

    /**
     * Returns List of all tour ID
     *
     * @return List instance
     * @throws DBException instance
     */
    public List<Integer> getAllTourID() throws DBException {
        Connection con = null;
        List<Integer> list = new ArrayList<>();


        try {
            con = ConnectionPool.getConnection();
            stmt = con.createStatement();
            rs = stmt.executeQuery(SQL.GET_ALL_TOURS_ID);
            while (rs.next()) {
                list.add(rs.getInt("id"));
            }
        } catch (SQLException e) {
            LOG.error("Cannot get all tours ID", e);
            throw new DBException("Cannot get all tours ID. Reason: " + e.getMessage(), e);
        } finally {
            close(rs);
            close(stmt);
            close(con);
        }
        return list;
    }

    /**
     * Returns List of all id users
     *
     * @return List instance
     * @throws DBException instance
     */
    public List<User> getAllUsers() throws DBException {
        Connection con = null;
        ArrayList<User> users = new ArrayList<>();

        try {
            con = ConnectionPool.getConnection();
            stmt = con.createStatement();
            rs = stmt.executeQuery(SQL.FIND_ALL_USERS_SORT);
            while (rs.next()) {
                users.add(getUser(rs));
            }
        } catch (SQLException e) {
            LOG.error("Cannot get all users", e);
            throw new DBException("Cannot get all users", e);
        } finally {
            close(rs);
            close(stmt);
            close(con);
        }

        return users;
    }


    /**
     * create new user and fill his fields by rs object
     *
     * @param rs ResultSet object
     * @return User object
     * @throws SQLException instance
     */
    private User getUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setRoleId(rs.getInt("role_id"));
        user.setLogin(rs.getString("login"));
        user.setPassword(rs.getString("password"));
        user.setFullName(rs.getString("full_name"));
        user.setEmail(rs.getString("email"));
        user.setDiscount(rs.getDouble("discount"));
        user.setBalance(rs.getInt("balance"));
        int status = rs.getInt("status");
        if (status == 1) {
            user.setStatus(true);
        } else {
            user.setStatus(false);
        }
        return user;
    }

    /**
     * create new Account and fill fields by rs object
     *
     * @param rs instance
     * @return Account instance
     * @throws SQLException instance
     */
    private Account getAccount(ResultSet rs) throws SQLException {
        Account account = new Account();
        account.setId(rs.getInt("id"));
        account.setUserId(rs.getInt("user_id"));
        account.setUserName(rs.getString("customer"));
        account.setDate(rs.getDate("date"));
        account.setPrice(rs.getInt("price"));
        account.setStatus(rs.getString("status"));
        return account;
    }

    /**
     * Create new tour and fill fields by rs object
     *
     * @param rs ResultSet instance
     * @return Tour instance
     * @throws SQLException instance
     */
    private Tour getTour(ResultSet rs) throws SQLException {
        Tour tour = new Tour();
        tour.setId(rs.getInt("id"));
        tour.setCountry(rs.getString("country"));
        tour.setCity(rs.getString("city"));
        tour.setType(rs.getString("type"));
        tour.setPersons(rs.getInt("persons"));
        tour.setHotel(rs.getString("hotel"));
        tour.setDate(rs.getDate("date"));
        tour.setDiscountStep(rs.getDouble("discount_step"));
        tour.setMaxDiscount(rs.getDouble("max_discount"));
        tour.setPrice(rs.getInt("price"));
        tour.setHot(rs.getInt("hot"));
        tour.setSum(rs.getInt("sum"));
        return tour;
    }

    /**
     * Create new order and fill fields by rs object
     *
     * @param rs ResultSet object
     * @return Order instance
     * @throws SQLException instance
     */
    private Order getOrder(ResultSet rs) throws SQLException, DBException {
        Order order = new Order();

        User user = getUserByID(rs.getInt("user_id"));
        Tour tour = getTourByID(rs.getInt("tour_id"));
        tour.setPrice(rs.getInt("price"));
        order.setId(rs.getInt("id"));
        order.setCustomer(user);
        order.setTour(tour);
        order.setDate(rs.getDate("date"));
        order.setStatus(rs.getString("status"));
        return order;
    }

    /**
     * get connection with DataBase
     *
     * @return Connection object
     * @throws DBException instance
     */
    /*private Connection getConnection() throws DBException {
        Connection con;
        try {
            InitialContext initContext = new InitialContext();
            Context context = (Context) initContext.lookup("java:comp/env");
            DataSource ds = (DataSource) context.lookup("jdbc/travel_agency");
            con = ds.getConnection();


        } catch (NamingException | SQLException e) {
            LOG.error("cannot obtain connection", e);
            throw new DBException("cannot obtain connection", e);
        }
        return con;
    }*/

    /**
     * close connection, statement and resultSet
     *
     * @param stmt Statement object
     * @param con  Connection object
     * @param rs   ResultSet object
     */
    private void close(Statement stmt, Connection con, ResultSet rs) {
        close(stmt);
        close(con);
        close(rs);
    }

    /**
     * close connection
     *
     * @param con Connection object
     */
    private void close(Connection con) {
        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                LOG.error("Cannot close connection", e);
            }
        }
    }

    /**
     * close statement
     *
     * @param stmt Statement object
     */
    private void close(Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                LOG.error("Cannot close statement", e);
            }
        }
    }

    private void close(PreparedStatement pstmt) {
        if (pstmt != null) {
            try {
                pstmt.close();
            } catch (SQLException e) {
                LOG.error("Cannot close pstatement", e);
            }
        }
    }

    /**
     * close result set
     *
     * @param rs ResultSet object
     */
    private void close(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                LOG.error("cannot close result set", e);
            }
        }
    }

    private void rollback(Connection con) {
        if (con != null) {
            try {
                con.rollback();
            } catch (SQLException e) {
                LOG.error("Cannot rollback transaction", e);
            }
        }
    }

    private void commit(Connection con) {
        if (con != null) {
            try {
                con.commit();
            } catch (SQLException e) {
                LOG.error("Cannot commit transaction", e);
            }
        }
    }
}
