package dev.sejtam.Banka.mysql.interfaces;

import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public interface ConnectionPool {
    Connection getConnection();

    void executeAsync(String sqlCommand, final Consumer<Boolean> callback);
    boolean execute(String sqlCommand);
    boolean executeUpdate(String sql, List<Object> parameters);
    void executeUpdateAsync(String sql, List<Object> parameters, final Consumer<Boolean> callback);
    void executeQueryAsync(String sqlCommand, List<Object> parameters, boolean isUpdate, final Consumer<List<Map<String, Object>>> callback);
    List<Map<String, Object>> executeQuery(String sqlCommand, List<Object> parameters, boolean isUpdate);

}
