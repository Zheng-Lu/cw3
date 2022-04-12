import command.*;
import controller.Controller;
import logging.Logger;
import model.*;
import org.junit.jupiter.api.*;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LogoutSystemTests {
    @BeforeEach
    void printTestName(TestInfo testInfo) {
        System.out.println(testInfo.getDisplayName());
    }

    @AfterEach
    void clearLogs() {
        Logger.getInstance().clearLog();
        System.out.println("---");
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
    @DisplayName("Simple Consumer Logout should work")
    void successfulConsumerLogout() {
        Controller controller = new Controller();

        loginConsumer1(controller);
        LogoutCommand cmd1 = new LogoutCommand();
        controller.runCommand(cmd1);
//        List<Consumer> consumers1 = cmd1.getResult();
//        assertEquals(0, consumers1.size());


        loginConsumer2(controller);
        LogoutCommand cmd2 = new LogoutCommand();
        controller.runCommand(cmd2);
//        List<Consumer> consumers2 = cmd2.getResult();
//        assertEquals(0, consumers2.size());
    }

    @Test
    @DisplayName("Simple Consumer Logout should work")
    void successfulGovernmentLogout() {
        Controller controller = new Controller();

        loginGovernment(controller);
        LogoutCommand cmd = new LogoutCommand();
        controller.runCommand(cmd);
    }
}
