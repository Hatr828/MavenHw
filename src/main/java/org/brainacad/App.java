package org.brainacad;

import java.sql.Connection;
import org.brainacad.db.CoffeeShopRepository;
import org.brainacad.db.Database;
import org.brainacad.service.Task2InsertService;
import org.brainacad.service.Task3UpdateService;
import org.brainacad.service.Task4DeleteService;
import org.brainacad.service.Task5QueryService;

public class App {

    public static void main(String[] args) {
        Database db = Database.fromEnv();
        System.out.printf("Connecting to %s...%n", db.jdbcUrl());
        try (Connection connection = db.openConnection()) {
            connection.setAutoCommit(false);
            CoffeeShopRepository repo = new CoffeeShopRepository(connection);

            Task2InsertService task2 = new Task2InsertService(repo);
            Task3UpdateService task3 = new Task3UpdateService(repo);
            Task4DeleteService task4 = new Task4DeleteService(repo);
            Task5QueryService task5 = new Task5QueryService(repo);

            task2.run();
            task3.run();
            task4.run();
            task5.run();
            connection.commit();
            System.out.println("Done.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
