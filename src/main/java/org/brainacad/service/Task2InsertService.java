package org.brainacad.service;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.brainacad.db.CoffeeShopRepository;

public class Task2InsertService {
    private final CoffeeShopRepository repo;

    public Task2InsertService(CoffeeShopRepository repo) {
        this.repo = repo;
    }

    public void run() throws SQLException {
        System.out.println("Task 2:");
        repo.insertMenuItem("DRINK", "Flat White Caramel", "Флет-вайт карамель", new BigDecimal("4.10"));
        repo.insertStaff("Дмитро Бариста", "+380631234567", "Київ, вул. Мельникова, 8", "BARISTA");
        repo.insertStaff("Катерина Кондитер", "+380662223344", "Київ, вул. Ломоносова, 3", "PASTRY_CHEF");
        repo.insertCustomer("Оксана Клієнт", LocalDate.of(1992, 5, 10), "+380981112233", "Київ, вул. Вишнева, 15", new BigDecimal("7.5"));

        long coffeeOrderId = createOrder("+380981112233", "+380503334455", "Coffee order");
        addOrderItem(coffeeOrderId, "Espresso", 1, "Espresso Doppio");

        long dessertOrderId = createOrder("+380981112233", "+380503334455", "Dessert order");
        addOrderItem(dessertOrderId, "Cheesecake New York", 2, "Berry Cheesecake");

        addUpcomingMondaySchedule(Arrays.asList(
                "+380501112233",
                "+380503334455",
                "+380675556677",
                "+380631234567",
                "+380662223344"
        ));

        repo.insertMenuItem("DRINK", "Cold Brew", "Колд брю", new BigDecimal("4.30"));
    }

    private long createOrder(String customerPhone, String staffPhone, String comments) throws SQLException {
        Long customerId = repo.findIdByPhone("customers", customerPhone);
        Long staffId = repo.findIdByPhone("staff", staffPhone);
        if (customerId == null || staffId == null) {
            throw new IllegalStateException("Missing customer or staff for order creation");
        }
        long orderId = repo.createOrder(customerId, staffId, comments);
        System.out.printf("Order created (id=%d) for customer %s%n", orderId, customerPhone);
        return orderId;
    }

    private void addOrderItem(long orderId, String menuItemEnName, int quantity, String... fallbackNames) throws SQLException {
        Long menuItemId = findMenuItemIdWithFallback(menuItemEnName, fallbackNames);
        BigDecimal price = repo.getMenuItemPrice(menuItemId);
        repo.insertOrderItem(orderId, menuItemId, quantity, price);
        System.out.printf("Order item: order %d, %s x%d%n", orderId, menuItemEnName, quantity);
    }

    private void addUpcomingMondaySchedule(List<String> staffPhones) throws SQLException {
        LocalDate nextMonday = LocalDate.now().with(TemporalAdjusters.nextOrSame(DayOfWeek.MONDAY));
        List<Long> staffIds = new ArrayList<>();
        for (String phone : staffPhones) {
            Long staffId = repo.findIdByPhone("staff", phone);
            if (staffId != null) {
                staffIds.add(staffId);
            } else {
                System.out.printf("Skip schedule: staff not found %s%n", phone);
            }
        }
        repo.addSchedulesForDate(nextMonday, staffIds);
        System.out.printf("Schedules for %s -> %d staff%n", nextMonday, staffIds.size());
    }

    private Long findMenuItemIdWithFallback(String primaryName, String... fallbackNames) throws SQLException {
        Long menuItemId = repo.findMenuItemIdByName(primaryName);
        if (menuItemId != null) {
            return menuItemId;
        }
        if (fallbackNames != null) {
            for (String name : fallbackNames) {
                menuItemId = repo.findMenuItemIdByName(name);
                if (menuItemId != null) {
                    System.out.printf("Fallback to menu item name: %s%n", name);
                    return menuItemId;
                }
            }
        }
        throw new IllegalStateException("Menu item not found: " + primaryName);
    }
}
