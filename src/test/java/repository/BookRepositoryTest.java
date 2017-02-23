package repository;

import entity.Author;
import entity.Book;
import entity.Category;
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

public class BookRepositoryTest {

    private BookRepository bookRepository;
    private CategoryRepository categoryRepository;
    private AuthorRepository authorRepository;
    private Connection connection;

    private static final String ANY_AUTHOR_NAME = "Yevgeni Brikman";
    private static final String ANY_AUTHOR_NAME2 = "Martin Odersky";

    @Before
    public void setUp() throws FileNotFoundException, SQLException {
        connection = TestUtils.setUpDatabase();
        this.bookRepository = new BookRepository(connection);
        this.categoryRepository = new CategoryRepository(connection);
        this.authorRepository = new AuthorRepository(connection);
    }

    @Test
    public void testSave() {
        Author author = authorRepository.save(new Author(ANY_AUTHOR_NAME));

        Book book = createTestBook();
        book.addAuthor(author);

        Assert.assertNotNull(bookRepository.save(book).getId());
    }

    @Test
    public void testSaveAuthorsForBook() {
        List<Author> authors = Arrays.asList(new Author(ANY_AUTHOR_NAME),
                new Author(ANY_AUTHOR_NAME2));
        authors.forEach(author -> authorRepository.save(author));

        Book book = bookRepository.save(createTestBook());

        Assert.assertEquals(2, bookRepository.saveAuthorsForBook(book, authors));
    }

    private Book createTestBook() {
        Category category = categoryRepository.save(new Category("Science"));

        return TestUtils.createTestBook(category, "Hello Startup");
    }

    @After
    public void tearDown() throws SQLException {
        connection.close();
    }
}
