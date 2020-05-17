package dev.sejtam.Banka;

import dev.sejtam.Banka.Commands.Bank;
import dev.sejtam.Banka.Utils.Log;
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

        MySQLSetup();

        getCommand("banka").setExecutor(new Bank());
    }



    //MySQL
    private static Connection connection;
    public static Connection getConnection() {
        return connection;
    }

    public void MySQLSetup() {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();

            Log.log("MySQL Driver setup");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/banka", "root", "");
            if(connection != null)
                Log.log("MySQL Setup completed");
        } catch (SQLException | ClassNotFoundException | IllegalAccessException | InstantiationException ex) { ex.printStackTrace(); }
        MySQL.createTables();
    }

}
