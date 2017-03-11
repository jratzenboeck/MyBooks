package repository;

import entity.ReadingActivity;
import entity.ReadingDiaryEntry;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import util.CalendarUtils;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ReadingDiaryRepository implements CrudRepository {

    private final Connection connection;
    private final QueryRunner queryRunner;

    public ReadingDiaryRepository(Connection connection) {
        this.connection = connection;
        this.queryRunner = new QueryRunner();
    }

    public Optional<ReadingDiaryEntry> save(ReadingDiaryEntry diaryEntry) {
        final String sql = "insert into reading_diary_entry(activity_id, created_at, " +
                "entry) " +
                "values(?, ?, ?)";
        return (Optional<ReadingDiaryEntry>) save(diaryEntry, queryRunner, connection, sql,
                diaryEntry.getActivity().getId(), diaryEntry.getCreatedAt().getTime(),
                diaryEntry.getEntry());
    }

    public Optional<List<ReadingDiaryEntry>> findByBook(Long bookId) {
        final String sql = "select rde.id, rde.created_at, rde.activity_id, rde.entry " +
                "from reading_diary_entry as rde " +
                "inner join reading_activity ra on (rde.activity_id = ra.id) " +
                "where ra.book_id = ?";
        Optional<List<ReadingDiaryEntry>> optionalReadingDiary = Optional.empty();
        try {
            optionalReadingDiary = Optional.of(queryRunner.query(connection, sql, getListResultSetHandler(), bookId));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return optionalReadingDiary;
    }

    private ResultSetHandler<List<ReadingDiaryEntry>> getListResultSetHandler() {
        return (rs) -> {
            List<ReadingDiaryEntry> found = new ArrayList<>();
            while (rs.next()) {
                found.add(new ReadingDiaryEntry(rs.getLong("id"),
                        CalendarUtils
                        .parseCalendar(rs.getTimestamp("created_at"))
                        .orElse(null), rs.getString("entry"),
                        new ReadingActivity(rs.getLong("activity_id"))));
            }
            return found;
        };
    }
}
