import command.*;
import controller.Controller;
import logging.LogEntry;
import logging.Logger;
import model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.Test;
import state.UserState;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class LoginSystemTests {
    @BeforeEach
    void printTestName(TestInfo testInfo) {
        System.out.println(testInfo.getDisplayName());
    }

    @AfterEach
    void clearLogs() {
        Logger.getInstance().clearLog();
        System.out.println("---");
    }

    private static void register3Consumers(Controller controller) {
        controller.runCommand(new RegisterConsumerCommand(
                "John Biggson",
                "jbiggson1@hotmail.co.uk",
                "077893153480",
                "jbiggson2",
                "jbiggson1@hotmail.co.uk"
        ));
        controller.runCommand(new LogoutCommand());
        controller.runCommand(new RegisterConsumerCommand(
                "Jane Giantsdottir",
                "jane@inf.ed.ac.uk",
                "04462187232",
                "giantsRverycool",
                "jane@aol.com"
        ));
        controller.runCommand(new LogoutCommand());
        controller.runCommand(new RegisterConsumerCommand(
                "Wednesday Kebede",
                "i-will-kick-your@gmail.com",
                "-",
                "it is wednesday my dudes",
                "i-will-kick-your@gmail.com"
        ));
        controller.runCommand(new LogoutCommand());
    }

    private static void register3EntertainmentProviders(Controller controller) {
        controller.runCommand(new RegisterEntertainmentProviderCommand(
                "Cinema Conglomerate",
                "Global Office, International Space Station",
                "$$$@there'sNoEmailValidation.wahey!",
                "Mrs Representative",
                "odeon@cineworld.com",
                "F!ghT th3 R@Pture",
                List.of("Dr Strangelove"),
                List.of("we_dont_get_involved@cineworld.com")
        ));
        controller.runCommand(new LogoutCommand());
        controller.runCommand(new RegisterEntertainmentProviderCommand(
                "Olympics Committee",
                "Mt. Everest",
                "noreply@gmail.com",
                "Secret Identity",
                "anonymous@gmail.com",
                "anonymous",
                List.of("Unknown Actor", "Spy"),
                List.of("unknown@gmail.com", "spy@gmail.com")
        ));
        controller.runCommand(new LogoutCommand());
        controller.runCommand(new RegisterEntertainmentProviderCommand(
                "No org",
                "Leith Walk",
                "a hat on the ground",
                "the best musicican ever",
                "busk@every.day",
                "When they say 'you can't do this': Ding Dong! You are wrong!",
                Collections.emptyList(),
                Collections.emptyList()
        ));
        controller.runCommand(new LogoutCommand());
    }

    String getLog() {
        List<LogEntry> entries = Logger.getInstance().getLog();
        String logStatus = entries.get(entries.size()-1).getAdditionalInfo().toString();
        System.out.println(logStatus);
        return logStatus;
    }


    @Test
    @DisplayName("Consumer Login should work")
    void successfulConsumerLogin() {
        Controller controller = new Controller();

        register3Consumers(controller);

        // Login case 1
        LoginCommand login_cmd1 = new LoginCommand("jbiggson1@hotmail.co.uk", "jbiggson2");
        controller.runCommand(login_cmd1);
        String loginStatus1 = getLog();
        assertEquals("{STATUS:=USER_LOGIN_SUCCESS}",loginStatus1);
        User consumers1 = login_cmd1.getResult();
        assertNotNull(consumers1);
        assertEquals("jbiggson1@hotmail.co.uk", consumers1.getEmail());
        assertTrue(consumers1.checkPasswordMatch("jbiggson2"));

        LogoutCommand logout_cmd1 = new LogoutCommand();
        controller.runCommand(logout_cmd1);
        String logoutStatus1 = getLog();
        assertEquals("{STATUS:=USER_LOGOUT_SUCCESS}",logoutStatus1);
        assertNull(logout_cmd1.getResult());

        // Login case 2
        LoginCommand login_cmd2 = new LoginCommand("jane@inf.ed.ac.uk", "giantsRverycool");
        controller.runCommand(login_cmd2);
        String loginStatus2 = getLog();
        assertEquals("{STATUS:=USER_LOGIN_SUCCESS}",loginStatus2);
        User consumers2 = login_cmd2.getResult();
        assertNotNull(consumers2);
        assertEquals("jane@inf.ed.ac.uk", consumers2.getEmail());
        assertTrue(consumers2.checkPasswordMatch("giantsRverycool"));

        LogoutCommand logout_cmd2 = new LogoutCommand();
        controller.runCommand(logout_cmd2);
        String logoutStatus2 = getLog();
        assertEquals("{STATUS:=USER_LOGOUT_SUCCESS}",logoutStatus2);
        assertNull(logout_cmd2.getResult());

        // Login case 3
        LoginCommand login_cmd3 = new LoginCommand("i-will-kick-your@gmail.com", "it is wednesday my dudes");
        controller.runCommand(login_cmd3);
        String loginStatus3 = getLog();
        assertEquals("{STATUS:=USER_LOGIN_SUCCESS}",loginStatus3);
        User consumers3 = login_cmd3.getResult();
        assertNotNull(consumers3);
        assertEquals("i-will-kick-your@gmail.com", consumers3.getEmail());
        assertTrue(consumers3.checkPasswordMatch("it is wednesday my dudes"));

        LogoutCommand logout_cmd3 = new LogoutCommand();
        controller.runCommand(logout_cmd3);
        String logoutStatus3 = getLog();
        assertEquals("{STATUS:=USER_LOGOUT_SUCCESS}",logoutStatus3);
        assertNull(logout_cmd3.getResult());
    }


    @Test
    @DisplayName("Entertainment Provider Login should work")
    void successfulEntertainmentProviderLogin() {
        Controller controller = new Controller();

        register3EntertainmentProviders(controller);

        LoginCommand login_cmd1 = new LoginCommand("busk@every.day", "When they say 'you can't do this': Ding Dong! You are wrong!");
        controller.runCommand(login_cmd1);
        String loginStatus1 = getLog();
        assertEquals("{STATUS:=USER_LOGIN_SUCCESS}",loginStatus1);
        User entertainmentProvider1 = login_cmd1.getResult();
        assertNotNull(entertainmentProvider1);
        assertEquals("busk@every.day", entertainmentProvider1.getEmail());
        assertTrue(entertainmentProvider1.checkPasswordMatch("When they say 'you can't do this': Ding Dong! You are wrong!"));
        assertEquals("a hat on the ground", entertainmentProvider1.getPaymentAccountEmail());
        LogoutCommand logout_cmd1 = new LogoutCommand();
        controller.runCommand(logout_cmd1);
        String logoutStatus1 = getLog();
        assertEquals("{STATUS:=USER_LOGOUT_SUCCESS}",logoutStatus1);
        assertNull(logout_cmd1.getResult());

        LoginCommand login_cmd2 = new LoginCommand("odeon@cineworld.com", "F!ghT th3 R@Pture");
        controller.runCommand(login_cmd2);
        String loginStatus2 = getLog();
        assertEquals("{STATUS:=USER_LOGIN_SUCCESS}",loginStatus2);
        User entertainmentProvider2 = login_cmd2.getResult();
        assertNotNull(entertainmentProvider2);
        assertEquals("odeon@cineworld.com", entertainmentProvider2.getEmail());
        assertTrue(entertainmentProvider2.checkPasswordMatch("F!ghT th3 R@Pture"));
        assertEquals("$$$@there'sNoEmailValidation.wahey!", entertainmentProvider2.getPaymentAccountEmail());
        LogoutCommand logout_cmd2 = new LogoutCommand();
        controller.runCommand(logout_cmd2);
        String logoutStatus2 = getLog();
        assertEquals("{STATUS:=USER_LOGOUT_SUCCESS}",logoutStatus2);
        assertNull(logout_cmd2.getResult());

        LoginCommand login_cmd3 = new LoginCommand("anonymous@gmail.com", "anonymous");
        controller.runCommand(login_cmd3);
        String loginStatus3 = getLog();
        assertEquals("{STATUS:=USER_LOGIN_SUCCESS}",loginStatus3);
        User entertainmentProvider3 = login_cmd3.getResult();
        assertNotNull(entertainmentProvider3);
        assertEquals("anonymous@gmail.com", entertainmentProvider3.getEmail());
        assertTrue(entertainmentProvider3.checkPasswordMatch("anonymous"));
        assertEquals("noreply@gmail.com", entertainmentProvider3.getPaymentAccountEmail());
        LogoutCommand logout_cmd3 = new LogoutCommand();
        controller.runCommand(logout_cmd3);
        String logoutStatus3 = getLog();
        assertEquals("{STATUS:=USER_LOGOUT_SUCCESS}",logoutStatus3);
        assertNull(logout_cmd3.getResult());
    }

    @Test
    @DisplayName("Government Login should work")
    void successfulGovernmentLogin() {
        Controller controller = new Controller();

        LoginCommand login_cmd = new LoginCommand("margaret.thatcher@gov.uk", "The Good times  ");
        controller.runCommand(login_cmd);
        String loginStatus = getLog();
        assertEquals("{STATUS:=USER_LOGIN_SUCCESS}",loginStatus);
        User government = login_cmd.getResult();
        assertNotNull(government);
        assertEquals("margaret.thatcher@gov.uk", government.getEmail());
        assertTrue(government.checkPasswordMatch("The Good times  "));

        LogoutCommand logout_cmd = new LogoutCommand();
        controller.runCommand(logout_cmd);
        String logoutStatus = getLog();
        assertEquals("{STATUS:=USER_LOGOUT_SUCCESS}",logoutStatus);
        assertNull(logout_cmd.getResult());
    }
}
