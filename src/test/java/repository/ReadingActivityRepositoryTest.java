package repository;

import entity.*;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import util.TestUtils;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Calendar;

public class ReadingActivityRepositoryTest {

    private ReadingActivityRepository readingActivityRepository;
    private UserRepository userRepository;
    private BookRepository bookRepository;
    private CategoryRepository categoryRepository;
    private AuthorRepository authorRepository;
    private Connection conn;

    @Before
    public void setUp() {
        conn = TestUtils.setUpDatabase();
        readingActivityRepository = new ReadingActivityRepository(conn);
        userRepository = new UserRepository(conn);
        bookRepository = new BookRepository(conn);
        categoryRepository = new CategoryRepository(conn);
        authorRepository = new AuthorRepository(conn);
    }

    @Test
    public void testSave() {
        User user = userRepository.save(createTestUser());

        Assert.assertNotNull(readingActivityRepository.save(createReadingActivity(user, "Hello Startup")).getId());
    }

    @Test
    public void testEndReadingActivity() {
        User user = userRepository.save(createTestUser());

        ReadingActivity readingActivity = createReadingActivity(user, "Java 8 in Action");
        readingActivity = readingActivityRepository.save(readingActivity);
        int updatedRows = readingActivityRepository.endReadingActivity(
                readingActivity.getId(), Calendar.getInstance());

        Assert.assertEquals(1, updatedRows);
    }

    @Test
    public void testGetCurrentReadingActivities() {
        User user = userRepository.save(createTestUser());

        readingActivityRepository.save(createReadingActivity(user, "Hello Startup"));

        Assert.assertEquals(1, readingActivityRepository.getCurrentReadingActivities(user.getId()).size());
    }

    @Test
    public void testGetCurrentReadingActivitiesEmpty() {
        User user = userRepository.save(createTestUser());

        ReadingActivity activityToBeEnded = readingActivityRepository.save(createReadingActivity(user, "Java 8 in Action"));
        readingActivityRepository.endReadingActivity(activityToBeEnded.getId(), Calendar.getInstance());

        Assert.assertEquals(0, readingActivityRepository.getCurrentReadingActivities(user.getId()).size());
    }

    private User createTestUser() {
        return TestUtils.createTestUser("juergen", "pwd");
    }

    private ReadingActivity createReadingActivity(User user, String bookTitle) {
        Category category = categoryRepository.save(TestUtils.createTestCategory());
        Author author = authorRepository.save(TestUtils.createTestAuthor());
        Book book = TestUtils.createTestBook(category, bookTitle);
        book.addAuthor(author);
        book = bookRepository.save(book);
        ReadingActivity readingActivity = new ReadingActivity(user, book);

        return readingActivity;
    }

    @After
    public void tearDown() throws SQLException {
        conn.close();
    }
}
