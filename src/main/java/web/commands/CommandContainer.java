package web.commands;

import org.apache.log4j.Logger;
import web.commands.administration.*;
import web.commands.administration.admin.*;
import web.commands.client.*;

import java.util.Map;
import java.util.TreeMap;

/**
 * class container that contains commands required by the controller
 *
 * @author Volodin Serhii
 */

public class CommandContainer {
    private static final Logger LOG = Logger.getLogger(CommandContainer.class);
    private static Map<String, Command> commands = new TreeMap<>();

    private CommandContainer() {
    }

    static {

        //client commands
        commands.put("find_tour", new FindTourCommand());
        commands.put("buy", new BuyCommand());
        commands.put("personal_settings", new PersonalSettingsCommand());
        commands.put("replenishment", new ReplenishmentCommand());
        commands.put("thanks", new ThanksCommand());
        commands.put("security_settings", new SecuritySettingsRedirectCommand());
        commands.put("change_email", new ChangeEmailCommand());
        commands.put("change_password", new ChangePasswordCommand());
        commands.put("send_ticket", new SendTicketCommand());
        commands.put("download_ticket", new DownloadTicketCommand());
        commands.put("subscribe", new SubscribeNewsletterCommand());
        commands.put("about_us", new AboutUsRedirectCommand());
        commands.put("feedback", new FeedbackCommand());


        //common commands for moder and administration
        commands.put("confirm_order", new ConfirmOrderCommand());
        commands.put("cancel_order", new CancelOrderCommand());
        commands.put("set_hot", new SetHotTourCommand());
        commands.put("set_normal", new SetNormalTourCommand());
        commands.put("set_max_discount", new SetMaxDiscountCommand());
        commands.put("set_discount_step", new SetDiscountStepCommand());

        //administration commands
        commands.put("block_user", new BlockUserCommand());
        commands.put("unblock_user", new UnblockUserCommand());
        commands.put("delete_tour", new DeleteTourCommand());
        commands.put("prepare_change_page", new PrepareChangeCommand());
        commands.put("change_tour", new ChangeTourInfoCommand());
        commands.put("add_tour", new AddTourCommand());
        commands.put("confirm_replenishment", new ConfirmReplenishmentCommand());
        commands.put("cancel_replenishment", new CancelReplenishmentCommand());
        commands.put("statistics", new RedirectStatisticsPageCommand());
        commands.put("send_email", new SendEmailCommand());


        //common commands for all roles
        commands.put("prepare-main-page", new PrepareMainPageCommand());
        commands.put("logout", new LogoutCommand());
        commands.put("go_home", new GoHomeCommand());

        //out of control
        commands.put("login", new LoginCommand());
        commands.put("registration", new RegistrationCommand());
        commands.put("change_locale", new ChangeLocaleCommand());
        commands.put("registration_page", new RegistrationRedirectCommand());
        commands.put("password_recovery_page", new PasswordRecoveryRedirectCommand());
        commands.put("generate_security_key", new GenerateKeyCommand());
        commands.put("check_key", new CheckKeyCommand());

        LOG.debug("Command container was successfully initialized");
        LOG.trace("Number of commands: " + commands.size());


    }

    public static Command getCommand(String command) {
        return commands.get(command);
    }
}
