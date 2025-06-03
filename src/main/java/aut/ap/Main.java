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

    private static void setUpSessionFactory() {
        sessionFactory = new Configuration()
                .configure("hibernate.cfg.xml")
                .buildSessionFactory();
    }

    private static void closeSessionFactory() {
        sessionFactory.close();
    }

    public static void main(String[] args) {
        Scanner scn = new Scanner(System.in);
        setUpSessionFactory();
        Session session = sessionFactory.openSession();

        System.out.println("Login: L");
        System.out.print("Sing up: S");
        System.out.println();
        String string = scn.nextLine();

        if (string.equals("L")) {
            System.out.println("Email: ");
            String email = scn.nextLine();
            System.out.println("Password: ");
            String password = scn.nextLine();

            try {
                Transaction tx = session.beginTransaction();
                String hql = "FROM User WHERE email = :email";
                Query<User> query = session.createQuery(hql, User.class);
                query.setParameter("email", email);
                User account = query.uniqueResult();

                if (account == null) {
                    System.out.println("Invalid email");
                    return;
                }
                else if (!account.getPassword().equals(password)) {
                    System.out.println("Password does not match");
                    return;
                }
                System.out.println("Welcome, " + account.getFirst_name() + " " + account.getLast_name() + "!");

                tx.commit();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        else if (string.equals("S")) {
            System.out.println("First Name: ");
            String firstName = scn.nextLine();
            System.out.println("Last Name: ");
            String lastName = scn.nextLine();
            System.out.println("Age: ");
            Integer age = Integer.parseInt(scn.nextLine());
            System.out.println("Email: ");
            String email = scn.nextLine();
            System.out.println("Password: ");
            String password = scn.nextLine();

            try {
                Transaction tx = session.beginTransaction();
                User account = new User(firstName, lastName, age, email, password);
                session.persist(account);

                tx.commit();
                System.out.println("Welcome, " + account.getFirst_name() + " " + account.getLast_name() + "!");
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            } catch (Exception e) {
                System.out.println("An account with this email already exists");
            }
        }
        else {
            System.out.println("Invalid choice");
        }
        session.close();
        closeSessionFactory();
    }
}