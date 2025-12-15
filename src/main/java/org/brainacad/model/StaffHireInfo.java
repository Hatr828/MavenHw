package org.brainacad.model;

import java.time.LocalDate;

public class StaffHireInfo {
    public final String fullName;
    public final String phone;
    public final String address;
    public final boolean active;
    public final String roleCode;
    public final LocalDate hiredAt;

    public StaffHireInfo(String fullName, String phone, String address, boolean active, String roleCode, LocalDate hiredAt) {
        this.fullName = fullName;
        this.phone = phone;
        this.address = address;
        this.active = active;
        this.roleCode = roleCode;
        this.hiredAt = hiredAt;
    }
}
