package dev.sejtam.Banka;

import dev.sejtam.Banka.Utils.MySQL;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Banka extends JavaPlugin {

    //Default Settings
    public boolean getDebugMode() {
        return true;
    }
    public String getPrefix() {
        return "§8[§6" + this.getName() + "§8] §7";
    }

    //Instance
    private static Banka instance;
    public static Banka getInstance() {
        return instance;
    }


    @Override
    public void onEnable() {
        instance = this;
    }



    //MySQL
    private static Connection connection;
    public static Connection getConnection() {
        return connection;
    }

    public void MySQLSetup() {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();

            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/banka", "root", "123456789");
        } catch (SQLException | ClassNotFoundException | IllegalAccessException | InstantiationException ex) { ex.printStackTrace(); }
        MySQL.createTables();
    }

}
