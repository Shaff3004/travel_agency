package email;

import constants.Emails;
import db.entity.Account;
import db.entity.Order;
import db.entity.User;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import javax.sql.DataSource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

public class MailAgent {
    private static MailAgent instance;

    private MailAgent() {
    }

    public static synchronized MailAgent getInstance() {
        if (instance == null) {
            instance = new MailAgent();
        }
        return instance;
    }

    public static void sendEmail(Emails type, Object... objects) throws IOException, MessagingException {
        final Properties properties = new Properties();
        properties.load(MailAgent.class.getClassLoader().getResourceAsStream("mail.properties"));

        Session mailSession = Session.getDefaultInstance(properties);
        MimeMessage message = new MimeMessage(mailSession);
        message.setFrom(new InternetAddress("besttourcompany2018@gmail.com"));

        User user = null;
        Order order = null;
        Account account = null;
        String email = null;
        String text = null;
        if (objects[0] instanceof Order) {
            order = (Order) objects[0];
            user = (User) objects[1];
        } else if (objects[0] instanceof Account) {
            account = (Account) objects[0];
            user = (User) objects[1];
        } else if (objects[0] instanceof User && objects.length == 1) {
            user = (User) objects[0];
        } else if (objects[0] instanceof String && objects.length == 2) {
            email = (String) objects[0];
            text = (String) objects[1];
        } else if (objects[0] instanceof String && objects.length == 1) {
            email = (String) objects[0];
        }

        switch (type) {
            case ORDER_REGISTERED:
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(user.getEmail()));
                message.setSubject("Order registration");
                message.setText(orderRegisteredMessage(order, user));
                break;
            case ORDER_CONFIRMED:
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(user.getEmail()));
                message.setSubject("Order confirmed");
                message.setText(orderConfirmedMessage(order, user));
                break;
            case ORDER_CANCELED:
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(user.getEmail()));
                message.setSubject("Order canceled");
                message.setText(orderCanceledMessage(order, user));
                break;
            case REPLENISHMENT_REGISTERED:
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(user.getEmail()));
                message.setSubject("Replenishment registered");
                message.setText(replenishmentRegisteredMessage(account, user));
                break;
            case REPLENISHMENT_CONFIRMED:
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(user.getEmail()));
                message.setSubject("Replenishment confirmed");
                message.setText(replenishmentConfirmedMessage(account, user));
                break;
            case REPLENISHMENT_CANCELED:
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(user.getEmail()));
                message.setSubject("Replenishment canceled");
                message.setText(replenishmentCanceledMessage(account, user));
                break;
            case RECOVERY:
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
                message.setSubject("Password recovery");
                message.setText("Your text for login: " + text);
                break;
            case NEW_PASSWORD:
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
                message.setSubject("New password");
                message.setText("Your new password: " + text);
                break;
            case TICKET:
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
                message.setSubject("Tickets");
                BodyPart messageText = new MimeBodyPart();
                messageText.setText("Hello! Here is your tickets");

                MimeBodyPart filePart = new MimeBodyPart();

                String fileName = "D://travel_agency19_01/web/style/pdf/ticket.pdf";
                FileDataSource dataSource = new FileDataSource(fileName);
                filePart.setDataHandler(new DataHandler(dataSource));
                filePart.setFileName(fileName);

                Multipart multipart = new MimeMultipart();
                multipart.addBodyPart(messageText);
                multipart.addBodyPart(filePart);

                message.setContent(multipart);
                break;
            case REGISTRATION:
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(user.getEmail()));
                message.setSubject("Registration");
                message.setText(registrationMessage(user));
                break;
            case MESSAGE:
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
                message.setSubject("Best tour company");
                message.setText(text);
                break;
            case FEEDBACK:
                message.addRecipient(Message.RecipientType.TO, new InternetAddress("besttourcompany2018@gmail.com"));
                message.setSubject("Feedback");
                message.setText(email + ", " + text);
        }

        Transport tr = mailSession.getTransport();
        tr.connect(null, "thebestpassword");
        tr.sendMessage(message, message.getAllRecipients());
        tr.close();
    }


    /*public static void sendOrderEmail(Emails type, Order order, User user) throws IOException, MessagingException {
        final Properties properties = new Properties();
        properties.load(MailAgent.class.getClassLoader().getResourceAsStream("mail.properties"));

        Session mailSession = Session.getDefaultInstance(properties);
        MimeMessage message = new MimeMessage(mailSession);
        message.setFrom(new InternetAddress("besttourcompany2018@gmail.com"));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(user.getEmail()));
        switch (type) {
            case ORDER_REGISTERED:
                message.setSubject("Order registration");
                message.setText(orderRegisteredMessage(order, user));
                break;
            case ORDER_CONFIRMED:
                message.setSubject("Order confirmed");
                message.setText(orderConfirmedMessage(order, user));
                break;
            case ORDER_CANCELED:
                message.setSubject("Order canceled");
                message.setText(orderCanceledMessage(order, user));
                break;
        }


        Transport tr = mailSession.getTransport();
        tr.connect(null, "thebestpassword");
        tr.sendMessage(message, message.getAllRecipients());
        tr.close();
    }

    public static void sendReplenishmentEmail(Emails type, Account account, User user) throws IOException, MessagingException {
        final Properties properties = new Properties();
        properties.load(MailAgent.class.getClassLoader().getResourceAsStream("mail.properties"));

        Session mailSession = Session.getDefaultInstance(properties);
        MimeMessage message = new MimeMessage(mailSession);
        message.setFrom(new InternetAddress("besttourcompany2018@gmail.com"));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(user.getEmail()));
        switch (type) {
            case REPLENISHMENT_REGISTERED:
                message.setSubject("Replenishment registered");
                message.setText(replenishmentRegisteredMessage(account, user));
                break;
            case REPLENISHMENT_CONFIRMED:
                message.setSubject("Replenishment confirmed");
                message.setText(replenishmentConfirmedMessage(account, user));
                break;
            case REPLENISHMENT_CANCELED:
                message.setSubject("Replenishment canceled");
                message.setText(replenishmentCanceledMessage(account, user));
                break;

        }


        Transport tr = mailSession.getTransport();
        tr.connect(null, "thebestpassword");
        tr.sendMessage(message, message.getAllRecipients());
        tr.close();
    }


    public static void sendRegistrationEmail(Emails type, User user) throws IOException, MessagingException {
        final Properties properties = new Properties();
        properties.load(MailAgent.class.getClassLoader().getResourceAsStream("mail.properties"));

        Session mailSession = Session.getDefaultInstance(properties);
        MimeMessage message = new MimeMessage(mailSession);
        message.setFrom(new InternetAddress("besttourcompany2018@gmail.com"));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(user.getEmail()));

        message.setSubject("Registration");
        message.setText(registrationMessage(user));

        Transport tr = mailSession.getTransport();
        tr.connect(null, "thebestpassword");
        tr.sendMessage(message, message.getAllRecipients());
        tr.close();
    }


    public static void sendRecoveryEmail(Emails type, String email, String key) throws IOException, MessagingException {
        final Properties properties = new Properties();
        properties.load(MailAgent.class.getClassLoader().getResourceAsStream("mail.properties"));

        Session mailSession = Session.getDefaultInstance(properties);
        MimeMessage message = new MimeMessage(mailSession);
        message.setFrom(new InternetAddress("besttourcompany2018@gmail.com"));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
        switch (type) {
            case RECOVERY:
                message.setSubject("Password recovery");
                message.setText("Your key for login: " + key);
                break;
            case NEW_PASSWORD:
                message.setSubject("New password");
                message.setText("Your new password: " + key);
        }


        Transport tr = mailSession.getTransport();
        tr.connect(null, "thebestpassword");
        tr.sendMessage(message, message.getAllRecipients());
        tr.close();
    }

    public static void sendTicket(Emails type, String email) throws IOException, MessagingException {
        final Properties properties = new Properties();
        properties.load(MailAgent.class.getClassLoader().getResourceAsStream("mail.properties"));

        Session mailSession = Session.getDefaultInstance(properties);
        MimeMessage message = new MimeMessage(mailSession);
        message.setFrom(new InternetAddress("besttourcompany2018@gmail.com"));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
        message.setSubject("Tickets");
        BodyPart messageText = new MimeBodyPart();
        messageText.setText("Hello! Here is your tickets");

        MimeBodyPart filePart = new MimeBodyPart();

        String fileName = "D://travel_agency19_01/web/style/pdf/ticket.pdf";
        FileDataSource dataSource = new FileDataSource(fileName);
        filePart.setDataHandler(new DataHandler(dataSource));
        filePart.setFileName(fileName);

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageText);
        multipart.addBodyPart(filePart);

        message.setContent(multipart);

        Transport tr = mailSession.getTransport();
        tr.connect(null, "thebestpassword");
        tr.sendMessage(message, message.getAllRecipients());
        tr.close();
    }*/

    private static String replenishmentRegisteredMessage(Account account, User user) {
        return "Уважаемый(ая) " + user.getFullName() + ", от Вас был получен запрос на пополнение лицевого счета " +
                "(№" + account.getId() + ")" + " в размере " + account.getPrice() + ". В ближайшее время Ваш запрос будет рассмотрен.";
    }

    private static String replenishmentConfirmedMessage(Account account, User user) {
        return "Уважаемый(ая) " + user.getFullName() + ", Ваш запрос на пополнение лицевого счета" + "(№" + account.getId() + ")" +
                " в размере " + account.getPrice() + " был одобрен. Проверьте Ваш лицевой счет.";
    }

    private static String replenishmentCanceledMessage(Account account, User user) {
        return "Уважаемый(ая) " + user.getFullName() + ", к сожалению, Ваш запрос на пополнение лицевого счета(№" + account.getId() + ")"
                + " в размере " + account.getPrice() + " был отклонён. Для получения дополнительной информации, пожалуйста, свяжитесь " +
                "с нами.";

    }

    private static String orderRegisteredMessage(Order order, User user) {
        return "Уважаемый(ая) " + user.getFullName() + ", Ваш заказ(№" + order.getId() + ") был " +
                "успешно зарегистрирован. С вашего лицевого счета списано " + order.getTour().getPrice() +
                ". Для подтверждения оплаты с Вами в ближайшее время свяжется наш менеджер.\n" +
                "Информация о заказе:\n" +
                "Заказчик: " + order.getCustomer().getFullName() + "\n" +
                "Пункт назначения: " + order.getTour().getCountry() + ", " + order.getTour().getCity();
    }

    private static String orderConfirmedMessage(Order order, User user) {
        return "Уважаемый(ая) " + user.getFullName() + ", Ваш заказ(№" + order.getId() + ") был " +
                "подтверждён, оплата произведена. Пожалуйста, свяжитесь с нами для уточнения Ваших паспортных данных " +
                "и подготовки необходимого пакета документов.";
    }

    private static String orderCanceledMessage(Order order, User user) {
        return "Уважаемый(ая) " + user.getFullName() + ", к сожалению, Ваш заказ(№" + order.getId() + ") был " +
                "отменён. Затраченные деньги вернулись на Ваш лицевой счет. Для получения дополнительной информации," +
                " пожалуйста, свяжитесь с нами.";
    }

    private static String registrationMessage(User user) {
        return "Уважаемый(ая) " + user.getFullName() + ", вы были успешно зарегистрированы на сайте компании Best Tour." +
                " Наша компания желает Вам хорошего дня и напоминает, что в любой момент Вы можете связаться с нашей командой" +
                " поддерки для получения ответа на вопрос, который Вас интересует. Добро пожаловать!\n";/* +
                "Ваши данные:\n" +
                "login: " + user.getLogin() + "\n" +
                "password: " + user.getPassword();*/
    }

}
