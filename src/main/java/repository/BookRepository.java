package repository;

import entity.Author;
import entity.Book;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class BookRepository implements CrudRepository {

    private final Connection connection;
    private final QueryRunner queryRunner;

    public BookRepository(Connection connection) {
        this.connection = connection;
        this.queryRunner = new QueryRunner();
    }

    public Optional<Book> save(Book book) {
        final String sql = "insert into book(title, language_iso2, pageCount, retailPrice)" +
                "values(?, ?, ?, ?)";

        Optional<Book> bookOptional = (Optional<Book>) save(book, queryRunner, connection, sql,
                book.getTitle(), book.getLanguageIso2(),
                book.getPageCount(), book.getRetailPrice());
        bookOptional.ifPresent(b -> saveAuthorsForBook(book, book.getAuthors()));

        return bookOptional;
    }

    int saveAuthorsForBook(Book book, List<Author> authors) {
        final String sql = "insert into author_book(author_id, book_id) " +
                "values(?, ?)";

        return batchUpdate(authors, sql, queryRunner, connection, book.getId());
    }

    public Optional<Book> find(String title) {
        final String sql = "select * from book where title = ?";
        Optional<Book> optionalBook = Optional.empty();

        try {
            final ResultSetHandler<Book> resultSetHandler = (rs) -> {
                if (!rs.next()) {
                    return null;
                }
                return new Book(rs.getLong("id"),
                        rs.getString("title"),
                        rs.getString("language_iso2"),
                        rs.getInt("pageCount"),
                        rs.getFloat("retailPrice"));
            };
            optionalBook = Optional.ofNullable(queryRunner.query(connection, sql, resultSetHandler, title));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return optionalBook;
    }
}
