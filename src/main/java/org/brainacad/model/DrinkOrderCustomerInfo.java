package org.brainacad.model;

public class DrinkOrderCustomerInfo {
    public final long orderId;
    public final String createdAt;
    public final String customerName;
    public final String customerPhone;
    public final String baristaName;
    public final String baristaPhone;

    public DrinkOrderCustomerInfo(long orderId, String createdAt, String customerName, String customerPhone, String baristaName, String baristaPhone) {
        this.orderId = orderId;
        this.createdAt = createdAt;
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.baristaName = baristaName;
        this.baristaPhone = baristaPhone;
    }
}
