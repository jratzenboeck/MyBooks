package util;

import org.junit.Assert;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseConnectorTest {

    @Test
    public void testGetDatabaseConnectionProd() throws SQLException {
        Connection connection = DatabaseConnector.getDatabaseConnectionForProd();
        Assert.assertNotNull(connection);
    }

    @Test
    public void testGetDatabaseConnectionTest() throws SQLException {
        Connection connection = DatabaseConnector.getDatabaseConnectionForTest();
        Assert.assertNotNull(connection);
    }
}
