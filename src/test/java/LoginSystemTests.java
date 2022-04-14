import command.*;
import controller.Controller;
import logging.Logger;
import model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.Test;

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

    private static void loginConsumer1(Controller controller) {
        controller.runCommand(new LoginCommand("jbiggson1@hotmail.co.uk", "jbiggson2"));
    }

    private static void loginConsumer2(Controller controller) {
        controller.runCommand(new LoginCommand("jane@inf.ed.ac.uk", "giantsRverycool"));
    }

    private static void loginConsumer3(Controller controller) {
        controller.runCommand(new LoginCommand("i-will-kick-your@gmail.com", "it is wednesday my dudes"));
    }

    private static void loginGovernment(Controller controller) {
        controller.runCommand(new LoginCommand("margaret.thatcher@gov.uk", "The Good times  "));
    }

    @Test
    @DisplayName("Consumer Login should work")
    void successfulConsumerLogin() {
        Controller controller = new Controller();

        register3Consumers(controller);

        LoginCommand cmd1 = new LoginCommand("jbiggson1@hotmail.co.uk", "jbiggson2");
        controller.runCommand(cmd1);
        User consumers1 = cmd1.getResult();
        assertNotNull(consumers1);
        assertEquals("jbiggson1@hotmail.co.uk", consumers1.getEmail());
        assertTrue(consumers1.checkPasswordMatch("jbiggson2"));


        LoginCommand cmd2 = new LoginCommand("jane@inf.ed.ac.uk", "giantsRverycool");
        controller.runCommand(cmd2);
        User consumers2 = cmd2.getResult();
        assertNotNull(consumers2);
        assertEquals("jane@inf.ed.ac.uk", consumers2.getEmail());
        assertTrue(consumers2.checkPasswordMatch("giantsRverycool"));
    }

    @Test
    @DisplayName("Government Login should work")
    void successfulGovernmentLogin() {
        Controller controller = new Controller();

        LoginCommand cmd = new LoginCommand("margaret.thatcher@gov.uk", "The Good times  ");
        controller.runCommand(cmd);
        System.out.println(cmd.getResult());
        User government = cmd.getResult();
        assertNotNull(government);
        assertEquals("jane@inf.ed.ac.uk", government.getEmail());
        assertTrue(government.checkPasswordMatch("giantsRverycool"));
    }
}
