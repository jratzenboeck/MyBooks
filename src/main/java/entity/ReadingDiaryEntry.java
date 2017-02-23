package entity;

import lombok.Data;

import java.util.Calendar;

@Data
public class ReadingDiaryEntry extends BaseEntity {

    private final Calendar createdAt;
    private final String entry;
}
