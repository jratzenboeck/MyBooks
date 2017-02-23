package util;

import com.mysql.cj.jdbc.MysqlDataSource;
import org.h2.jdbcx.JdbcDataSource;

import javax.sql.DataSource;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.JDBCType;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class DatabaseConnector {

    private static final String PATH_TO_DB_CONFIG = "./db/config";

    private static Connection getDatabaseConnection(String profile) {
        DataSource dataSource;
        if ("test".equals(profile)) {
            dataSource = initializeH2DataSource();
        } else if ("prod".equals(profile)) {
            dataSource = initializeMySqlDataSource(getDatabaseSettings());
        } else {
            throw new RuntimeException("Profile " + profile + " not available.");
        }
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException("Connection could not be established");
        }
    }

    public static Connection getDatabaseConnectionForProd() {
        return getDatabaseConnection("prod");
    }

    public static Connection getDatabaseConnectionForTest() {
        return getDatabaseConnection("test");
    }

    private static DataSource initializeMySqlDataSource(Map<String, String> dbSettings) {
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setServerName(dbSettings.get("serverName"));
        dataSource.setPort(Integer.parseInt(dbSettings.get("port")));
        dataSource.setDatabaseName(dbSettings.get("database"));
        dataSource.setUser(dbSettings.get("user"));
        dataSource.setPassword(dbSettings.get("password"));
        return dataSource;
    }

    private static DataSource initializeH2DataSource() {
        JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setURL("jdbc:h2:mem:mybookstest;MODE=MYSQL");
        return dataSource;
    }

    private static Map<String, String> getDatabaseSettings() {
        Map<String, String> settings = new HashMap<>();

        try (Stream<String> lines = Files.lines(Paths.get(PATH_TO_DB_CONFIG))) {
            lines
                    .map(line -> line.split("="))
                    .filter(lineSplit -> lineSplit.length == 2)
                    .forEach(setting -> settings.put(setting[0], setting[1]));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return settings;
    }
}
