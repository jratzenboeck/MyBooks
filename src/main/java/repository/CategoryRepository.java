package repository;

import entity.Category;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;

import java.sql.Connection;
import java.sql.SQLException;

public class CategoryRepository implements CrudRepository<Category> {

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
}
