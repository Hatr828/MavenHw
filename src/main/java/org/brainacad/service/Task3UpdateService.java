package org.brainacad.service;

import java.math.BigDecimal;
import java.sql.SQLException;
import org.brainacad.db.CoffeeShopRepository;

public class Task3UpdateService {
    private final CoffeeShopRepository repo;

    public Task3UpdateService(CoffeeShopRepository repo) {
        this.repo = repo;
    }

    public void run() throws SQLException {
        System.out.println("Task 3:");
        repo.updateMenuPrice("Espresso", new BigDecimal("2.80"));
        repo.updateStaffAddress("+380662223344", "PASTRY_CHEF", "Київ, вул. Прорізна, 18");
        int phoneUpdated = repo.updateStaffPhone("+380631234567", "BARISTA", "+380631230000");
        if (phoneUpdated == 0) {
            System.out.println("Skip phone update: target phone already in use or not found");
        }
        repo.updateCustomerDiscount("+380981112233", new BigDecimal("12.5"));
        repo.renameMenuItem("DRINK", "Espresso", "Espresso Doppio", "Еспресо доппіо");
        repo.renameMenuItem("DESSERT", "Cheesecake New York", "Berry Cheesecake", "Сирник з ягодами");
    }
}
