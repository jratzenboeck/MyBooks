package repository;

import entity.Category;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoryRepository implements CrudRepository {

    private final Connection connection;
    private final QueryRunner queryRunner;

    public CategoryRepository(Connection connection) {
        this.connection = connection;
        this.queryRunner = new QueryRunner();
    }

    public Category save(Category category) {
        final String sql = "insert into category(name) " +
                "values(?)";
        return (Category) save(category, queryRunner, connection, sql, category.getName());
    }

    public List<Category> findAll() {
        final String sql = "select * from category";

        try {
            return queryRunner.query(connection, sql, getListResultSetHandler());
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Category> getReadingInterests(Long userId) {
        final String sql = "select * from category " +
                "where category.id in (select reading_interest.category_id from reading_interest " +
                "where reading_interest.user_id = ?)";

        try {
            return queryRunner.query(connection, sql, getListResultSetHandler(), userId);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private ResultSetHandler<List<Category>> getListResultSetHandler() {
        return (rs) -> {
            List<Category> found = new ArrayList<>();
            while (rs.next()) {
                found.add(new Category(rs.getLong("id"),
                        rs.getString("name")));
            }
            return found;
        };
    }
}
