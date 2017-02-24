package util;

import org.junit.Assert;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseUtilsTest {

    @Test
    public void testGetDatabaseConnectionProd() throws SQLException {
        Connection connection = DatabaseUtils.getDatabaseConnectionForProd();
        Assert.assertNotNull(connection);
    }

    @Test
    public void testGetDatabaseConnectionTest() throws SQLException {
        Connection connection = DatabaseUtils.getDatabaseConnectionForTest();
        Assert.assertNotNull(connection);
    }
}
