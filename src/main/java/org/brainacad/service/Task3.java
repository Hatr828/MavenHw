package org.brainacad.service;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.brainacad.db.CoffeeShopRepository;
import org.brainacad.model.OrderInfo;

public class Task3 {
    private final CoffeeShopRepository repo;

    public Task3(CoffeeShopRepository repo) {
        this.repo = repo;
    }

    public void printOrdersOnDate(LocalDate date) throws SQLException {
        printOrders("Orders on " + date, repo.findOrdersOnDate(date));
    }

    public void printOrdersInRange(LocalDateTime from, LocalDateTime to) throws SQLException {
        printOrders(String.format("Orders from %s to %s", from, to), repo.findOrdersInRange(from, to));
    }

    public void printDessertOrdersCountOnDate(LocalDate date) throws SQLException {
        int cnt = repo.countDessertOrdersOnDate(date);
        System.out.printf("Dessert orders on %s: %d%n", date, cnt);
    }

    public void printDrinkOrdersCountOnDate(LocalDate date) throws SQLException {
        int cnt = repo.countDrinkOrdersOnDate(date);
        System.out.printf("Drink orders on %s: %d%n", date, cnt);
    }

    private void printOrders(String title, List<OrderInfo> orders) {
        if (orders == null || orders.isEmpty()) {
            System.out.printf("%s: none%n", title);
            return;
        }
        System.out.println(title + ":");
        for (OrderInfo order : orders) {
            System.out.printf("- id=%d status=%s created=%s customer=%s staff=%s comments=%s%n",
                    order.id,
                    order.status,
                    order.createdAt,
                    order.customerPhone == null ? "n/a" : order.customerPhone,
                    order.staffPhone == null ? "n/a" : order.staffPhone,
                    order.comments == null ? "" : order.comments);
        }
    }
}
