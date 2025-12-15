package org.brainacad.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class WorkScheduleInfo {
    public final String staffName;
    public final String staffPhone;
    public final String roleCode;
    public final LocalDate shiftDate;
    public final LocalTime shiftStart;
    public final LocalTime shiftEnd;

    public WorkScheduleInfo(String staffName, String staffPhone, String roleCode, LocalDate shiftDate, LocalTime shiftStart, LocalTime shiftEnd) {
        this.staffName = staffName;
        this.staffPhone = staffPhone;
        this.roleCode = roleCode;
        this.shiftDate = shiftDate;
        this.shiftStart = shiftStart;
        this.shiftEnd = shiftEnd;
    }
}
