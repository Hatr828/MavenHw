package org.brainacad.service;

import java.math.BigDecimal;
import java.sql.SQLException;
import org.brainacad.db.CoffeeShopRepository;

public class Task3 {
    private final CoffeeShopRepository repo;

    public Task3(CoffeeShopRepository repo) {
        this.repo = repo;
    }

    public void run() throws SQLException {
        System.out.println("Task 3: updating sample data...");
        updateDrinkPrice();
        updateConfectionerAddress();
        updateBaristaPhone();
        updateCustomerDiscount();
    }

    private void updateDrinkPrice() throws SQLException {
        int updated = repo.updateMenuPrice("Flat White", new BigDecimal("92.00"));
        logUpdate("Drink price [Flat White]", updated);
    }

    private void updateConfectionerAddress() throws SQLException {
        int updated = repo.updateStaffAddress("+380501234568", "CONFECTIONER", "Lviv, Heroiv UPA 20");
        logUpdate("Confectioner address [+380501234568]", updated);
    }

    private void updateBaristaPhone() throws SQLException {
        int updated = repo.updateStaffPhone("+380501234567", "BARISTA", "+380501234570");
        logUpdate("Barista phone [+380501234567 -> +380501234570]", updated);
    }

    private void updateCustomerDiscount() throws SQLException {
        int updated = repo.updateCustomerDiscount("+380501234569", new BigDecimal("7.5"));
        logUpdate("Customer discount [+380501234569]", updated);
    }

    private void logUpdate(String title, int updated) {
        if (updated > 0) {
            System.out.printf("- %s updated%n", title);
        } else {
            System.out.printf("- %s not changed (no match)%n", title);
        }
    }
}
