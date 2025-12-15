package org.brainacad.model;

import java.math.BigDecimal;

public class MenuItemInfo {
    public final String category;
    public final String nameEn;
    public final String nameUk;
    public final BigDecimal price;

    public MenuItemInfo(String category, String nameEn, String nameUk, BigDecimal price) {
        this.category = category;
        this.nameEn = nameEn;
        this.nameUk = nameUk;
        this.price = price;
    }

    public MenuItemInfo(String nameEn, String nameUk, BigDecimal price) {
        this(null, nameEn, nameUk, price);
    }
}
