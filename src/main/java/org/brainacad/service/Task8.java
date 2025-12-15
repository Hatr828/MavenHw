package org.brainacad.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.util.List;
import org.brainacad.db.CoffeeShopRepository;
import org.brainacad.model.MenuItemInfo;

public class Task8 {
    private final CoffeeShopRepository repo;

    public Task8(CoffeeShopRepository repo) {
        this.repo = repo;
    }

    public void printPriceStats() throws SQLException {
        BigDecimal maxDrink = repo.findMaxDrinkPrice();
        BigDecimal maxDessert = repo.findMaxDessertPrice();
        BigDecimal avgDrink = repo.findAvgDrinkPrice();
        BigDecimal avgDessert = repo.findAvgDessertPrice();
        BigDecimal avgAll = repo.findAvgDrinkAndDessertPrice();

        System.out.printf("Max drink price: %s%n", fmt(maxDrink));
        printItems("Drink(s) with max price", repo.findDrinksWithMaxPrice());

        System.out.printf("Max dessert price: %s%n", fmt(maxDessert));
        printItems("Dessert(s) with max price", repo.findDessertsWithMaxPrice());

        System.out.printf("Average drink price: %s%n", fmt(avgDrink));
        System.out.printf("Average dessert price: %s%n", fmt(avgDessert));
        System.out.printf("Average drink+dessert price: %s%n", fmt(avgAll));
    }

    private void printItems(String title, List<MenuItemInfo> items) {
        if (items == null || items.isEmpty()) {
            System.out.printf("%s: none%n", title);
            return;
        }
        System.out.println(title + ":");
        for (MenuItemInfo item : items) {
            System.out.printf("- [%s] %s | %s | price %s%n",
                    item.category == null ? "n/a" : item.category,
                    valOrNA(item.nameEn),
                    valOrNA(item.nameUk),
                    fmt(item.price));
        }
    }

    private String fmt(BigDecimal val) {
        if (val == null) {
            return "n/a";
        }
        return val.setScale(2, RoundingMode.HALF_UP).toPlainString();
    }

    private String valOrNA(String val) {
        return (val == null || val.trim().isEmpty()) ? "n/a" : val;
    }
}
