package org.brainacad.service;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import org.brainacad.db.CoffeeShopRepository;
import org.brainacad.model.CustomerOrderTotalInfo;
import org.brainacad.model.DrinkOrderCustomerInfo;

public class Task4 {
    private final CoffeeShopRepository repo;

    public Task4(CoffeeShopRepository repo) {
        this.repo = repo;
    }

    public void printDrinkOrdersToday(LocalDate date) throws SQLException {
        List<DrinkOrderCustomerInfo> list = repo.findDrinkOrdersWithBaristaOnDate(date);
        if (list.isEmpty()) {
            System.out.printf("Drink orders on %s: none%n", date);
            return;
        }
        System.out.println("Drink orders with baristas on " + date + ":");
        for (DrinkOrderCustomerInfo info : list) {
            System.out.printf("- order %d at %s | customer: %s (%s) | barista: %s (%s)%n",
                    info.orderId,
                    info.createdAt,
                    emptyToNA(info.customerName),
                    emptyToNA(info.customerPhone),
                    emptyToNA(info.baristaName),
                    emptyToNA(info.baristaPhone));
        }
    }

    public void printAverageOrderTotal(LocalDate date) throws SQLException {
        BigDecimal avg = repo.findAverageOrderTotalOnDate(date);
        System.out.printf("Average order total on %s: %s%n", date, formatMoney(avg));
    }

    public void printMaxOrderTotal(LocalDate date) throws SQLException {
        BigDecimal max = repo.findMaxOrderTotalOnDate(date);
        System.out.printf("Max order total on %s: %s%n", date, formatMoney(max));
    }

    public void printCustomersWithMaxOrder(LocalDate date) throws SQLException {
        List<CustomerOrderTotalInfo> list = repo.findCustomersWithMaxOrderOnDate(date);
        if (list.isEmpty()) {
            System.out.printf("Customers with max order on %s: none%n", date);
            return;
        }
        System.out.printf("Customers with max order on %s:%n", date);
        for (CustomerOrderTotalInfo info : list) {
            System.out.printf("- order %d | total %s | customer: %s (%s)%n",
                    info.orderId,
                    formatMoney(info.total),
                    emptyToNA(info.customerName),
                    emptyToNA(info.customerPhone));
        }
    }

    private String formatMoney(BigDecimal val) {
        if (val == null) {
            return "n/a";
        }
        return val.setScale(2, java.math.RoundingMode.HALF_UP).toPlainString();
    }

    private String emptyToNA(String val) {
        return (val == null || val.trim().isEmpty()) ? "n/a" : val;
    }
}
