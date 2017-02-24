package repository;

import entity.Author;
import entity.Book;
import entity.Category;
import org.apache.commons.dbutils.QueryRunner;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class BookRepository implements CrudRepository {

    private final Connection connection;
    private final QueryRunner queryRunner;

    public BookRepository(Connection connection) {
        this.connection = connection;
        this.queryRunner = new QueryRunner();
    }

    public Book save(Book book) {
        final String sql = "insert into book(title, publish_date, language_iso2, pageCount, retailPrice, category_id)" +
                "values(?, ?, ?, ?, ?, ?)";
        book = (Book) save(book, queryRunner, connection, sql,
                book.getTitle(), book.getPublishDate().getTime(), book.getLanguageIso2(),
                book.getPageCount(), book.getRetailPrice(), book.getCategory().getId());
        saveAuthorsForBook(book, book.getAuthors());

        return book;
    }

    int saveAuthorsForBook(Book book, List<Author> authors) {
        final String sql = "insert into author_book(author_id, book_id) " +
                "values(?, ?)";

        return batchUpdate(authors, sql, queryRunner, connection, book.getId());
    }
}
