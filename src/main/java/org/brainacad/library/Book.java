package org.brainacad.library;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
public class Book implements CatalogItem {
    @ToString.Include
    private String title;

    @ToString.Include
    private String author;

    private String genre;
    private int pageCount;
    private String annotation;
    private LocalDate publicationDate;

    @Override
    public ItemType getItemType() {
        return ItemType.BOOK;
    }
}
