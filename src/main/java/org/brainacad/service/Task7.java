package org.brainacad.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.util.List;
import org.brainacad.db.CoffeeShopRepository;
import org.brainacad.model.MenuItemInfo;

public class Task7 {
    private final CoffeeShopRepository repo;

    public Task7(CoffeeShopRepository repo) {
        this.repo = repo;
    }

    public void printMinPriceStats() throws SQLException {
        BigDecimal minDrink = repo.findMinDrinkPrice();
        BigDecimal minDessert = repo.findMinDessertPrice();

        System.out.printf("Min drink price: %s%n", formatMoney(minDrink));
        printItems("Drink(s) with min price", repo.findDrinksWithMinPrice());

        System.out.printf("Min dessert price: %s%n", formatMoney(minDessert));
        printItems("Dessert(s) with min price", repo.findDessertsWithMinPrice());
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
                    nullToNA(item.nameEn),
                    nullToNA(item.nameUk),
                    formatMoney(item.price));
        }
    }

    private String formatMoney(BigDecimal val) {
        if (val == null) {
            return "n/a";
        }
        return val.setScale(2, RoundingMode.HALF_UP).toPlainString();
    }

    private String nullToNA(String val) {
        return (val == null || val.trim().isEmpty()) ? "n/a" : val;
    }
}
