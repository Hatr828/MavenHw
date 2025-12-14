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
public class Newspaper implements CatalogItem {
    @ToString.Include
    private String title;

    private LocalDate issueDate;

    @Builder.Default
    private List<String> headlines = new ArrayList<>();

    @Override
    public LocalDate getPublicationDate() {
        return issueDate;
    }

    @Override
    public ItemType getItemType() {
        return ItemType.NEWSPAPER;
    }
}
