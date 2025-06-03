package aut.ap;

import aut.ap.account.User;
import org.hibernate.query.Query;
import java.util.Scanner;
import org.hibernate.Session;
import org.hibernate.cfg.Configuration;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class Main {
    private static SessionFactory sessionFactory;

    private static void initializeSessionFactory() {
        sessionFactory = new Configuration()
                .configure("hibernate.cfg.xml")
                .buildSessionFactory();
    }

    private static void shutdownSessionFactory() {
        if (sessionFactory != null) sessionFactory.close();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        initializeSessionFactory();
        Session session = sessionFactory.openSession();

        System.out.println("Enter L to log in");
        System.out.print("Enter S to sign up\n");
        String choice = scanner.nextLine();

        if (choice.equalsIgnoreCase("L")) {
            System.out.print("Enter your email: ");
            String emailInput = scanner.nextLine();
            System.out.print("Enter your password: ");
            String passwordInput = scanner.nextLine();

            try {
                Transaction transaction = session.beginTransaction();

                Query<User> query = session.createQuery("FROM User WHERE email = :emailParam", User.class);
                query.setParameter("emailParam", emailInput);
                User user = query.uniqueResult();

                if (user == null) {
                    System.out.println("Email not found.");
                } else if (!user.getPassword().equals(passwordInput)) {
                    System.out.println("Incorrect password.");
                } else {
                    System.out.println("Hello, " + user.getFirst_name() + " " + user.getLast_name() + "!");
                }

                transaction.commit();
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        else if (choice.equalsIgnoreCase("S")) {
            System.out.print("First name: ");
            String firstName = scanner.nextLine();
            System.out.print("Last name: ");
            String lastName = scanner.nextLine();
            System.out.print("Age: ");
            int age = Integer.parseInt(scanner.nextLine());
            System.out.print("Email: ");
            String newEmail = scanner.nextLine();
            System.out.print("Password: ");
            String newPassword = scanner.nextLine();

            try {
                Transaction transaction = session.beginTransaction();
                User newUser = new User(firstName, lastName, age, newEmail, newPassword);
                session.persist(newUser);
                transaction.commit();

                System.out.println("Account created for " + newUser.getFirst_name() + " " + newUser.getLast_name() + "!");
            } catch (IllegalArgumentException e) {
                System.out.println("Validation error: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("An account with this email already exists.");
            }
        }

        else {
            System.out.println("Unknown option.");
        }

        session.close();
        shutdownSessionFactory();
    }
}
