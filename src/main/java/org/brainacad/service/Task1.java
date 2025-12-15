package org.brainacad.service;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import org.brainacad.db.CoffeeShopRepository;
import org.brainacad.model.CustomerInfo;

public class Task1 {
    private final CoffeeShopRepository repo;

    public Task1(CoffeeShopRepository repo) {
        this.repo = repo;
    }

    public void printDiscountStats() throws SQLException {
        BigDecimal minDiscount = repo.findMinDiscount();
        BigDecimal maxDiscount = repo.findMaxDiscount();
        BigDecimal avgDiscount = repo.findAvgDiscount();

        System.out.printf("Min discount: %s%%%n", formatPercent(minDiscount));
        printCustomersWithDiscount("Customers with min discount", minDiscount);

        System.out.printf("Max discount: %s%%%n", formatPercent(maxDiscount));
        printCustomersWithDiscount("Customers with max discount", maxDiscount);

        System.out.printf("Average discount: %s%%%n", formatPercent(avgDiscount));
    }

    private void printCustomersWithDiscount(String title, BigDecimal discount) throws SQLException {
        if (discount == null) {
            System.out.printf("%s: none%n", title);
            return;
        }
        List<CustomerInfo> customers = repo.findCustomersWithDiscount(discount);
        if (customers.isEmpty()) {
            System.out.printf("%s: none%n", title);
            return;
        }
        System.out.println(title + ":");
        for (CustomerInfo customer : customers) {
            System.out.printf("- %s (%s) discount %s%%%n",
                    customer.fullName,
                    customer.phone,
                    formatPercent(customer.discountPercent));
        }
    }

    private String formatPercent(BigDecimal value) {
        if (value == null) {
            return "n/a";
        }
        return value.stripTrailingZeros().toPlainString();
    }
}
