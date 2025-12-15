package org.brainacad.service;

import java.sql.SQLException;
import java.util.List;
import org.brainacad.db.CoffeeShopRepository;
import org.brainacad.model.MenuItemInfo;
import org.brainacad.model.StaffInfo;

public class Task5 {
    private final CoffeeShopRepository repo;

    public Task5(CoffeeShopRepository repo) {
        this.repo = repo;
    }

    public void run() throws SQLException {
        System.out.println("Task 5: listing data...");
        showDrinks();
        showDesserts();
        showBaristas();
        showWaiters();
    }

    private void showDrinks() throws SQLException {
        printMenu("All drinks", repo.findMenuItemsByCategory("DRINK"));
    }

    private void showDesserts() throws SQLException {
        printMenu("All desserts", repo.findMenuItemsByCategory("DESSERT"));
    }

    private void showBaristas() throws SQLException {
        printStaff("All baristas", repo.findStaffByRole("BARISTA"));
    }

    private void showWaiters() throws SQLException {
        printStaff("All waiters", repo.findStaffByRole("WAITER"));
    }

    private void printMenu(String title, List<MenuItemInfo> items) {
        if (items == null || items.isEmpty()) {
            System.out.printf("%s: none%n", title);
            return;
        }
        System.out.println(title + ":");
        for (MenuItemInfo item : items) {
            System.out.printf("- [%s] %s | %s | price %s%n",
                    item.category,
                    nullToNA(item.nameEn),
                    nullToNA(item.nameUk),
                    item.price);
        }
    }

    private void printStaff(String title, List<StaffInfo> staff) {
        if (staff == null || staff.isEmpty()) {
            System.out.printf("%s: none%n", title);
            return;
        }
        System.out.println(title + ":");
        for (StaffInfo s : staff) {
            System.out.printf("- %s | phone: %s | address: %s | active: %s%n",
                    s.fullName,
                    nullToNA(s.phone),
                    nullToNA(s.address),
                    s.active);
        }
    }

    private String nullToNA(String val) {
        return (val == null || val.trim().isEmpty()) ? "n/a" : val;
    }
}
