package org.brainacad.service;

import java.sql.SQLException;
import org.brainacad.db.CoffeeShopRepository;

public class Task4DeleteService {
    private final CoffeeShopRepository repo;

    public Task4DeleteService(CoffeeShopRepository repo) {
        this.repo = repo;
    }

    public void run() throws SQLException {
        System.out.println("Task 4:");

        deleteDessertOrders("Cheesecake New York", "Berry Cheesecake");
        deleteLatestOrderForCustomer("+380981112233");
        clearOrdersForStaff("+380503334455");
        clearOrdersForStaff("+380631234567"); 
        clearOrdersForCustomer("+380981112233");

        deleteDessert("Cheesecake New York", "Berry Cheesecake");
        deleteStaff("WAITER", "+380503334455");
        deleteStaff("BARISTA", "+380631234567");
        deleteCustomer("+380981112233");
    }

    private void deleteDessert(String primaryName, String fallbackName) throws SQLException {
        int deleted = repo.deleteMenuItemByNameAndCategory("DESSERT", primaryName);
        if (deleted == 0 && fallbackName != null) {
            deleted = repo.deleteMenuItemByNameAndCategory("DESSERT", fallbackName);
            if (deleted > 0) {
                System.out.printf("Deleted dessert via fallback: %s%n", fallbackName);
                return;
            }
        }
        System.out.printf("Delete dessert %s -> %d row(s)%n", primaryName, deleted);
    }

    private void deleteStaff(String roleCode, String phone) throws SQLException {
        int deleted = repo.deleteStaffByPhoneAndRole(phone, roleCode);
        System.out.printf("Delete staff %s (%s) -> %d row(s)%n", phone, roleCode, deleted);
    }

    private void deleteCustomer(String phone) throws SQLException {
        int deleted = repo.deleteCustomerByPhone(phone);
        System.out.printf("Delete customer %s -> %d row(s)%n", phone, deleted);
    }

    private void deleteDessertOrders(String primaryName, String fallbackName) throws SQLException {
        int deleted = repo.deleteOrderItemsByDessertName(primaryName);
        if (deleted == 0 && fallbackName != null) {
            deleted = repo.deleteOrderItemsByDessertName(fallbackName);
            if (deleted > 0) {
                System.out.printf("Deleted dessert order items via fallback: %s%n", fallbackName);
                return;
            }
        }
        System.out.printf("Delete order items for dessert %s -> %d row(s)%n", primaryName, deleted);
    }

    private void deleteLatestOrderForCustomer(String customerPhone) throws SQLException {
        int deleted = repo.deleteLatestOrderForCustomer(customerPhone);
        System.out.printf("Delete latest order for customer %s -> %d row(s)%n", customerPhone, deleted);
    }

    private void clearOrdersForStaff(String staffPhone) throws SQLException {
        int updated = repo.clearOrdersStaffByPhone(staffPhone);
        System.out.printf("Clear staff from orders %s -> %d row(s)%n", staffPhone, updated);
    }

    private void clearOrdersForCustomer(String customerPhone) throws SQLException {
        int updated = repo.clearOrdersCustomerByPhone(customerPhone);
        System.out.printf("Clear customer from orders %s -> %d row(s)%n", customerPhone, updated);
    }
}
