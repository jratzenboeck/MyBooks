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
        User user = createTestUser();
        Assert.assertNotNull(userRepository.save(user).getId());
    }

    @Test
    public void testSaveWithReadingInterests() {
        User user = createTestUser();
        user.addReadingInterest(new Category("Sports"));

        Assert.assertNotNull(userRepository.save(user).getId());
    }

    @Test
    public void testAuthenticate() {
        userRepository.save(createTestUser());

        User authenticated = userRepository.authenticate(ANY_USERNAME, ANY_PASSWORD);
        Assert.assertNotNull(authenticated);
    }

    @Test
    public void testAuthenticateFailWrongUsername() {
        userRepository.save(createTestUser());

        User authenticated = userRepository.authenticate("wrongUsername", ANY_PASSWORD);
        Assert.assertNull(authenticated);
    }

    @Test
    public void testAuthenticateFailWrongPassword() {
        userRepository.save(createTestUser());

        User authenticated = userRepository.authenticate(ANY_USERNAME, "wrongPassword".toCharArray());
        Assert.assertNull(authenticated);
    }

    @Test
    public void testAuthenticateFailWrongUsernameAndPassword() {
        userRepository.save(createTestUser());

        User authenticated = userRepository.authenticate("wrongUsername", "wrongPassword".toCharArray());
        Assert.assertNull(authenticated);
    }

    private User createTestUser() {
        return TestUtils.createTestUser(ANY_USERNAME, ANY_PASSWORD);
    }

    @After
    public void tearDown() throws SQLException {
        conn.close();
    }
}
