package repository;

import entity.Book;
import entity.ReadingActivity;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import util.CalendarUtils;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

public class ReadingActivityRepository implements CrudRepository {

    private final Connection connection;
    private final QueryRunner queryRunner;

    public ReadingActivityRepository(Connection connection) {
        this.connection = connection;
        this.queryRunner = new QueryRunner();
    }

    public Optional<ReadingActivity> save(ReadingActivity readingActivity) {
        final String sql = "insert into reading_activity(user_id, book_id, " +
                "start_reading) " +
                "values(?, ?, ?)";
        return (Optional<ReadingActivity>) save(readingActivity, queryRunner, connection, sql,
                readingActivity.getUser().getId(), readingActivity.getBook().getId(),
                Calendar.getInstance().getTime());
    }

    public Optional<List<ReadingActivity>> getCurrentReadingActivities(Long userId) {
        final String sql = "select r.start_reading as start, r.end_reading as end, " +
                "r.bookmark as bookmark, b.title as title, " +
                "b.language_iso2 as iso2, b.pageCount as pageCount, " +
                "b.retailPrice as price, a.id as authorId " +
                "from reading_activity r " +
                "inner join book b on (r.book_id = b.id) " +
                "inner join author_book ab on (b.id = ab.book_id) " +
                "inner join author a on (ab.author_id = a.id) " +
                "where user_id = ? and end_reading is NULL";

        Optional<List<ReadingActivity>> optionalReadingActivities = Optional.empty();

        try {
            final ResultSetHandler<List<ReadingActivity>> resultSetHandler = (rs) -> {
                List<ReadingActivity> readingActivities = new ArrayList<>();
                while (rs.next()) {
                    Book book = new Book(rs.getString("title"),
                            rs.getString("iso2"),
                            rs.getInt("pageCount"),
                            rs.getFloat("price"));
                    ReadingActivity activity = new ReadingActivity(book,
                            CalendarUtils.parseCalendar(rs.getDate("start")).orElse(null),
                            CalendarUtils.parseCalendar(rs.getDate("end")).orElse(null),
                            rs.getInt("bookmark"));
                    readingActivities.add(activity);
                }
                return readingActivities;
            };

            optionalReadingActivities = Optional.of(queryRunner.query(connection, sql, resultSetHandler, userId));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return optionalReadingActivities;
    }

    public int endReadingActivity(Long id, Calendar endDate) {
        final String sql = "update reading_activity set end_reading = ? where id = ?";

        try {
            return queryRunner.update(connection, sql, endDate.getTime(), id);
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }
}
