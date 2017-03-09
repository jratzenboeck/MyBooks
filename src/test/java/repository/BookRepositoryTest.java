package repository;

import entity.Author;
import entity.Book;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import util.TestUtils;

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class BookRepositoryTest {

    private BookRepository bookRepository;
    private AuthorRepository authorRepository;
    private Connection connection;

    private static final String ANY_AUTHOR_NAME = "Yevgeni Brikman";
    private static final String ANY_AUTHOR_NAME2 = "Martin Odersky";

    @Before
    public void setUp() throws FileNotFoundException, SQLException {
        connection = TestUtils.setUpDatabase();
        this.bookRepository = new BookRepository(connection);
        this.authorRepository = new AuthorRepository(connection);
    }

    @Test
    public void testSave() {
        Optional<Author> oAuthor = authorRepository.save(new Author(ANY_AUTHOR_NAME));
        if (oAuthor.isPresent()) {
            Book book = createTestBook();
            book.addAuthor(oAuthor.get());

            Assert.assertTrue(bookRepository.save(book).isPresent());
        } else {
            Assert.fail("Author could not be saved");
        }
    }

    @Test
    public void testSaveAuthorsForBook() {
        List<Author> authors = Arrays.asList(new Author(ANY_AUTHOR_NAME),
                new Author(ANY_AUTHOR_NAME2));
        authors.forEach(author -> authorRepository.save(author));

        Optional<Book> oBook = bookRepository.save(createTestBook());
        if (oBook.isPresent()) {
            Assert.assertEquals(2, bookRepository.saveAuthorsForBook(oBook.get(), authors));
        } else {
            Assert.fail("Book could not be saved");
        }
    }

    @Test
    public void testFind() {
        Optional<Book> oBook = bookRepository.save(createTestBook());

        if (oBook.isPresent()) {
            Assert.assertTrue(bookRepository.find(oBook.get().getTitle()).isPresent());
        } else {
            Assert.fail("Book could not be saved");
        }
    }

    @Test
    public void testFindFail() {
        Assert.assertFalse(bookRepository.find("TEST").isPresent());
    }

    private Book createTestBook() {
        return TestUtils.createTestBook("Hello Startup");
    }

    @After
    public void tearDown() throws SQLException {
        connection.close();
    }
}
