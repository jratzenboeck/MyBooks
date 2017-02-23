package repository;

import entity.Category;
import entity.User;
import entity.UserCredentials;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import util.PasswordHashing;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserRepository implements CrudRepository<User> {

    private final Connection connection;
    private final QueryRunner queryRunner;

    public UserRepository(Connection connection) {
        this.connection = connection;
        this.queryRunner = new QueryRunner();
    }

    public User save(User user) {
        final String sql = "insert into user(username, password, salt) " +
                "values(?, ?, ?)";
        user = (User) save(user, queryRunner, connection, sql, user.getUsername(),
                user.getPassword(), user.getSalt());
        saveReadingInterests(user, user.getReadingInterests());

        return user;
    }

    public int saveReadingInterests(User user, List<Category> readingInterests) {
        final String sql = "insert into reading_interest(category_id, user_id) values(?, ?)";

        return batchUpdate(readingInterests, sql, queryRunner, connection, user.getId());
    }

    public User authenticate(String username, String plainPassword) {
        final User user = findByUserName(username);

        if (user != null) {
            final byte[] hashedPassword = PasswordHashing.hashPassword(plainPassword.toCharArray(), user.getSalt());
            if (Arrays.equals(hashedPassword, user.getPassword())) {
                return user;
            }
        }
        return null;
    }

    private User findByUserName(String username) {
        final String sql = "select * from user where username = ?";

        final ResultSetHandler<User> resultSetHandler = (rs) -> {
            if (!rs.next()) {
                return null;
            }
            return new User(rs.getLong("id"),
                    rs.getString("username"),
                    new UserCredentials(rs.getBytes("password"),
                            rs.getBytes("salt")));
        };
        try {
            return queryRunner.query(connection, sql, resultSetHandler, username);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Category> getReadingInterests(Long userId) {
        final String sql = "select * from category " +
                "where category.id in (select reading_interest.category_id from reading_interest " +
                "where reading_interest.user_id = ?)";
        final ResultSetHandler<List<Category>> resultSetHandler = (rs) -> {
            List<Category> found = new ArrayList<>();
            while (rs.next()) {
                found.add(new Category(rs.getLong("id"),
                        rs.getString("name")));
            }
            return found;
        };

        try {
            return queryRunner.query(connection, sql, resultSetHandler, userId);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
