package dev.sejtam.Banka.Utils;

import dev.sejtam.Banka.Banka;

import java.sql.SQLException;

public class MySQL {

    public static boolean createTables() {
        try {
            if(Banka.getConnection() == null)
                return false;

            if(Banka.getConnection().isClosed())
                return false;

            Banka.getConnection().createStatement().executeQuery(
                    "create table if not exists ucty(" +
                        "    id          int auto_increment" +
                        "    primary     key," +
                        "    name        varchar(255)                          null," +
                        "    password    varchar(255)                          null," +
                        "    create_date timestamp default current_timestamp() null," +
                        "    type        enum('klasicky', 'studentsky')        null," +
                        "    money       int       default 0                   null);");

            Banka.getConnection().createStatement().executeQuery(
                    "create table if not exists logs(" +
                        "    id   int                                           null," +
                        "    name varchar(255)                                  null," +
                        "    time TIMESTAMP default current_timestamp()         null," +
                        "    type enum('vyber', 'prevod', 'vklad', 'poplatek')  null," +
                        "    text MEDIUMTEXT                                    null);");
        } catch (SQLException ex) { return false; }
        return true;
    }

}
