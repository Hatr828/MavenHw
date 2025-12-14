package org.brainacad.library;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class LibraryCatalog {
    private final List<CatalogItem> items = new ArrayList<>();
    private final Random random;

    public LibraryCatalog() {
        this(ThreadLocalRandom.current());
    }

    public LibraryCatalog(Random random) {
        this.random = random;
    }

    public static LibraryCatalog withSampleData() {
        LibraryCatalog catalog = new LibraryCatalog();

        Book bookOne = Book.builder()
                .title("The Silent Library")
                .author("Agatha Christie")
                .genre("Mystery")
                .pageCount(320)
                .annotation("Detective unravels secrets hidden between the shelves.")
                .publicationDate(LocalDate.of(1998, 5, 12))
                .build();

        Book bookTwo = Book.builder()
                .title("Starlit Voyage")
                .author("Ursula Le Guin")
                .genre("Science Fiction")
                .pageCount(410)
                .annotation("Crew seeks a new home beyond the known galaxies.")
                .publicationDate(LocalDate.of(2002, 11, 3))
                .build();

        Almanac almanac = Almanac.builder()
                .title("Hidden Horizons Anthology")
                .annotation("Collection of genre-bending novellas.")
                .focus("Showcases unique voices across speculative fiction.")
                .pageCount(540)
                .publicationDate(LocalDate.of(2010, 3, 18))
                .works(Arrays.asList(
                        Book.builder()
                                .title("Night Market")
                                .author("Haruki Murakami")
                                .genre("Magical Realism")
                                .pageCount(190)
                                .annotation("Journeys between dreams and bustling streets.")
                                .publicationDate(LocalDate.of(2005, 8, 9))
                                .build(),
                        Book.builder()
                                .title("Clockmaker's Dilemma")
                                .author("Terry Pratchett")
                                .genre("Fantasy")
                                .pageCount(240)
                                .annotation("A clock that may freeze time threatens a city.")
                                .publicationDate(LocalDate.of(2007, 2, 21))
                                .build()))
                .build();

        Newspaper newspaper = Newspaper.builder()
                .title("City Times")
                .issueDate(LocalDate.of(2024, 4, 7))
                .headlines(Arrays.asList(
                        "City marathon attracts record number of runners",
                        "Local author tops bestseller list",
                        "New library wing opens to the public"))
                .build();

        catalog.addItem(bookOne);
        catalog.addItem(bookTwo);
        catalog.addItem(almanac);
        catalog.addItem(newspaper);

        return catalog;
    }

    public List<CatalogItem> listAll() {
        return new ArrayList<>(items);
    }

    public void addItem(CatalogItem item) {
        if (item != null) {
            items.add(item);
        }
    }

    public CatalogItem addRandomItem() {
        CatalogItem generated = generateRandomItem();
        items.add(generated);
        return generated;
    }

    public boolean removeByTitle(String title) {
        String normalized = normalize(title);
        if (!hasText(normalized)) {
            return false;
        }
        int before = items.size();
        items.removeIf(item -> normalize(item.getTitle()).equals(normalized));
        return items.size() < before;
    }

    public List<CatalogItem> searchBooksOrNewspapersByTitle(String query) {
        String normalized = normalize(query);
        if (!hasText(normalized)) {
            return Collections.emptyList();
        }
        return items.stream()
                .filter(item -> item instanceof Book || item instanceof Newspaper)
                .filter(item -> contains(item.getTitle(), normalized))
                .collect(Collectors.toList());
    }

    public List<CatalogItem> searchByAuthor(String author) {
        String normalized = normalize(author);
        if (!hasText(normalized)) {
            return Collections.emptyList();
        }
        return items.stream()
                .filter(item -> matchesAuthor(item, normalized))
                .collect(Collectors.toList());
    }

    public List<Book> searchBooksByKeywords(List<String> keywords) {
        List<String> normalized = keywords == null
                ? Collections.emptyList()
                : keywords.stream()
                .map(this::normalize)
                .filter(this::hasText)
                .collect(Collectors.toList());

        if (normalized.isEmpty()) {
            return Collections.emptyList();
        }

        return items.stream()
                .filter(Book.class::isInstance)
                .map(Book.class::cast)
                .filter(book -> containsAllKeywords(book.getAnnotation(), normalized))
                .collect(Collectors.toList());
    }

    public List<CatalogItem> searchByPagesGenreOrAuthor(int maxPages, String genre, String author) {
        String normalizedGenre = normalize(genre);
        String normalizedAuthor = normalize(author);

        return items.stream()
                .filter(item -> item instanceof Book || item instanceof Almanac)
                .filter(item -> getPageCount(item) <= maxPages)
                .filter(item -> matchesGenreOrAuthor(item, normalizedGenre, normalizedAuthor))
                .collect(Collectors.toList());
    }

    public List<Newspaper> searchNewspapersByHeadline(String headlineFragment) {
        String normalized = normalize(headlineFragment);
        if (!hasText(normalized)) {
            return Collections.emptyList();
        }

        return items.stream()
                .filter(Newspaper.class::isInstance)
                .map(Newspaper.class::cast)
                .filter(newspaper -> newspaper.getHeadlines().stream()
                        .anyMatch(headline -> contains(headline, normalized)))
                .collect(Collectors.toList());
    }

    public String formattedCatalog() {
        if (items.isEmpty()) {
            return "Catalog is empty.";
        }
        return items.stream()
                .map(this::formatItem)
                .collect(Collectors.joining(System.lineSeparator()));
    }

    private CatalogItem generateRandomItem() {
        ItemType type = ItemType.values()[random.nextInt(ItemType.values().length)];
        switch (type) {
            case BOOK:
                return randomBook();
            case NEWSPAPER:
                return randomNewspaper();
            case ALMANAC:
            default:
                return randomAlmanac();
        }
    }

    private Book randomBook() {
        String title = randomBookTitle() + " #" + (random.nextInt(80) + 1);
        String author = randomAuthor();
        String genre = randomGenre();
        int pages = 150 + random.nextInt(450);
        String annotation = randomAnnotation();

        return Book.builder()
                .title(title)
                .author(author)
                .genre(genre)
                .pageCount(pages)
                .annotation(annotation)
                .publicationDate(randomDate())
                .build();
    }

    private Newspaper randomNewspaper() {
        String title = randomNewspaperTitle();
        List<String> headlines = Stream.generate(this::randomHeadline)
                .distinct()
                .limit(3)
                .collect(Collectors.toList());
        return Newspaper.builder()
                .title(title)
                .issueDate(randomDate())
                .headlines(headlines)
                .build();
    }

    private Almanac randomAlmanac() {
        List<Book> works = IntStream.range(0, 2 + random.nextInt(3))
                .mapToObj(i -> randomBook())
                .collect(Collectors.toList());

        int totalPages = works.stream().mapToInt(Book::getPageCount).sum() + 20;

        return Almanac.builder()
                .title(randomAlmanacTitle() + " Almanac")
                .annotation(randomAnnotation())
                .focus(randomAlmanacFocus())
                .pageCount(totalPages)
                .publicationDate(randomDate())
                .works(works)
                .build();
    }

    private String randomBookTitle() {
        String[] starters = {"Shadows", "Whispering", "Silent", "Clockwork", "Aurora", "Glass", "Edge", "Northern",
                "Invisible", "Pattern", "Voyage", "Hidden", "Starlit"};
        String[] endings = {"of the Valley", "Pages", "Witness", "Dusk", "Dreams", "Gardens", "Memory",
                "Lights", "Cities", "Seeker", "Beyond", "Horizons", "Paths"};
        return pick(starters) + " " + pick(endings);
    }

    private String randomAuthor() {
        String[] first = {"Ursula", "Neil", "Terry", "Stephen", "Agatha", "Haruki", "Octavia", "Fredrik", "Donna", "Toni"};
        String[] last = {"Le Guin", "Gaiman", "Pratchett", "King", "Christie", "Murakami", "Butler", "Backman", "Tartt", "Morrison"};
        return pick(first) + " " + pick(last);
    }

    private String randomGenre() {
        return pick("Fantasy", "Science Fiction", "Detective", "Thriller", "Mystery", "Historical Fiction");
    }

    private String randomAnnotation() {
        String[] notes = {
                "A quiet town hides secrets that surface when a stranger arrives.",
                "An unlikely duo pieces together clues left in forgotten letters.",
                "A journey across continents reshapes the fate of two families.",
                "An inventor races against time to stop a runaway creation.",
                "Chronicles of dreamers who reshape reality with their words."
        };
        return pick(notes);
    }

    private String randomNewspaperTitle() {
        return pick("Daily Chronicle", "Evening Herald", "City Times", "Morning Post", "Weekly Observer");
    }

    private String randomHeadline() {
        String[] headlines = {
                "Local community garden wins national prize",
                "New art district opens downtown",
                "Historic bridge restoration completed",
                "Scientists announce breakthrough in clean energy",
                "Youth orchestra prepares for European tour",
                "Technology summit highlights cybersecurity trends",
                "City marathon attracts record number of runners",
                "Libraries extend weekend hours to meet demand"
        };
        return pick(headlines);
    }

    private String randomAlmanacTitle() {
        return pick("Voices of Tomorrow", "Ink and Stardust", "Hidden Horizons", "Paths and Portals");
    }

    private String randomAlmanacFocus() {
        return pick(
                "Modern speculative fiction",
                "Urban fantasy anthology",
                "Emerging mystery writers",
                "Stories about resilience and hope",
                "Short fiction celebrating curiosity");
    }

    private boolean matchesAuthor(CatalogItem item, String normalizedAuthor) {
        if (!hasText(normalizedAuthor)) {
            return false;
        }
        if (item instanceof Book) {
            Book book = (Book) item;
            return contains(book.getAuthor(), normalizedAuthor);
        }
        if (item instanceof Almanac) {
            Almanac almanac = (Almanac) item;
            return almanac.getWorks().stream()
                    .anyMatch(work -> contains(work.getAuthor(), normalizedAuthor));
        }
        return false;
    }

    private boolean matchesGenreOrAuthor(CatalogItem item, String genre, String author) {
        if (!hasText(genre) && !hasText(author)) {
            return true;
        }

        if (item instanceof Book) {
            Book book = (Book) item;
            boolean genreMatch = hasText(genre) && contains(book.getGenre(), genre);
            boolean authorMatch = hasText(author) && contains(book.getAuthor(), author);
            return genreMatch || authorMatch;
        }

        if (item instanceof Almanac) {
            Almanac almanac = (Almanac) item;
            return almanac.getWorks().stream().anyMatch(work -> {
                boolean genreMatch = hasText(genre) && contains(work.getGenre(), genre);
                boolean authorMatch = hasText(author) && contains(work.getAuthor(), author);
                return genreMatch || authorMatch;
            });
        }

        return false;
    }

    private int getPageCount(CatalogItem item) {
        if (item instanceof Book) {
            return ((Book) item).getPageCount();
        }
        if (item instanceof Almanac) {
            return ((Almanac) item).getPageCount();
        }
        return Integer.MAX_VALUE;
    }

    private boolean containsAllKeywords(String source, List<String> keywords) {
        if (source == null) {
            return false;
        }
        String normalizedSource = source.toLowerCase(Locale.ROOT);
        return keywords.stream().allMatch(normalizedSource::contains);
    }

    private boolean contains(String source, String needle) {
        return hasText(source) && hasText(needle) && source.toLowerCase(Locale.ROOT).contains(needle);
    }

    private boolean hasText(String value) {
        return value != null && !value.trim().isEmpty();
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim().toLowerCase(Locale.ROOT);
    }

    private String pick(String... values) {
        return values[random.nextInt(values.length)];
    }

    private LocalDate randomDate() {
        int year = 1990 + random.nextInt(35);
        int month = 1 + random.nextInt(12);
        int day = 1 + random.nextInt(28);
        return LocalDate.of(year, month, day);
    }

    private String formatItem(CatalogItem item) {
        if (item instanceof Book) {
            return formatBook((Book) item);
        }
        if (item instanceof Newspaper) {
            return formatNewspaper((Newspaper) item);
        }
        if (item instanceof Almanac) {
            return formatAlmanac((Almanac) item);
        }
        return item.basicSummary();
    }

    private String formatBook(Book book) {
        return "Book: " + book.getTitle()
                + " by " + book.getAuthor()
                + " | Genre: " + book.getGenre()
                + " | Pages: " + book.getPageCount()
                + " | Published: " + book.getPublicationDate();
    }

    private String formatNewspaper(Newspaper newspaper) {
        String headlines = String.join("; ", newspaper.getHeadlines());
        return "Newspaper: " + newspaper.getTitle()
                + " | Issue: " + newspaper.getPublicationDate()
                + " | Headlines: " + headlines;
    }

    private String formatAlmanac(Almanac almanac) {
        String works = almanac.getWorks().stream()
                .map(Book::getTitle)
                .collect(Collectors.joining(", "));
        return "Almanac: " + almanac.getTitle()
                + " | Focus: " + almanac.getFocus()
                + " | Pages: " + almanac.getPageCount()
                + " | Published: " + almanac.getPublicationDate()
                + " | Works: " + works;
    }
}
