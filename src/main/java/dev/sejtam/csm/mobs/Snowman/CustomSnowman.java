package dev.sejtam.csm.mobs.Snowman;

import dev.sejtam.csm.mobs.EntityTypes;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.EntityType;

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

        List targetB = (List) EntityTypes.getPrivateField("b", PathfinderGoalSelector.class, targetSelector);
        targetB.clear();
        List targetC = (List) EntityTypes.getPrivateField("c", PathfinderGoalSelector.class, targetSelector);
        targetC.clear();

        this.targetSelector.a(1, new SnowmanPathfinderGoalNearestAttackableTarget(this, EntityHuman.class, 30, true, false, null, owner));
    }

}