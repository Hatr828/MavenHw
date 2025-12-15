package org.brainacad.service;

import java.sql.SQLException;
import java.util.List;
import org.brainacad.db.CoffeeShopRepository;
import org.brainacad.model.StaffHireInfo;

public class Task9 {
    private final CoffeeShopRepository repo;

    public Task9(CoffeeShopRepository repo) {
        this.repo = repo;
    }

    public void printStaffStats() throws SQLException {
        int baristas = repo.countStaffByRole("BARISTA");
        int waiters = repo.countStaffByRole("WAITER");
        int confectioners = repo.countStaffByRole("CONFECTIONER");
        int total = repo.countAllStaff();

        System.out.printf("Baristas: %d%n", baristas);
        System.out.printf("Waiters: %d%n", waiters);
        System.out.printf("Confectioners: %d%n", confectioners);
        System.out.printf("Total staff: %d%n", total);

        printStaffList("Latest hired staff", repo.findLatestHiredStaff());
        printStaffList("Longest working staff", repo.findLongestWorkingStaff());
    }

    private void printStaffList(String title, List<StaffHireInfo> staff) {
        if (staff == null || staff.isEmpty()) {
            System.out.printf("%s: none%n", title);
            return;
        }
        System.out.println(title + ":");
        for (StaffHireInfo s : staff) {
            System.out.printf("- %s (%s) role=%s hired=%s active=%s address=%s%n",
                    s.fullName,
                    s.phone,
                    s.roleCode,
                    s.hiredAt,
                    s.active,
                    s.address);
        }
    }
}
