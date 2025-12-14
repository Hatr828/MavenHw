package org.brainacad.library;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import org.junit.Test;

public class LibraryCatalogTest {

    @Test
    public void sampleInitializationContainsExpectedItems() {
        LibraryCatalog catalog = LibraryCatalog.withSampleData();

        List<CatalogItem> items = catalog.listAll();
        assertEquals(4, items.size());
        assertTrue(items.stream().anyMatch(Book.class::isInstance));
        assertTrue(items.stream().anyMatch(Newspaper.class::isInstance));
        assertTrue(items.stream().anyMatch(Almanac.class::isInstance));
    }

    @Test
    public void addItemAppendsToCatalog() {
        LibraryCatalog catalog = new LibraryCatalog();
        Book book = Book.builder()
                .title("JUnit in Action")
                .author("Someone")
                .genre("Tech")
                .pageCount(250)
                .annotation("Testing guide")
                .publicationDate(LocalDate.of(2020, 1, 1))
                .build();

        catalog.addItem(book);
        assertEquals(1, catalog.listAll().size());
        assertSame(book, catalog.listAll().get(0));
    }

    @Test
    public void addRandomItemUsesGeneratorAndIncreasesSize() {
        LibraryCatalog catalog = new LibraryCatalog(new Random(42));

        CatalogItem generated = catalog.addRandomItem();

        assertNotNull(generated);
        assertEquals(1, catalog.listAll().size());
        assertNotNull(generated.getItemType());
        assertTrue(generated.getTitle() != null && !generated.getTitle().trim().isEmpty());
    }

    @Test
    public void removeByTitleDeletesMatchingItems() {
        LibraryCatalog catalog = LibraryCatalog.withSampleData();

        boolean removed = catalog.removeByTitle("City Times");

        assertTrue(removed);
        assertEquals(3, catalog.listAll().size());
        assertTrue(catalog.searchBooksOrNewspapersByTitle("City Times").isEmpty());
    }

    @Test
    public void searchBooksOrNewspapersByTitleFindsMatches() {
        LibraryCatalog catalog = LibraryCatalog.withSampleData();

        List<CatalogItem> results = catalog.searchBooksOrNewspapersByTitle("library");

        assertEquals(1, results.size());
        assertEquals("The Silent Library", results.get(0).getTitle());
    }

    @Test
    public void searchByAuthorHandlesBooksAndAlmanacWorks() {
        LibraryCatalog catalog = LibraryCatalog.withSampleData();

        List<CatalogItem> christieResults = catalog.searchByAuthor("Christie");
        List<CatalogItem> murakamiResults = catalog.searchByAuthor("Murakami");

        assertEquals(1, christieResults.size());
        assertEquals("The Silent Library", christieResults.get(0).getTitle());

        assertEquals(1, murakamiResults.size());
        assertEquals(ItemType.ALMANAC, murakamiResults.get(0).getItemType());
    }

    @Test
    public void searchBooksByKeywordsMatchesAllProvidedWords() {
        LibraryCatalog catalog = LibraryCatalog.withSampleData();

        List<Book> results = catalog.searchBooksByKeywords(Arrays.asList("detective", "shelves"));

        assertEquals(1, results.size());
        assertEquals("The Silent Library", results.get(0).getTitle());
    }

    @Test
    public void searchByPagesGenreOrAuthorFiltersByGenre() {
        LibraryCatalog catalog = LibraryCatalog.withSampleData();

        List<CatalogItem> results = catalog.searchByPagesGenreOrAuthor(350, "mystery", null);

        assertEquals(1, results.size());
        assertEquals("The Silent Library", results.get(0).getTitle());
    }

    @Test
    public void searchByPagesGenreOrAuthorFiltersByAuthorWithinAlmanac() {
        LibraryCatalog catalog = LibraryCatalog.withSampleData();

        List<CatalogItem> results = catalog.searchByPagesGenreOrAuthor(600, null, "Murakami");

        assertEquals(1, results.size());
        assertEquals(ItemType.ALMANAC, results.get(0).getItemType());
    }

    @Test
    public void searchNewspapersByHeadlineFindsPartialMatches() {
        LibraryCatalog catalog = LibraryCatalog.withSampleData();

        List<Newspaper> results = catalog.searchNewspapersByHeadline("marathon");

        assertEquals(1, results.size());
        assertEquals("City Times", results.get(0).getTitle());
    }

    @Test
    public void formattedCatalogListsAllEntries() {
        LibraryCatalog catalog = LibraryCatalog.withSampleData();

        String formatted = catalog.formattedCatalog();

        assertTrue(formatted.contains("Book: The Silent Library"));
        assertTrue(formatted.contains("Almanac: Hidden Horizons Anthology"));
        assertTrue(formatted.contains("Newspaper: City Times"));
    }
}
