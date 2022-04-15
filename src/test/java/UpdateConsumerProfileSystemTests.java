import command.*;
import controller.Context;
import controller.Controller;
import logging.Logger;
import model.*;
import org.junit.jupiter.api.*;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UpdateConsumerProfileSystemTests {
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

    @Test
    @DisplayName("Updating Consumer Profile should work")
    void testUpdateConsumerProfile() {
        Controller controller = new Controller();
        Context context = new Context();
        ConsumerPreferences consumerPreferences = new ConsumerPreferences();

        register3Consumers(controller);
        loginConsumer1(controller);
        UpdateConsumerProfileCommand cmd = new UpdateConsumerProfileCommand(
                "jbiggson2",
                "Shikai Geng",
                "Shikai_Geng@163.com",
                "07751234567",
                "Shikai_love_Varian",
                "Shikai_Geng@163.com",
                consumerPreferences
        );
        controller.runCommand(cmd);
        Boolean isUpdateSuccessful = cmd.getResult();
        assertTrue(isUpdateSuccessful,"Updating Profile is failed");
        controller.runCommand(new LogoutCommand());

        LoginCommand login_cmd = new LoginCommand(
                "Shikai_Geng@163.com",
                "Shikai_love_Varian"
        );
        controller.runCommand(login_cmd);
        User consumer1 = login_cmd.getResult();
        assertNotNull(consumer1,"User not found");
        assertEquals("Shikai_Geng@163.com", consumer1.getEmail(),"Incorrect Email");
        assertTrue(consumer1.checkPasswordMatch("Shikai_love_Varian"),"incorrect password");
    }
}
