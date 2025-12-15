package org.brainacad.service;

import java.sql.SQLException;
import org.brainacad.db.CoffeeShopRepository;

public class Task4 {
    private final CoffeeShopRepository repo;

    public Task4(CoffeeShopRepository repo) {
        this.repo = repo;
    }

    public void run() throws SQLException {
        System.out.println("Task 4: deleting sample data...");
        deleteDessert();
        deleteWaiter();
        deleteBarista();
        deleteCustomer();
    }

    private void deleteDessert() throws SQLException {
        int deleted = repo.deleteMenuItemByNameAndCategory("DESSERT", "Cheesecake");
        logDelete("Dessert [Cheesecake]", deleted);
    }

    private void deleteWaiter() throws SQLException {
        int deleted = repo.deleteStaffByPhoneAndRole("+380501234571", "WAITER");
        logDelete("Waiter [+380501234571]", deleted);
    }

    private void deleteBarista() throws SQLException {
        int deleted = repo.deleteStaffByPhoneAndRole("+380501234570", "BARISTA");
        logDelete("Barista [+380501234570]", deleted);
    }

    private void deleteCustomer() throws SQLException {
        int deleted = repo.deleteCustomerByPhone("+380501234569");
        logDelete("Customer [+380501234569]", deleted);
    }

    private void logDelete(String title, int deleted) {
        if (deleted > 0) {
            System.out.printf("- %s deleted%n", title);
        } else {
            System.out.printf("- %s not found%n", title);
        }
    }
}
