package repository;

import entity.*;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import util.TestUtils;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class ReadingDiaryRepositoryTest {

    private ReadingDiaryRepository readingDiaryRepository;
    private ReadingActivityRepository readingActivityRepository;
    private BookRepository bookRepository;
    private UserRepository userRepository;
    private Connection connection;

    @Before
    public void setUp() {
        connection = TestUtils.setUpDatabase();
        readingDiaryRepository = new ReadingDiaryRepository(connection);
        readingActivityRepository = new ReadingActivityRepository(connection);
        bookRepository = new BookRepository(connection);
        userRepository = new UserRepository(connection);
    }

    @Test
    public void testSave() {
        Optional<User> oUser = userRepository.save(TestUtils
                .createTestUser("test", "test".toCharArray()));
        Optional<Book> oBook = bookRepository.save(TestUtils.createTestBook("TestBook"));
        if (!oUser.isPresent() || !oBook.isPresent()) {
            Assert.fail("An entity could not be saved");
            return;
        }
        Optional<ReadingActivity> oActivity = readingActivityRepository
                .save(new ReadingActivity(oUser.get(), oBook.get()));
        if (!oActivity.isPresent()) {
            Assert.fail("Activity could not be saved");
            return;
        }
        ReadingDiaryEntry readingDiaryEntry = new
                ReadingDiaryEntry("Test", oActivity.get());

        Assert.assertTrue(readingDiaryRepository.save(readingDiaryEntry).isPresent());
    }

    @Test
    public void testFindByBook() {
        Optional<User> oUser = userRepository.save(TestUtils
                .createTestUser("test", "test".toCharArray()));
        Optional<Book> oBook = bookRepository.save(TestUtils.createTestBook("TestBook"));
        if (!oUser.isPresent() || !oBook.isPresent()) {
            Assert.fail("User or book could not be saved");
            return;
        }
        Optional<ReadingActivity> oActivity = readingActivityRepository
                .save(new ReadingActivity(oUser.get(), oBook.get()));
        Optional<ReadingDiaryEntry> oDiaryEntry = readingDiaryRepository.save(new
                ReadingDiaryEntry("Test", oActivity.get()));

        if (!oActivity.isPresent() || !oDiaryEntry.isPresent()) {
            Assert.fail("Activity or diary entry could not be saved");
            return;
        }

        Optional<List<ReadingDiaryEntry>> oDiary = readingDiaryRepository
                .findByBook(oBook.get().getId());
        if (!oDiary.isPresent()) {
            Assert.fail("No reading diary fetched");
            return;
        }
        Assert.assertEquals(1, oDiary.get().size());
    }

    @After
    public void tearDown() throws SQLException {
        connection.close();
    }
}