package org.brainacad;

import java.sql.Connection;
import java.time.LocalDate;
import org.brainacad.db.CoffeeShopRepository;
import org.brainacad.db.Database;
import org.brainacad.service.Task2;
import org.brainacad.service.Task3;
import org.brainacad.service.Task4;
import org.brainacad.service.Task5;
import org.brainacad.service.Task6;

public class App {

    public static void main(String[] args) {
        Database db = Database.fromEnv();
        System.out.printf("Connecting to %s...%n", db.jdbcUrl());
        try (Connection connection = db.openConnection()) {
            connection.setAutoCommit(false);
            CoffeeShopRepository repo = new CoffeeShopRepository(connection);

            LocalDate today = LocalDate.now();

            new Task2(repo).run();
            new Task3(repo).run();
            new Task4(repo).run();
            new Task5(repo).run();
            new Task6(repo).run(today);

            connection.commit();
            System.out.println("Done.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
