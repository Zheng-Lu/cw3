import command.*;
import controller.Controller;
import logging.LogEntry;
import logging.Logger;
import model.*;
import org.junit.jupiter.api.*;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class RegisterConsumerSystemTests {
    @BeforeEach
    void printTestName(TestInfo testInfo) {
        System.out.println(testInfo.getDisplayName());
    }

    @AfterEach
    void clearLogs() {
        Logger.getInstance().clearLog();
        System.out.println("---");
    }

    String getLog() {
        List<LogEntry> entries = Logger.getInstance().getLog();
        String logStatus = entries.get(entries.size()-1).getAdditionalInfo().toString();
        System.out.println(logStatus);
        return logStatus;
    }

    @Test
    @DisplayName("Consumer Registrations should work")
    void successfulConsumerRegistered(){
        Controller controller = new Controller();

        RegisterConsumerCommand cmd = new RegisterConsumerCommand(
                "John Biggson",
                "jbiggson1@hotmail.co.uk",
                "077893153480",
                "jbiggson2",
                "jbiggson1@hotmail.co.uk"
        );
        controller.runCommand(cmd);
        String logStatus = getLog();
        assertNotNull(logStatus);
        assertEquals("{STATUS:=REGISTER_CONSUMER_SUCCESS}",logStatus);
    }

    @Test
    @DisplayName("Consumer Registrations Then Login should work")
    void successfulConsumerRegisteredThenLogin(){
        Controller controller = new Controller();

        RegisterConsumerCommand cmd = new RegisterConsumerCommand(
                "John Biggson",
                "jbiggson1@hotmail.co.uk",
                "077893153480",
                "jbiggson2",
                "jbiggson1@hotmail.co.uk"
        );
        controller.runCommand(cmd);
        String registerStatus = getLog();
        assertNotNull(registerStatus);
        assertEquals("{STATUS:=REGISTER_CONSUMER_SUCCESS}",registerStatus);

        LoginCommand login_cmd = new LoginCommand("jbiggson1@hotmail.co.uk","jbiggson2");
        controller.runCommand(login_cmd);
        String loginStatus = getLog();
        assertNotNull(loginStatus);
        assertEquals("{STATUS:=USER_LOGIN_SUCCESS}",loginStatus);
    }

    @Test
    @DisplayName("Consumer Registrations With Existed Email should not work")
    void failedConsumerRegisteredExistedEmail(){
        Controller controller = new Controller();

        RegisterConsumerCommand cmd1 = new RegisterConsumerCommand(
                "John Biggson",
                "jbiggson1@hotmail.co.uk",
                "077893153480",
                "jbiggson2",
                "jbiggson1@hotmail.co.uk"
        );
        controller.runCommand(cmd1);
        String registerStatus1 = getLog();
        assertNotNull(registerStatus1);
        assertEquals("{STATUS:=REGISTER_CONSUMER_SUCCESS}",registerStatus1);

        controller.runCommand(new LogoutCommand());

        RegisterConsumerCommand cmd2 = new RegisterConsumerCommand(
                "Alex Bobbing",
                "jbiggson1@hotmail.co.uk",
                "07781234567",
                "d8ircnyq23oir72tybw378",
                "jbiggson1@hotmail.co.uk"
        );
        controller.runCommand(cmd2);
        String registerStatus2 = getLog();
        assertNotNull(registerStatus2);
        assertEquals("{STATUS:=USER_REGISTER_EMAIL_ALREADY_REGISTERED}",registerStatus2);
    }

    @Test
    @DisplayName("Consumer Registrations with Missing Name should not work")
    void failedConsumerRegisteredWithMissingName(){
        Controller controller = new Controller();

        RegisterConsumerCommand cmd = new RegisterConsumerCommand(
                null,
                "jbiggson1@hotmail.co.uk",
                "077893153480",
                "jbiggson2",
                "jbiggson1@hotmail.co.uk"
        );
        controller.runCommand(cmd);
        String logStatus = getLog();
        assertNotNull(logStatus);
        assertEquals("{STATUS:=USER_REGISTER_FIELDS_CANNOT_BE_NULL}",logStatus);
    }

    @Test
    @DisplayName("Consumer Registrations with Missing Email should not work")
    void failedConsumerRegisteredWithMissingEmail(){
        Controller controller = new Controller();

        RegisterConsumerCommand cmd = new RegisterConsumerCommand(
                "Rick Morty",
                null,
                "077893153480",
                "jbiggson2",
                "jbiggson1@hotmail.co.uk"
        );
        controller.runCommand(cmd);
        String logStatus = getLog();
        assertNotNull(logStatus);
        assertEquals("{STATUS:=USER_REGISTER_FIELDS_CANNOT_BE_NULL}",logStatus);
    }

    @Test
    @DisplayName("Consumer Registrations with Missing Phone Number should not work")
    void failedConsumerRegisteredWithMissingPhoneNumber(){
        Controller controller = new Controller();

        RegisterConsumerCommand cmd = new RegisterConsumerCommand(
                "Rick Morty",
                "Rick_Morty@gmail.com",
                null,
                "jbiggson2",
                "jbiggson1@hotmail.co.uk"
        );
        controller.runCommand(cmd);
        String logStatus = getLog();
        assertNotNull(logStatus);
        assertEquals("{STATUS:=USER_REGISTER_FIELDS_CANNOT_BE_NULL}",logStatus);
    }

    @Test
    @DisplayName("Consumer Registrations with Missing Password should not work")
    void failedConsumerRegisteredWithMissingPassword(){
        Controller controller = new Controller();

        RegisterConsumerCommand cmd = new RegisterConsumerCommand(
                "Rick Morty",
                "Rick_Morty@gmail.com",
                "077893153480",
                null,
                "jbiggson1@hotmail.co.uk"
        );
        controller.runCommand(cmd);
        String logStatus = getLog();
        assertNotNull(logStatus);
        assertEquals("{STATUS:=USER_REGISTER_FIELDS_CANNOT_BE_NULL}",logStatus);
    }

    @Test
    @DisplayName("Consumer Registrations with Missing Payment Email should not work")
    void failedConsumerRegisteredWithMissingPaymentEmail(){
        Controller controller = new Controller();

        RegisterConsumerCommand cmd = new RegisterConsumerCommand(
                "Rick Morty",
                "Rick_Morty@gmail.com",
                "077893153480",
                "cwumeryqwo4b879rtbwaieyr",
                null
        );
        controller.runCommand(cmd);
        String logStatus = getLog();
        assertNotNull(logStatus);
        assertEquals("{STATUS:=USER_REGISTER_FIELDS_CANNOT_BE_NULL}",logStatus);
    }

    @Test
    @DisplayName("Consumer Registrations with Missing All Information should not work")
    void failedConsumerRegisteredWithoutInformation(){
        Controller controller = new Controller();

        RegisterConsumerCommand cmd = new RegisterConsumerCommand(
                null,
                null,
                null,
                null,
                null
        );
        controller.runCommand(cmd);
        String logStatus = getLog();
        assertNotNull(logStatus);
        assertEquals("{STATUS:=USER_REGISTER_FIELDS_CANNOT_BE_NULL}",logStatus);
    }
}
