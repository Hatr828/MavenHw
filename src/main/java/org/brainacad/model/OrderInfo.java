package org.brainacad.model;

public class OrderInfo {
    public final long id;
    public final String status;
    public final String createdAt;
    public final String customerPhone;
    public final String staffPhone;
    public final String comments;

    public OrderInfo(long id, String status, String createdAt, String customerPhone, String staffPhone, String comments) {
        this.id = id;
        this.status = status;
        this.createdAt = createdAt;
        this.customerPhone = customerPhone;
        this.staffPhone = staffPhone;
        this.comments = comments;
    }
}
