package repository;

import entity.Category;
import entity.User;
import entity.UserCredentials;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import util.PasswordHashing;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class UserRepository implements CrudRepository {

    private final Connection connection;
    private final QueryRunner queryRunner;

    public UserRepository(Connection connection) {
        this.connection = connection;
        this.queryRunner = new QueryRunner();
    }

    public Optional<User> save(User user) {
        final String sql = "insert into user(username, password, salt) " +
                "values(?, ?, ?)";

        Optional<User> optionalUser = (Optional<User>) save(user, queryRunner, connection, sql, user.getUsername(),
                user.getPassword(), user.getSalt());
        optionalUser.ifPresent(u -> saveReadingInterests(user, user.getReadingInterests()));

        return optionalUser;
    }

    private int saveReadingInterests(User user, List<Category> readingInterests) {
        final String sql = "insert into reading_interest(category_id, user_id) values(?, ?)";

        return batchUpdate(readingInterests, sql, queryRunner, connection, user.getId());
    }

    public Optional<User> authenticate(String username, char[] plainPassword) {
        final Optional<User> optionalUser = findByUserName(username);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            final byte[] hashedPassword = PasswordHashing.hashPassword(plainPassword, user.getSalt());
            if (Arrays.equals(hashedPassword, user.getPassword())) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    private Optional<User> findByUserName(String username) {
        final String sql = "select * from user where username = ?";
        Optional<User> user = Optional.empty();

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
            user = Optional.ofNullable(queryRunner.query(connection, sql, resultSetHandler, username));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }
}
