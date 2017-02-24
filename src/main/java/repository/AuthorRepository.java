package repository;

import entity.Author;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;

import java.sql.Connection;
import java.sql.SQLException;

public class AuthorRepository implements CrudRepository {

    private final Connection connection;
    private final QueryRunner queryRunner;

    public AuthorRepository(Connection connection) {
        this.connection = connection;
        this.queryRunner = new QueryRunner();
    }

    public Author save(Author author) {
        final String sql = "insert into author(name) " +
                "values(?)";
        return (Author) save(author, queryRunner, connection, sql, author.getName());
    }

    public Author find(Long id) {
        final String sql = "select * from author where id = ?";

        try {
            final ResultSetHandler<Author> resultSetHandler = (rs) -> {
                if (!rs.next()) {
                    return null;
                }
                return new Author(rs.getLong("id"),
                        rs.getString("name"));
            };
            return queryRunner.query(connection, sql, resultSetHandler, id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
