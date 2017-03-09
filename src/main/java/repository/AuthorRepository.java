package repository;

import entity.Author;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

public class AuthorRepository implements CrudRepository {

    private final Connection connection;
    private final QueryRunner queryRunner;

    public AuthorRepository(Connection connection) {
        this.connection = connection;
        this.queryRunner = new QueryRunner();
    }

    public Optional<Author> save(Author author) {
        final String sql = "insert into author(name) " +
                "values(?)";
        return (Optional<Author>) save(author, queryRunner, connection, sql, author.getName());
    }

    public Optional<Author> find(String name) {
        final String sql = "select * from author where name = ?";
        Optional<Author> optionalAuthor = Optional.empty();

        try {
            final ResultSetHandler<Author> resultSetHandler = (rs) -> {
                if (!rs.next()) {
                    return null;
                }
                return new Author(rs.getLong("id"),
                        rs.getString("name"));
            };
            optionalAuthor = Optional.ofNullable(queryRunner.query(connection, sql, resultSetHandler, name));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return optionalAuthor;
    }
}
