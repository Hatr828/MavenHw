package org.brainacad.service;

import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import org.brainacad.db.CoffeeShopRepository;
import org.brainacad.model.WorkScheduleInfo;

public class Task5 {
    private final CoffeeShopRepository repo;

    public Task5(CoffeeShopRepository repo) {
        this.repo = repo;
    }

    public void printBaristaScheduleForWeek(LocalDate anyDateInWeek, String baristaPhone) throws SQLException {
        if (baristaPhone == null || baristaPhone.trim().isEmpty()) {
            System.out.println("Barista phone not provided, skipping individual schedule.");
            return;
        }
        LocalDate weekStart = startOfWeek(anyDateInWeek);
        printSchedule("Barista schedule for " + baristaPhone, repo.findBaristaScheduleForWeek(weekStart, baristaPhone));
    }

    public void printAllBaristasScheduleForWeek(LocalDate anyDateInWeek) throws SQLException {
        LocalDate weekStart = startOfWeek(anyDateInWeek);
        printSchedule("All baristas schedule", repo.findAllBaristaScheduleForWeek(weekStart));
    }

    public void printAllStaffScheduleForWeek(LocalDate anyDateInWeek) throws SQLException {
        LocalDate weekStart = startOfWeek(anyDateInWeek);
        printSchedule("All staff schedule", repo.findAllStaffScheduleForWeek(weekStart));
    }

    private LocalDate startOfWeek(LocalDate date) {
        DayOfWeek dow = date.getDayOfWeek();
        int daysFromMonday = dow.getValue() - DayOfWeek.MONDAY.getValue();
        return date.minusDays(daysFromMonday);
    }

    private void printSchedule(String title, List<WorkScheduleInfo> schedule) {
        if (schedule == null || schedule.isEmpty()) {
            System.out.printf("%s: none%n", title);
            return;
        }
        System.out.println(title + ":");
        for (WorkScheduleInfo info : schedule) {
            System.out.printf("- %s (%s) [%s] %s %s-%s%n",
                    info.staffName,
                    info.staffPhone,
                    info.roleCode,
                    info.shiftDate,
                    info.shiftStart,
                    info.shiftEnd);
        }
    }
}
