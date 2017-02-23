package entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class ReadingActivity extends BaseEntity {

    private User user;
    private final Book book;
    private Calendar startReading;
    private Calendar endReading;
    private int bookmark;
    private List<ReadingDiaryEntry> readingDiary;

    public ReadingActivity(User user, Book book, Calendar startReading, Calendar endReading, int bookmark) {
        this.user = user;
        this.book = book;
        this.startReading = startReading;
        this.endReading = endReading;
        this.bookmark = bookmark;
        this.readingDiary = new ArrayList<>();
    }

    public ReadingActivity(Book book, Calendar startReading, Calendar endReading, int bookmark) {
        this.book = book;
        this.startReading = startReading;
        this.endReading = endReading;
        this.bookmark = bookmark;
    }

    public ReadingActivity(User user, Book book) {
        this.user = user;
        this.book = book;
    }
}
