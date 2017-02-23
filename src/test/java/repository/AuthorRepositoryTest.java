package repository;

import entity.Author;
import org.apache.commons.dbutils.QueryRunner;
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

        Assert.assertNotNull(authorRepository.save(author).getId());
    }

    @Test
    public void testFind() {
        Long id = authorRepository.save(new Author(ANY_NAME)).getId();

        Assert.assertNotNull(authorRepository.find(id));
    }

    @After
    public void tearDown() throws SQLException {
        connection.close();
    }
}
