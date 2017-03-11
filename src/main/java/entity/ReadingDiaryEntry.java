package entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Calendar;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ReadingDiaryEntry extends BaseEntity {

    private final Calendar createdAt;
    private final String entry;
    private final ReadingActivity activity;

    public ReadingDiaryEntry(Long id, Calendar createdAt, String entry, ReadingActivity activity) {
        super(id);
        this.createdAt = createdAt;
        this.entry = entry;
        this.activity = activity;
    }

    public ReadingDiaryEntry(String entry, ReadingActivity activity) {
        this.createdAt = Calendar.getInstance();
        this.entry = entry;
        this.activity = activity;
    }
}
