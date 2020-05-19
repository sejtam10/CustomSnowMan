package dev.sejtam.Banka;

import dev.sejtam.Banka.Commands.Bank;
import dev.sejtam.Banka.mysql.SimpleConnectionPool;
import org.bukkit.plugin.java.JavaPlugin;

public class Banka extends JavaPlugin {

    //Default Settings
    public boolean getDebugMode() {
        return true;
    }
    public String getPrefix() {
        return "§8[§c" + this.getName() + "§8] §7";
    }

    //Instance
    private static Banka instance;
    public static Banka getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;

        MySQLSetup();

        Bank banka = new Bank();

        getCommand("banka").setExecutor(banka);
        getServer().getPluginManager().registerEvents(banka, this);
    }

    //MySQL
    public SimpleConnectionPool connectionPool = new SimpleConnectionPool("127.0.01", "banka", "root", "");

    //TODO Databaze v configu
    public void MySQLSetup() {
        createTables();
    }

    public void createTables() {
        connectionPool.executeUpdate(
                "create table if not exists ucty(" +
                        "    id          int auto_increment" +
                        "    primary     key," +
                        "    name        varchar(255)                          not null," +
                        "    password    varchar(255)                          not null," +
                        "    create_date timestamp default current_timestamp() not null," +
                        "    type        enum('klasicky', 'studentsky')        not null," +
                        "    money       int       default 0                   not null);", null);

        connectionPool.executeUpdate(
                "create table if not exists account_log(" +
                        "    id     int                                           not null," +
                        "    name   varchar(255)                                  not null," +
                        "    time   TIMESTAMP default current_timestamp()         not null," +
                        "    type   enum('vyber', 'prevod', 'vklad', 'poplatek')  not null," +
                        "    money  int                                           not null);", null);

        connectionPool.executeUpdate(
                "create table if not exists player_log(" +
                        "    name   varchar(255)                                  not null," +
                        "    time   TIMESTAMP default current_timestamp()         not null," +
                        "    type   enum('vyber', 'prevod', 'vklad', 'poplatek')  not null," +
                        "    money  int                                           not null);", null);
    }

}
