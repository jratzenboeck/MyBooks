package repository;

import entity.Category;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import util.TestUtils;

import java.sql.Connection;
import java.sql.SQLException;

public class CategoryRepositoryTest {

    private CategoryRepository categoryRepository;
    private Connection conn;

    @Before
    public void setUp() {
        conn = TestUtils.setUpDatabase();
        this.categoryRepository = new CategoryRepository(conn);
    }

    @Test
    public void testSave() {
        Category category = new Category("Sports");
        Assert.assertNotNull(categoryRepository.save(category).getId());
    }

    @After
    public void tearDown() throws SQLException {
        conn.close();
    }
}
