package repository;

import entity.BaseEntity;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface CrudRepository {

    default Optional<? extends BaseEntity> save(BaseEntity obj, QueryRunner queryRunner, Connection connection, String sql, Object... params) {
        Optional<? extends BaseEntity> optionalBaseEntity = Optional.empty();

        try {
            obj.setId(queryRunner.insert(connection, sql,
                    getIdResultSetHandler(), params));
            optionalBaseEntity = Optional.ofNullable(obj);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return optionalBaseEntity;
    }

    default ResultSetHandler<Long> getIdResultSetHandler() {
        return (rs) -> {
            if (!rs.next()) {
                return null;
            }
            return rs.getLong(1);
        };
    }

    default int batchUpdate(List<? extends BaseEntity> batch, String sql, QueryRunner queryRunner, Connection connection, Object param) {
        class SumObj {
            private int sum = 0;
        }
        final SumObj sumObj = new SumObj();
        batch.forEach(item -> {
            try {
                sumObj.sum += queryRunner.update(connection, sql, item.getId(), param);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        return sumObj.sum;
    }
}
