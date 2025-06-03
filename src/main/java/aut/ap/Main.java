package app.entry;

import app.models.User;
import org.hibernate.query.Query;
import java.util.Scanner;
import org.hibernate.Session;
import org.hibernate.cfg.Configuration;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class Main {
    private static SessionFactory factory;

    private static void initializeFactory() {
        factory = new Configuration()
                .configure("hibernate.cfg.xml")
                .buildSessionFactory();
    }

    private static void shutdownFactory() {
        if (factory != null) factory.close();
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        initializeFactory();
        Session dbSession = factory.openSession();

        System.out.println("To log in, enter: L");
        System.out.print("To register, enter: S\n");

        String command = input.nextLine();

        if (command.equalsIgnoreCase("L")) {
            System.out.print("Email: ");
            String enteredEmail = input.nextLine();
            System.out.print("Password: ");
            String enteredPassword = input.nextLine();

            try {
                Transaction tx = dbSession.beginTransaction();
                Query<User> userQuery = dbSession
                        .createQuery("FROM User WHERE email = :mail", User.class);
                userQuery.setParameter("mail", enteredEmail);
                User foundUser = userQuery.uniqueResult();

                if (foundUser == null) {
                    System.out.println("No user found with this email.");
                } else if (!foundUser.getPassword().equals(enteredPassword)) {
                    System.out.println("Incorrect password.");
                } else {
                    System.out.println("Hello, " + foundUser.getFirst_name() + " " + foundUser.getLast_name() + "!");
                }

                tx.commit();
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        else if (command.equalsIgnoreCase("S")) {
            System.out.print("First Name: ");
            String fname = input.nextLine();
            System.out.print("Last Name: ");
            String lname = input.nextLine();
            System.out.print("Age: ");
            int age = Integer.parseInt(input.nextLine());
            System.out.print("Email: ");
            String mail = input.nextLine();
            System.out.print("Password: ");
            String pass = input.nextLine();

            try {
                Transaction tx = dbSession.beginTransaction();
                User newUser = new User(fname, lname, age, mail, pass);
                dbSession.persist(newUser);
                tx.commit();
                System.out.println("Account created for " + fname + " " + lname + "!");
            } catch (IllegalArgumentException ex) {
                System.out.println("Validation failed: " + ex.getMessage());
            } catch (Exception ex) {
                System.out.println("This email is already registered.");
            }
        }

        else {
            System.out.println("Unrecognized input.");
        }

        dbSession.close();
        shutdownFactory();
    }
}
