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

public class UserRepositoryTest {

    private UserRepository userRepository;
    private Connection conn;

    private static final String ANY_USERNAME = "juergen";
    private static final char[] ANY_PASSWORD = "admin".toCharArray();

    @Before
    public void setUp() {
        conn = TestUtils.setUpDatabase();
        userRepository = new UserRepository(conn);
    }

    @Test
    public void testSave() {
        Assert.assertTrue(userRepository.save(createTestUser()).isPresent());
    }

    @Test
    public void testSaveWithReadingInterests() {
        User user = createTestUser();
        user.addReadingInterest(new Category("Sports"));

        Assert.assertTrue(userRepository.save(user).isPresent());
    }

    @Test
    public void testAuthenticate() {
        userRepository.save(createTestUser());

        Assert.assertTrue(userRepository.authenticate(ANY_USERNAME, ANY_PASSWORD).isPresent());
    }

    @Test
    public void testAuthenticateFailWrongUsername() {
        userRepository.save(createTestUser());

        Assert.assertFalse(userRepository.authenticate("wrongUsername", ANY_PASSWORD).isPresent());
    }

    @Test
    public void testAuthenticateFailWrongPassword() {
        userRepository.save(createTestUser());

        Assert.assertFalse(userRepository.authenticate(ANY_USERNAME, "wrongPassword".toCharArray()).isPresent());
    }

    @Test
    public void testAuthenticateFailWrongUsernameAndPassword() {
        userRepository.save(createTestUser());

        Assert.assertFalse(userRepository.authenticate("wrongUsername", "wrongPassword".toCharArray()).isPresent());
    }

    private User createTestUser() {
        return TestUtils.createTestUser(ANY_USERNAME, ANY_PASSWORD);
    }

    @After
    public void tearDown() throws SQLException {
        conn.close();
    }
}
