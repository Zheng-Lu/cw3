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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


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
        System.out.println(cmd1.getResult());
        List<Consumer> consumers1 = cmd1.getResult();
        assertTrue(consumers1.stream().anyMatch(consumer -> consumer.getName().equals("John Biggson")));

        LoginCommand cmd2 = new LoginCommand("jane@inf.ed.ac.uk", "giantsRverycool");
        controller.runCommand(cmd2);
        System.out.println(cmd2.getResult());
        List<Consumer> consumers2 = cmd2.getResult();
        assertTrue(consumers2.stream().anyMatch(consumer -> consumer.getName().equals("Jane Giantsdottir")));
    }

    @Test
    @DisplayName("Government Login should work")
    void successfulGovernmentLogin() {
        Controller controller = new Controller();

        LoginCommand cmd = new LoginCommand("margaret.thatcher@gov.uk", "The Good times  ");
        controller.runCommand(cmd);
        System.out.println(cmd.getResult());
    }
}
