package dev.sejtam.csm.mobs;

import dev.sejtam.csm.mobs.Snowman.CustomSnowman;
import net.minecraft.server.v1_8_R3.Entity;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;

import java.lang.reflect.Field;
import java.util.Map;

public enum EntityTypes {
    CUSTOM_SNOWMAN("SnowMan", 97, CustomSnowman.class);

    private String name;
    private int id;
    private Class<? extends Entity> clazz;

    EntityTypes(String name, int id, Class<? extends Entity> clazz) {
        this.name = name;
        this.id = id;
        this.clazz = clazz;
    }

    public static void spawnEntity(Entity entity, Location loc) {
        entity.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
        ((CraftWorld) loc.getWorld()).getHandle().addEntity(entity);
    }

    //TODO Replace default snowman
    public void addToMap() {
        ((Map) getPrivateField("c", net.minecraft.server.v1_8_R3.EntityTypes.class, null)).put(name, clazz);
        ((Map) getPrivateField("d", net.minecraft.server.v1_8_R3.EntityTypes.class, null)).put(clazz, name);
        ((Map) getPrivateField("e", net.minecraft.server.v1_8_R3.EntityTypes.class, null)).put(Integer.valueOf(id), clazz);
        ((Map) getPrivateField("f", net.minecraft.server.v1_8_R3.EntityTypes.class, null)).put(clazz, Integer.valueOf(id));
        ((Map) getPrivateField("g", net.minecraft.server.v1_8_R3.EntityTypes.class, null)).put(name, Integer.valueOf(id));
    }


    public static Object getPrivateField(String fieldName, Class clazz, Object instance) {
        Field field;
        Object object = null;

        try {
            field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);

            object = field.get(instance);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return object;
    }
}
