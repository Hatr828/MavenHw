package org.brainacad.model;

import java.math.BigDecimal;

public class CustomerOrderTotalInfo {
    public final long orderId;
    public final String customerName;
    public final String customerPhone;
    public final BigDecimal total;

    public CustomerOrderTotalInfo(long orderId, String customerName, String customerPhone, BigDecimal total) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.total = total;
    }
}
