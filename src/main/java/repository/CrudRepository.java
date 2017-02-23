package repository;

import com.sun.xml.internal.rngom.parse.host.Base;
import entity.BaseEntity;
import entity.Category;
import entity.User;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface CrudRepository<T> {

    default BaseEntity save(BaseEntity obj, QueryRunner queryRunner, Connection connection, String sql, Object... params) {
        try {
            obj.setId(queryRunner.insert(connection, sql,
                    getResultSetHandler(), params));
            return obj;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    default ResultSetHandler<Long> getResultSetHandler() {
        return (rs) -> {
            if (!rs.next()) {
                return null;
            }
            return rs.getLong(1);
        };
    }

    default int batchUpdate(List<? extends BaseEntity> batch, String sql, QueryRunner queryRunner, Connection connection, Object param) {
        class SumObj {
            public int sum = 0;
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
