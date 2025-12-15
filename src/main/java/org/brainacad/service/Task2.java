package org.brainacad.service;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import org.brainacad.db.CoffeeShopRepository;
import org.brainacad.model.CustomerInfo;

public class Task2 {
    private final CoffeeShopRepository repo;

    public Task2(CoffeeShopRepository repo) {
        this.repo = repo;
    }

    public void printCustomerStats(LocalDate date) throws SQLException {
        printCustomers("Youngest customers", repo.findYoungestCustomers());
        printCustomers("Oldest customers", repo.findOldestCustomers());
        printCustomers("Birthdays on " + date, repo.findBirthdayCustomers(date));
        printCustomers("Customers without address", repo.findCustomersWithoutAddress());
    }

    private void printCustomers(String title, List<CustomerInfo> customers) {
        if (customers == null || customers.isEmpty()) {
            System.out.printf("%s: none%n", title);
            return;
        }
        System.out.println(title + ":");
        for (CustomerInfo customer : customers) {
            System.out.printf("- %s | phone: %s | birth: %s | address: %s | discount: %s%%%n",
                    customer.fullName,
                    customer.phone,
                    customer.birthDate,
                    customer.address == null ? "n/a" : customer.address,
                    customer.discountPercent.stripTrailingZeros().toPlainString());
        }
    }
}
