package org.brainacad;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.brainacad.library.Almanac;
import org.brainacad.library.Book;
import org.brainacad.library.CatalogItem;
import org.brainacad.library.LibraryCatalog;
import org.brainacad.library.Newspaper;

public class App {
    public static void main(String[] args) {
        LibraryCatalog catalog = LibraryCatalog.withSampleData();

        System.out.println("Initial catalog:");
        System.out.println(catalog.formattedCatalog());

        catalog.addItem(Newspaper.builder()
                .title("Weekly Observer")
                .issueDate(LocalDate.now())
                .headlines(Arrays.asList(
                        "New waterfront park opens this weekend",
                        "Local bookstore hosts author meet-and-greet",
                        "Robotics club wins regional contest"))
                .build());

        catalog.addItem(Book.builder()
                .title("Whispering Pages")
                .author("Donna Tartt")
                .genre("Mystery")
                .pageCount(280)
                .annotation("An archivist uncovers dangerous secrets in old diaries.")
                .publicationDate(LocalDate.of(2016, 6, 15))
                .build());

        CatalogItem randomAddition = catalog.addRandomItem();
        System.out.println("\nAfter adding manual entries and a random item:");
        System.out.println(catalog.formattedCatalog());
        System.out.println("\nRandom item added: " + randomAddition.basicSummary());

        boolean removed = catalog.removeByTitle("The Silent Library");
        System.out.println("\nRemoved 'The Silent Library': " + removed);

        System.out.println("\nCatalog after removal:");
        System.out.println(catalog.formattedCatalog());

        System.out.println("\nSearch by title containing 'City' (books/newspapers):");
        printItems(catalog.searchBooksOrNewspapersByTitle("City"));

        System.out.println("\nSearch by author 'Ursula':");
        printItems(catalog.searchByAuthor("Ursula"));

        System.out.println("\nBooks with keywords 'journey' and 'dream' in annotation:");
        printItems(catalog.searchBooksByKeywords(Arrays.asList("journey", "dream")));

        System.out.println("\nBooks or almanacs up to 350 pages for genre 'Mystery':");
        printItems(catalog.searchByPagesGenreOrAuthor(350, "Mystery", null));

        Almanac newAnthology = Almanac.builder()
                .title("Voices of Tomorrow")
                .annotation("Fresh authors share speculative tales.")
                .focus("Emerging sci-fi writers")
                .pageCount(330)
                .publicationDate(LocalDate.of(2023, 9, 10))
                .works(Arrays.asList(
                        Book.builder()
                                .title("Glass Gardens")
                                .author("Neil Gaiman")
                                .genre("Fantasy")
                                .pageCount(160)
                                .annotation("A botanist enters worlds within enchanted terrariums.")
                                .publicationDate(LocalDate.of(2022, 4, 22))
                                .build(),
                        Book.builder()
                                .title("Edge of Memory")
                                .author("Octavia Butler")
                                .genre("Science Fiction")
                                .pageCount(150)
                                .annotation("Explorers chart worlds they can no longer remember.")
                                .publicationDate(LocalDate.of(2021, 1, 5))
                                .build()))
                .build();

        catalog.addItem(newAnthology);

        System.out.println("\nBooks or almanacs up to 400 pages by author 'Gaiman':");
        printItems(catalog.searchByPagesGenreOrAuthor(400, null, "Gaiman"));

        System.out.println("\nNewspapers containing headline 'library':");
        printItems(catalog.searchNewspapersByHeadline("library"));
    }

    private static void printItems(List<? extends CatalogItem> items) {
        if (items.isEmpty()) {
            System.out.println("No matches found.");
            return;
        }
        items.forEach(item -> System.out.println(item.basicSummary()));
    }
}
