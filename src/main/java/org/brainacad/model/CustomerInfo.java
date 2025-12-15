package org.brainacad.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class CustomerInfo {
    public final String fullName;
    public final String phone;
    public final LocalDate birthDate;
    public final String address;
    public final BigDecimal discountPercent;

    public CustomerInfo(String fullName, String phone, LocalDate birthDate, String address, BigDecimal discountPercent) {
        this.fullName = fullName;
        this.phone = phone;
        this.birthDate = birthDate;
        this.address = address;
        this.discountPercent = discountPercent;
    }
}
