package repository;

import entity.Author;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import util.TestUtils;

import java.sql.Connection;
import java.sql.SQLException;

public class AuthorRepositoryTest {

    private Connection connection;
    private AuthorRepository authorRepository;

    private static final String ANY_NAME = "Yevgeni Brikman";

    @Before
    public void setUp() {
        connection = TestUtils.setUpDatabase();
        authorRepository = new AuthorRepository(connection);
    }

    @Test
    public void testSave() {
        Author author = new Author(ANY_NAME);

        Assert.assertTrue(authorRepository.save(author).isPresent());
    }

    @Test
    public void testFind() {
        authorRepository.save(new Author(ANY_NAME))
                .ifPresent(savedAuthor -> Assert.assertTrue(authorRepository.find(savedAuthor.getName()).isPresent()));
    }

    @Test
    public void testFindFail() {
        Assert.assertFalse(authorRepository.find("TEST").isPresent());
    }

    @After
    public void tearDown() throws SQLException {
        connection.close();
    }
}
