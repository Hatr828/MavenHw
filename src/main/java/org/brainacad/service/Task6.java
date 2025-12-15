package org.brainacad.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import org.brainacad.db.CoffeeShopRepository;
import org.brainacad.model.CustomerOrderTotalInfo;
import org.brainacad.model.DrinkOrderCustomerInfo;
import org.brainacad.model.WorkScheduleInfo;

public class Task6 {
    private final CoffeeShopRepository repo;

    public Task6(CoffeeShopRepository repo) {
        this.repo = repo;
    }

    public void run(LocalDate date) throws SQLException {
        System.out.println("Task 6: reporting data...");
        printDrinkOrdersWithBarista(date);
        printAverageOrderTotal(date);
        printMaxOrderTotal(date);
        printCustomersWithMaxOrder(date);
        printAllStaffScheduleForWeek(date);
    }

    private void printDrinkOrdersWithBarista(LocalDate date) throws SQLException {
        List<DrinkOrderCustomerInfo> list = repo.findDrinkOrdersWithBaristaOnDate(date);
        if (list.isEmpty()) {
            System.out.printf("Drink orders with baristas on %s: none%n", date);
            return;
        }
        System.out.printf("Drink orders with baristas on %s:%n", date);
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

    private void printAverageOrderTotal(LocalDate date) throws SQLException {
        BigDecimal avg = repo.findAverageOrderTotalOnDate(date);
        System.out.printf("Average order total on %s: %s%n", date, formatMoney(avg));
    }

    private void printMaxOrderTotal(LocalDate date) throws SQLException {
        BigDecimal max = repo.findMaxOrderTotalOnDate(date);
        System.out.printf("Max order total on %s: %s%n", date, formatMoney(max));
    }

    private void printCustomersWithMaxOrder(LocalDate date) throws SQLException {
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

    private void printAllStaffScheduleForWeek(LocalDate anyDateInWeek) throws SQLException {
        LocalDate weekStart = startOfWeek(anyDateInWeek);
        List<WorkScheduleInfo> schedule = repo.findAllStaffScheduleForWeek(weekStart);
        if (schedule == null || schedule.isEmpty()) {
            System.out.printf("All staff schedule for week starting %s: none%n", weekStart);
            return;
        }
        System.out.printf("All staff schedule for week starting %s:%n", weekStart);
        for (WorkScheduleInfo info : schedule) {
            System.out.printf("- %s (%s) [%s] %s %s-%s%n",
                    info.staffName,
                    emptyToNA(info.staffPhone),
                    info.roleCode,
                    info.shiftDate,
                    info.shiftStart,
                    info.shiftEnd);
        }
    }

    private LocalDate startOfWeek(LocalDate date) {
        DayOfWeek dow = date.getDayOfWeek();
        int daysFromMonday = dow.getValue() - DayOfWeek.MONDAY.getValue();
        return date.minusDays(daysFromMonday);
    }

    private String formatMoney(BigDecimal val) {
        if (val == null) {
            return "n/a";
        }
        return val.setScale(2, RoundingMode.HALF_UP).toPlainString();
    }

    private String emptyToNA(String val) {
        return (val == null || val.trim().isEmpty()) ? "n/a" : val;
    }
}
