package org.brainacad.library;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
public class Almanac implements CatalogItem {
    @ToString.Include
    private String title;

    private String annotation;
    private String focus;
    private int pageCount;
    private LocalDate publicationDate;

    @Builder.Default
    private List<Book> works = new ArrayList<>();

    @Override
    public LocalDate getPublicationDate() {
        return publicationDate;
    }

    @Override
    public ItemType getItemType() {
        return ItemType.ALMANAC;
    }
}
