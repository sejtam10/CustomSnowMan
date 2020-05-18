package dev.sejtam.Banka.Utils;

import dev.sejtam.Banka.Banka;
import dev.sejtam.Banka.Utils.Enums.ActionType;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MySQL {

    public static boolean createTables() {
        try {
            if(Banka.getConnection() == null)
                return false;

            if(Banka.getConnection().isClosed())
                return false;

            Banka.getConnection().createStatement().execute(
                    "create table if not exists ucty(" +
                        "    id          int auto_increment" +
                        "    primary     key," +
                        "    name        varchar(255)                          null," +
                        "    password    varchar(255)                          null," +
                        "    create_date timestamp default current_timestamp() null," +
                        "    type        enum('klasicky', 'studentsky')        null," +
                        "    money       int       default 0                   null);");

            Banka.getConnection().createStatement().execute(
                    "create table if not exists account_log(" +
                        "    id     int                                           null," +
                        "    name   varchar(255)                                  null," +
                        "    time   TIMESTAMP default current_timestamp()         null," +
                        "    type   enum('vyber', 'prevod', 'vklad', 'poplatek')  null," +
                        "    money  int                                           null);");

            Banka.getConnection().createStatement().execute(
                    "create table if not exists player_log(" +
                        "    name   varchar(255)                                  null," +
                        "    time   TIMESTAMP default current_timestamp()         null," +
                        "    type   enum('vyber', 'prevod', 'vklad', 'poplatek')  null," +
                        "    money  int                                           null);");

        } catch (SQLException ex) { ex.printStackTrace(); return false; }
        return true;
    }

    public static Pagination<BankAccount.Log> getPlayerLog(String name) {
        try {
            if (Banka.getConnection() == null)
                return null;

            if (Banka.getConnection().isClosed())
                return null;

            Statement statement = Banka.getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT t.* FROM banka.player_log t WHERE t.name = '" + name + "'");
            Pagination<BankAccount.Log> logs = new Pagination<>(5);
            while(resultSet.next()) {
                logs.add(new BankAccount.Log(resultSet.getTimestamp("time"),
                        ActionType.valueOf(resultSet.getString("type")),
                        resultSet.getInt("money")));
            }

            if(logs.isEmpty())
                return null;

            return logs;
        } catch (SQLException ex) {
            return null;
        }
    }

}
