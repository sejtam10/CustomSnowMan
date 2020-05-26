package dev.sejtam.csm.mobs.Snowman;

import net.minecraft.server.v1_8_R3.*;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;

import java.lang.reflect.Field;
import java.util.List;

public class CustomSnowman extends EntitySnowman {

    public CustomSnowman(org.bukkit.World world) {
        this(world, null);
    }

    public CustomSnowman(org.bukkit.World world, String owner) {
        this(((CraftWorld) world).getHandle(), owner);
    }

    public CustomSnowman(World world) {
        this(world, null);

    }

    public CustomSnowman(World world, String owner) {
        super(world);

        List targetB = (List)getPrivateField("b", PathfinderGoalSelector.class, targetSelector); targetB.clear();
        List targetC = (List)getPrivateField("c", PathfinderGoalSelector.class, targetSelector); targetC.clear();

        //this.targetSelector.a(new PathfinderGoalHurtByTarget(this, false, new Class[0]));
        this.targetSelector.a(1, new SnowmanPathfinderGoalNearestAttackableTarget(this, EntityHuman.class, true, owner));
    }

    public static Object getPrivateField(String fieldName, Class clazz, Object object)
    {
        Field field;
        Object o = null;

        try {
            field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);

            o = field.get(object);
        } catch(NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        return o;
    }

}
