package org.brainacad.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.util.List;
import org.brainacad.db.CoffeeShopRepository;
import org.brainacad.model.MenuItemInfo;

public class Task6 {
    private final CoffeeShopRepository repo;

    public Task6(CoffeeShopRepository repo) {
        this.repo = repo;
    }

    public void printMenuNameStats() throws SQLException {
        printItems("Drinks with names in two languages", repo.findDrinksWithTwoNames());
        printItems("Drinks with name in one language", repo.findDrinksWithSingleName());
        printItems("Desserts with names in two languages", repo.findDessertsWithTwoNames());
        printItems("Desserts with name in one language", repo.findDessertsWithSingleName());
        printItems("All items with name in one language", repo.findItemsWithSingleName());
        printItems("All items with names in two languages", repo.findItemsWithTwoNames());
        printItems("Items with identical EN/UK name", repo.findItemsWithSameName());
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
