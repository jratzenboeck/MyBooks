package entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@EqualsAndHashCode(callSuper = true)
public class Book extends BaseEntity {

    private final String title;
    private final String languageIso2;
    private final int pageCount;
    private final float retailPrice;
    private final List<Author> authors;

    public Book(String title, String languageIso2, int pageCount, float retailPrice) {
        this.title = title;
        this.languageIso2 = languageIso2;
        this.pageCount = pageCount;
        this.retailPrice = retailPrice;
        this.authors = new ArrayList<>();
    }

    public Book(Long id, String title, String languageIso2, int pageCount, float retailPrice) {
        super(id);
        this.title = title;
        this.languageIso2 = languageIso2;
        this.pageCount = pageCount;
        this.retailPrice = retailPrice;
        this.authors = new ArrayList<>();
    }

    public void addAuthor(Author author) {
        authors.add(author);
    }

    public void addAuthors(List<Author> authors) { authors.addAll(authors); }

    @Override
    public String toString() {
        return "Title: " + title + "; Authors: " + getAuthorsFormatted();
    }

    private String getAuthorsFormatted() {
        return authors
                .stream()
                .map(Author::getName)
                .collect(Collectors.joining(", "));
    }

    public String getDetailsFormatted() {
        return "Book: " + title + "\n-------------------------------\n" +
                "Authors: " + getAuthorsFormatted() + "\n" +
                "Language: " + languageIso2 + "\n" +
                "Page Count: " + pageCount + "\n" +
                "Retail Price: " + retailPrice;
    }
}

