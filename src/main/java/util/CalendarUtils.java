package util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

public class CalendarUtils {

    public static Optional<Calendar> parseCalendar(Date date) {
        if (date == null) {
            return Optional.empty();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return Optional.of(calendar);
    }

    public static Optional<Calendar> parseCalendar(String date) {
        if (date == null) {
            return Optional.empty();
        }
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
            calendar.set(1970, 0, 1);
        }
        return Optional.of(calendar);
    }

    public static String formatDate(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy hh:mm");
        return simpleDateFormat.format(date);
    }
}
