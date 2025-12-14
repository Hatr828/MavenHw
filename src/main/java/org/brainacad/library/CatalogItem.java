package org.brainacad.library;

import java.time.LocalDate;

public interface CatalogItem {
    String getTitle();

    LocalDate getPublicationDate();

    ItemType getItemType();

    default String basicSummary() {
        return getItemType() + ": " + getTitle() + " (" + getPublicationDate() + ")";
    }
}
