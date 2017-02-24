package repository;

import entity.Category;
import entity.User;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import util.TestUtils;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class CategoryRepositoryTest {

    private CategoryRepository categoryRepository;
    private UserRepository userRepository;
    private Connection conn;

    @Before
    public void setUp() {
        conn = TestUtils.setUpDatabase();
        this.categoryRepository = new CategoryRepository(conn);
        this.userRepository = new UserRepository(conn);
    }

    @Test
    public void testSave() {
        Category category = new Category("Sports");
        Assert.assertNotNull(categoryRepository.save(category).getId());
    }

    @Test
    public void testGetReadingInterests() {
        Category sports = categoryRepository.save(new Category("Sports"));
        Category science = categoryRepository.save(new Category("Science"));

        User user = TestUtils.createTestUser("juergen", "pwd".toCharArray());
        user.addReadingInterest(sports);
        user.addReadingInterest(science);

        user = userRepository.save(user);

        List<Category> readingInterests = categoryRepository.getReadingInterests(user.getId());

        Assert.assertNotNull(readingInterests);
        Assert.assertEquals(2, readingInterests.size());
    }

    @Test
    public void testFindAll() {
        categoryRepository.save(new Category("Sports"));
        categoryRepository.save(new Category("Science"));

        Assert.assertEquals(2, categoryRepository.findAll().size());
    }

    @After
    public void tearDown() throws SQLException {
        conn.close();
    }
}
