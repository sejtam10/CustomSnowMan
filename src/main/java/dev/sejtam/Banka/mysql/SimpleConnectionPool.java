package dev.sejtam.Banka.mysql;

import dev.sejtam.Banka.mysql.interfaces.ConnectionPool;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

//TODO Max connections
public class SimpleConnectionPool implements ConnectionPool {

    private String host;
    private int port;
    private String database;
    private String user;
    private String password;

    public SimpleConnectionPool(String host, String database, String user, String password) {
        this(host, 3306, database, user, password);
    }

    public SimpleConnectionPool(String host, int port, String database, String user, String password) {
        this.host = host;
        this.port = port;
        this.database = database;
        this.user = user;
        this.password = password;
    }

    private Connection createConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            return DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/"
                    + database + "?autoReconnectForPools=true", user, password);

        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public Connection getConnection() {
        return createConnection();
    }

    public Boolean executeAsync(String sql) {
        CompletableFuture<Boolean> future = CompletableFuture.supplyAsync(() -> executeQuery(sql, null, false) != null );

        try {
            return future.get();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public boolean execute(String sql) {
        return executeQuery(sql, null, false) != null;
    }

    public Boolean executeUpdateAsync(String sql, List<Object> parameters) {
        CompletableFuture<Boolean> future = CompletableFuture.supplyAsync(() -> executeQuery(sql, parameters, false) != null );

        try {
            return future.get();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public boolean executeUpdate(String sql, List<Object> parameters) {
        return executeQuery(sql, parameters, true) != null;
    }

    public List<Map<String, Object>> executeQueryAsync(String sql, List<Object> parameters) {
        CompletableFuture<List<Map<String, Object>>> future = CompletableFuture.supplyAsync(() -> executeQuery(sql, parameters, false));

        try {
            return future.get();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public List<Map<String, Object>> executeQuery(String sql, List<Object> parameters, boolean isUpdate) {
        Connection connection = getConnection();

        if (connection == null)
            return null;

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {

            preparedStatement = connection.prepareStatement(sql);

            if (preparedStatement == null)
                return null;

            if(parameters != null) {
                for (int i = 1; i <= parameters.size(); i++) {
                    preparedStatement.setObject(i, parameters.get(i - 1));
                }
            }

            if(isUpdate) {
                if(preparedStatement.executeUpdate() > 0)
                    return new ArrayList<>();
                else
                    return null;
            }

            resultSet = preparedStatement.executeQuery();
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();

            List<Map<String, Object>> returnList = new ArrayList<>();
            while (resultSet.next()) {
                Map<String, Object> objects = new HashMap<>();
                for (int x = 1; x <= resultSetMetaData.getColumnCount(); x++) {
                    objects.put(resultSetMetaData.getColumnName(x), resultSet.getObject(x));
                }
                returnList.add(objects);
            }

            return returnList;
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            close(resultSet);
            close(preparedStatement);
            close(connection);
        }
        return null;
    }


    public static boolean close(Connection connection) {
        try {
            if (connection == null)
                return false;

            if (connection.isClosed())
                return false;

            connection.close();
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public static boolean close(PreparedStatement preparedStatement) {
        try {
            if (preparedStatement == null)
                return false;

            if (preparedStatement.isClosed())
                return false;

            preparedStatement.close();
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public static boolean close(ResultSet resultSet) {
        try {
            if (resultSet == null)
                return false;

            if (resultSet.isClosed())
                return false;

            resultSet.close();
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
