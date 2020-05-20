package dev.sejtam.Banka.mysql.interfaces;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

public interface ConnectionPool {
    Connection getConnection();

    Boolean executeAsync(String sqlCommand);
    boolean execute(String sqlCommand);
    boolean executeUpdate(String sql, List<Object> parameters);
    Boolean executeUpdateAsync(String sql, List<Object> parameters);
    List<Map<String, Object>> executeQueryAsync(String sqlCommand, List<Object> parameters);
    List<Map<String, Object>> executeQuery(String sqlCommand, List<Object> parameters, boolean isUpdate);

}
