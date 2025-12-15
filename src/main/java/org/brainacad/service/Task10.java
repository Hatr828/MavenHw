package org.brainacad.service;

import java.sql.SQLException;
import java.util.List;
import org.brainacad.db.CoffeeShopRepository;
import org.brainacad.model.StaffInfo;

public class Task10 {
    private final CoffeeShopRepository repo;

    public Task10(CoffeeShopRepository repo) {
        this.repo = repo;
    }

    public void printStaffContactStats() throws SQLException {
        printStaff("Staff without phone", repo.findStaffWithoutPhone());
        printStrings("Unique first names", repo.findUniqueStaffFirstNames());
        printStrings("Unique last names", repo.findUniqueStaffLastNames());
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
                    s.phone == null || s.phone.trim().isEmpty() ? "n/a" : s.phone,
                    s.address,
                    s.active);
        }
    }

    private void printStrings(String title, List<String> values) {
        if (values == null || values.isEmpty()) {
            System.out.printf("%s: none%n", title);
            return;
        }
        System.out.println(title + ":");
        for (String v : values) {
            System.out.printf("- %s%n", v);
        }
    }
}
