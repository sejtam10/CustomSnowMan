package dev.sejtam.csm;

import dev.sejtam.csm.mobs.Snowman.CustomSnowman;
import dev.sejtam.csm.mobs.EntityTypes;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    //Default Settings
    public boolean getDebugMode() {
        return true;
    }
    public String getPrefix() {
        return "§8[§c" + this.getName() + "§8] §7";
    }

    //Instance
    private static Main instance;
    public static Main getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;

        //TEST REGISTRING
        EntityTypes.CUSTOM_SNOWMAN.addToMap();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(label.equalsIgnoreCase("test")) {
            EntityTypes.spawnEntity(new CustomSnowman(Bukkit.getWorlds().get(0), ((Player) sender).getName()), ((Player) sender).getLocation());
        }
        return false;
    }
}
