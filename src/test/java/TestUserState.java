import command.*;
import controller.Controller;
import logging.Logger;
import model.*;
import org.junit.jupiter.api.*;
import state.UserState;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class TestUserState {
    Consumer consumer1;
    Consumer consumer2;
    Consumer consumer3;
    EntertainmentProvider entertainmentProvider1;
    EntertainmentProvider entertainmentProvider2;
    EntertainmentProvider entertainmentProvider3;

    @BeforeEach
    void printTestName(TestInfo testInfo) {
        System.out.println(testInfo.getDisplayName());
    }

    @BeforeEach
    void setup() throws Exception {
        consumer1 = new Consumer(
                "Shikai Geng",
                "Shikai_Geng@163.com",
                "07751234567",
                "Shikai_love_Varian",
                "Shikai_Geng@163.com"
        );

        consumer2 = new Consumer(
                "Rays Zhang",
                "Rays_Zhang@gmail.com",
                "07757654321",
                "Shuyuan&Zhang2002",
                "Rays_Zhang@qq.com"
        );

        consumer3 = new Consumer(
                "Lawrence Zhu",
                "LawrenceZYZ@outlook.com",
                "07759876543",
                "ZYZ1234567890",
                "LawrenceZYZ@gmail.com"
        );

        entertainmentProvider1 = new EntertainmentProvider(
                "No org",
                "Leith Walk",
                "a hat on the ground",
                "the best musicican ever",
                "busk@every.day",
                "When they say 'you can't do this': Ding Dong! You are wrong!",
                Collections.emptyList(),
                Collections.emptyList()
        );

        entertainmentProvider2 = new EntertainmentProvider(
                "Cinema Conglomerate",
                "Global Office, International Space Station",
                "$$$@there'sNoEmailValidation.wahey!",
                "Mrs Representative",
                "odeon@cineworld.com",
                "F!ghT th3 R@Pture",
                List.of("Dr Strangelove"),
                List.of("we_dont_get_involved@cineworld.com")
        );

        entertainmentProvider3 = new EntertainmentProvider(
                "Olympics Committee",
                "Mt. Everest",
                "noreply@gmail.com",
                "Secret Identity",
                "anonymous@gmail.com",
                "anonymous",
                List.of("Unknown Actor", "Spy"),
                List.of("unknown@gmail.com", "spy@gmail.com")
        );
    }


    @AfterEach
    void clearLogs() {
        Logger.getInstance().clearLog();
        System.out.println("---");
    }


    @Test
    void test_addUser_and_getAllUsers() {
        UserState userState = new UserState();

        assertEquals(1, userState.getAllUsers().size());

        assertEquals("Shikai Geng", consumer1.getName());
        assertEquals("Rays Zhang", consumer2.getName());
        assertEquals("Lawrence Zhu", consumer3.getName());

        userState.addUser(consumer1);
        userState.addUser(consumer2);
        userState.addUser(consumer3);
        assertEquals(4, userState.getAllUsers().size());
    }


    @Test
    void test_setCurrentUser_and_getCurrentUser() {
        UserState userState = new UserState();

        assertNull(userState.getCurrentUser());

        userState.setCurrentUser(consumer1);
        assertNotNull(userState.getCurrentUser());
        assertEquals("Shikai_Geng@163.com", userState.getCurrentUser().getEmail());
    }
}
