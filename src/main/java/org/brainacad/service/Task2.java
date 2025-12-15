package org.brainacad.service;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import org.brainacad.db.CoffeeShopRepository;

public class Task2 {
    private final CoffeeShopRepository repo;

    public Task2(CoffeeShopRepository repo) {
        this.repo = repo;
    }

    public void run() throws SQLException {
        System.out.println("Task 2: inserting sample data...");
        insertMenuItem();
        insertBarista();
        insertConfectioner();
        insertCustomer();
    }

    private void insertMenuItem() throws SQLException {
        int inserted = repo.insertMenuItem("DRINK", "Flat White", "Флет-уайт", new BigDecimal("89.00"));
        logResult("Menu item [Flat White]", inserted);
    }

    private void insertBarista() throws SQLException {
        if (!ensureRoleExists("BARISTA")) {
            return;
        }
        int inserted = repo.insertStaff("Іван Петренко", "+380501234567", "Kyiv, Khreshchatyk 1", "BARISTA");
        logResult("Barista [Іван Петренко]", inserted);
    }

    private void insertConfectioner() throws SQLException {
        if (!ensureRoleExists("CONFECTIONER")) {
            return;
        }
        int inserted = repo.insertStaff("Олена Кондитер", "+380501234568", "Lviv, Svobody Ave 10", "CONFECTIONER");
        logResult("Confectioner [Олена Кондитер]", inserted);
    }

    private void insertCustomer() throws SQLException {
        LocalDate birthDate = LocalDate.of(1990, 1, 15);
        int inserted = repo.insertCustomer("Сергій Клієнт", birthDate, "+380501234569", "Dnipro, Central 15", new BigDecimal("5.0"));
        logResult("Customer [Сергій Клієнт]", inserted);
    }

    private boolean ensureRoleExists(String roleCode) throws SQLException {
        if (repo.roleExists(roleCode)) {
            return true;
        }
        System.out.printf("- Role %s not found, skipping related insert%n", roleCode);
        return false;
    }

    private void logResult(String title, int inserted) {
        if (inserted > 0) {
            System.out.printf("- %s inserted%n", title);
        } else {
            System.out.printf("- %s skipped (already exists)%n", title);
        }
    }
}
