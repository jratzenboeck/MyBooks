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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        Assert.assertTrue(categoryRepository.save(category).isPresent());
    }

    @Test
    public void testGetReadingInterests() {
        Optional<Category> oSports = categoryRepository.save(new Category("Sports"));
        Optional<Category> oScience = categoryRepository.save(new Category("Science"));

        if (!oSports.isPresent() || !oScience.isPresent()) {
            Assert.fail("Categories could not be saved");
        } else {
            User user = TestUtils.createTestUser("juergen", "pwd".toCharArray());
            user.addReadingInterest(oSports.get());
            user.addReadingInterest(oScience.get());

            Optional<User> oUser = userRepository.save(user);
            if (!oUser.isPresent()) {
                Assert.fail("User could not be saved");
            } else {
                List<Category> readingInterests = categoryRepository.getReadingInterests(user.getId()).orElse(new ArrayList<>());

                Assert.assertEquals(2, readingInterests.size());
            }
        }
    }

    @Test
    public void testFindAll() {
        categoryRepository.save(new Category("Sports"));
        categoryRepository.save(new Category("Science"));

        Assert.assertEquals(2, categoryRepository.findAll().orElse(new ArrayList<>()).size());
    }

    @After
    public void tearDown() throws SQLException {
        conn.close();
    }
}
