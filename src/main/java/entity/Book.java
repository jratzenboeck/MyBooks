package entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class Book extends BaseEntity {

    private final String title;
    private final String languageIso2;
    private final Calendar publishDate;
    private final Category category;
    private final int pageCount;
    private final float retailPrice;
    private final List<Author> authors;

    public Book(String title, String languageIso2, Calendar publishDate, Category category, int pageCount, float retailPrice) {
        this.title = title;
        this.languageIso2 = languageIso2;
        this.publishDate = publishDate;
        this.category = category;
        this.pageCount = pageCount;
        this.retailPrice = retailPrice;
        this.authors = new ArrayList<>();
    }

    public void addAuthor(Author author) {
        authors.add(author);
    }
}

