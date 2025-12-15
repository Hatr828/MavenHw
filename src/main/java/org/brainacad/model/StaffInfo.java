package org.brainacad.model;

public class StaffInfo {
    public final String fullName;
    public final String phone;
    public final String address;
    public final boolean active;

    public StaffInfo(String fullName, String phone, String address, boolean active) {
        this.fullName = fullName;
        this.phone = phone;
        this.address = address;
        this.active = active;
    }
}
