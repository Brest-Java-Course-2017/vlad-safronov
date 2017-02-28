package com.epam.test.client;

import com.epam.test.client.rest.api.UsersConsumer;
import com.epam.test.client.exception.ServerDataAccessException;
import com.epam.test.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Scanner;

/**
 * REST client console application demo.
 */
@Component
public class DemoApp {

    @Autowired
    UsersConsumer usersConsumer;

    Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("spring-context.xml");

        DemoApp demoApp = ctx.getBean(DemoApp.class);
        demoApp.menu();
    }

    private void menu() {

        int swValue = 0;

        System.out.println("=================================");
        System.out.println("|   MENU SELECTION DEMO         |");
        System.out.println("=================================");
        System.out.println("| Options:                      |");
        System.out.println("|        1. Get all users       |");
        System.out.println("|        2. Get user by login   |");
        System.out.println("|        3. Exit                |");
        System.out.println("=================================");
        while (swValue != 3) {
            System.out.print("Select option: ");
            if (sc.hasNextInt()) {
                swValue = sc.nextInt();
                checkValue(swValue);
            } else {
                System.out.println("Bad value: " + sc.next());
            }
        }
    }

    private void checkValue(int item) {
        switch (item) {
            case 1:
                getAllUsers();
                break;
            case 2:
                getUserByLogin();
                break;
            case 3:
                System.out.println("Exit.");
                break;
            default:
                System.out.println("Invalid selection.");
                break;
        }
    }

    private void getAllUsers() {
        List<User> users = usersConsumer.getAllUsers();
        System.out.println("users: " + users);
    }

    private void getUserByLogin() {
        String userLogin = "";
        System.out.print("    Enter user login: ");
        if (sc.hasNextLine()) {
            userLogin = sc.next();
        }

        try {
            User user = usersConsumer.getUserByLogin(userLogin);
            System.out.println("    User: " + user);
        } catch (ServerDataAccessException ex) {
            System.out.println("    ERROR: " + ex.getMessage());
        }
    }
}
