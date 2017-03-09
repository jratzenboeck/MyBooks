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
import java.util.List;
import java.util.Optional;

public class ReadingActivityRepositoryTest {

    private ReadingActivityRepository readingActivityRepository;
    private UserRepository userRepository;
    private BookRepository bookRepository;
    private AuthorRepository authorRepository;
    private Connection conn;

    @Before
    public void setUp() {
        conn = TestUtils.setUpDatabase();
        readingActivityRepository = new ReadingActivityRepository(conn);
        userRepository = new UserRepository(conn);
        bookRepository = new BookRepository(conn);
        authorRepository = new AuthorRepository(conn);
    }
    // TODO: Overthink .get() calls

    @Test
    public void testSave() {
        User user = userRepository.save(createTestUser()).get();

        Assert.assertTrue(readingActivityRepository.save(createReadingActivity(user, "Hello Startup")).isPresent());
    }

    @Test
    public void testEndReadingActivity() {
        User user = userRepository.save(createTestUser()).get();

        ReadingActivity readingActivity = createReadingActivity(user, "Java 8 in Action");
        readingActivity = readingActivityRepository.save(readingActivity).get();
        int updatedRows = readingActivityRepository.endReadingActivity(
                readingActivity.getId(), Calendar.getInstance());

        Assert.assertEquals(1, updatedRows);
    }

    @Test
    public void testGetCurrentReadingActivities() {
        User user = userRepository.save(createTestUser()).get();

        readingActivityRepository.save(createReadingActivity(user, "Hello Startup"));

        Optional<List<ReadingActivity>> activityList = readingActivityRepository.getCurrentReadingActivities(user.getId());
        if (activityList.isPresent()) {
            Assert.assertEquals(1, activityList.get().size());
        } else {
            Assert.fail("No Reading activities");
        }
    }

    @Test
    public void testGetCurrentReadingActivitiesEmpty() {
        User user = userRepository.save(createTestUser()).get();

        ReadingActivity activityToBeEnded = readingActivityRepository
                .save(createReadingActivity(user, "Java 8 in Action")).get();
        readingActivityRepository.endReadingActivity(activityToBeEnded.getId(), Calendar.getInstance());

        Assert.assertFalse(readingActivityRepository.getCurrentReadingActivities(user.getId()).get().size() != 0);
    }

    private User createTestUser() {
        return TestUtils.createTestUser("juergen", "pwd".toCharArray());
    }

    private ReadingActivity createReadingActivity(User user, String bookTitle) {
        Author author = authorRepository.save(TestUtils.createTestAuthor()).get();
        Book book = TestUtils.createTestBook(bookTitle);
        book.addAuthor(author);
        book = bookRepository.save(book).get();
        return new ReadingActivity(user, book);
    }

    @After
    public void tearDown() throws SQLException {
        conn.close();
    }
}
