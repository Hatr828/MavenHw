package org.brainacad;

import java.sql.Connection;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.brainacad.db.CoffeeShopRepository;
import org.brainacad.db.Database;
import org.brainacad.service.Task1;
import org.brainacad.service.Task2;
import org.brainacad.service.Task3;
import org.brainacad.service.Task4;
import org.brainacad.service.Task5;
import org.brainacad.service.Task6;
import org.brainacad.service.Task7;
import org.brainacad.service.Task8;
import org.brainacad.service.Task9;
import org.brainacad.service.Task10;

public class App {

    public static void main(String[] args) {
        Database db = Database.fromEnv();
        System.out.printf("Connecting to %s...%n", db.jdbcUrl());
        try (Connection connection = db.openConnection()) {
            connection.setAutoCommit(false);
            CoffeeShopRepository repo = new CoffeeShopRepository(connection);

            LocalDate today = LocalDate.now();
            LocalDateTime weekAgo = today.minusDays(7).atStartOfDay();
            LocalDateTime tomorrow = today.plusDays(1).atStartOfDay();
            String baristaPhone = System.getenv().getOrDefault("BARISTA_PHONE", "");

            System.out.println("Task 1:");
            new Task1(repo).printDiscountStats();

            System.out.println("Task 2:");
            new Task2(repo).printCustomerStats(today);

            System.out.println("Task 3:");
            Task3 task3 = new Task3(repo);
            task3.printOrdersOnDate(today);
            task3.printOrdersInRange(weekAgo, tomorrow);
            task3.printDessertOrdersCountOnDate(today);
            task3.printDrinkOrdersCountOnDate(today);

            System.out.println("Task 4:");
            Task4 task4 = new Task4(repo);
            task4.printDrinkOrdersToday(today);
            task4.printAverageOrderTotal(today);
            task4.printMaxOrderTotal(today);
            task4.printCustomersWithMaxOrder(today);

            System.out.println("Task 5:");
            Task5 task5 = new Task5(repo);
            task5.printBaristaScheduleForWeek(today, baristaPhone);
            task5.printAllBaristasScheduleForWeek(today);
            task5.printAllStaffScheduleForWeek(today);

            System.out.println("Task 6:");
            new Task6(repo).printMenuNameStats();

            System.out.println("Task 7:");
            new Task7(repo).printMinPriceStats();

            System.out.println("Task 8:");
            new Task8(repo).printPriceStats();

            System.out.println("Task 9:");
            new Task9(repo).printStaffStats();

            System.out.println("Task 10:");
            new Task10(repo).printStaffContactStats();

            connection.commit();
            System.out.println("Done.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
