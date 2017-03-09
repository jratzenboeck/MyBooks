package util;

import entity.*;
import org.h2.tools.RunScript;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class TestUtils {

    private static final String CREATE_SCRIPT = "./src/test/resources/scripts/create.sql";

    public static Connection setUpDatabase() {
        final Connection conn;
        try {
            conn = DatabaseUtils.getDatabaseConnectionForTest();
            RunScript.execute(conn, new FileReader(CREATE_SCRIPT));
        } catch (SQLException | FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return conn;
    }

    public static User createTestUser(String username, char[] password) {
        byte[] salt = PasswordHashing.generateSalt();
        UserCredentials credentials = new UserCredentials(
                PasswordHashing.hashPassword(password, salt), salt);

        User user = new User(username, credentials);

        return user;
    }

    public static Book createTestBook(String title) {
        return new Book(title, "en",
                800, 39.90f);
    }

    public static Author createTestAuthor() {
        return new Author("Martin Odersky");
    }
}
