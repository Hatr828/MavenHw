package org.brainacad.service;

import java.sql.SQLException;
import java.util.List;
import org.brainacad.db.CoffeeShopRepository;
import org.brainacad.model.MenuItemInfo;
import org.brainacad.model.OrderInfo;
import org.brainacad.model.StaffInfo;

public class Task5QueryService {
    private final CoffeeShopRepository repo;

    public Task5QueryService(CoffeeShopRepository repo) {
        this.repo = repo;
    }

    public void run() throws SQLException {
        System.out.println("Task 5:");
        showMenuCategory("DRINK");
        showMenuCategory("DESSERT");
        showStaffByRole("BARISTA", "Бариста");
        showStaffByRole("WAITER", "Офіціант");
        showOrdersByDessert("Cheesecake New York", "Berry Cheesecake");
        showOrdersByWaiter("+380503334455");
        showOrdersByCustomer("+380981112233");
    }

    private void showMenuCategory(String category) throws SQLException {
        List<MenuItemInfo> items = repo.findMenuItemsByCategory(category);
        System.out.printf("Menu items (%s):%n", category);
        for (MenuItemInfo i : items) {
            System.out.printf(" - %s / %s : %s%n", i.nameEn, i.nameUk, i.price);
        }
    }

    private void showStaffByRole(String roleCode, String roleLabel) throws SQLException {
        List<StaffInfo> staff = repo.findStaffByRole(roleCode);
        System.out.printf("Staff (%s):%n", roleLabel);
        for (StaffInfo s : staff) {
            System.out.printf(" - %s, %s, %s, active=%s%n", s.fullName, s.phone, s.address, s.active);
        }
    }

    private void showOrdersByDessert(String primaryName, String fallback) throws SQLException {
        List<OrderInfo> orders = repo.findOrdersByDessertName(primaryName);
        if (orders.isEmpty() && fallback != null) {
            orders = repo.findOrdersByDessertName(fallback);
            if (!orders.isEmpty()) {
                System.out.printf("Orders for dessert (fallback %s):%n", fallback);
                printOrders(orders);
                return;
            }
        }
        System.out.printf("Orders for dessert %s:%n", primaryName);
        printOrders(orders);
    }

    private void showOrdersByWaiter(String waiterPhone) throws SQLException {
        List<OrderInfo> orders = repo.findOrdersByWaiterPhone(waiterPhone);
        System.out.printf("Orders by waiter %s:%n", waiterPhone);
        printOrders(orders);
    }

    private void showOrdersByCustomer(String customerPhone) throws SQLException {
        List<OrderInfo> orders = repo.findOrdersByCustomerPhone(customerPhone);
        System.out.printf("Orders by customer %s:%n", customerPhone);
        printOrders(orders);
    }

    private void printOrders(List<OrderInfo> orders) {
        if (orders.isEmpty()) {
            System.out.println(" (none)");
            return;
        }
        for (OrderInfo o : orders) {
            System.out.printf(" - id=%d, status=%s, created=%s, customer=%s, staff=%s, comment=%s%n",
                    o.id, o.status, o.createdAt, o.customerPhone, o.staffPhone, o.comments);
        }
    }
}
