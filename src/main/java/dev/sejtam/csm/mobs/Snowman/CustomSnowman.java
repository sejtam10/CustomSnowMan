package dev.sejtam.csm.mobs.Snowman;

import dev.sejtam.csm.Main;
import dev.sejtam.csm.mobs.EntityTypes;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;

import java.util.List;

public class CustomSnowman extends EntitySnowman {

    private boolean canShoot = true;
    private int cooldown = 20;

    public CustomSnowman(org.bukkit.World world) {
        this(world, null);
    }

    public CustomSnowman(org.bukkit.World world, String owner) {
        this(((CraftWorld) world).getHandle(), owner, 20);
    }

    public CustomSnowman(org.bukkit.World world, int cooldown) {
        this(((CraftWorld) world).getHandle(), null, cooldown);
    }

    public CustomSnowman(org.bukkit.World world, String owner, int cooldown) {
        this(((CraftWorld) world).getHandle(), owner, cooldown);
    }

    public CustomSnowman(World world) {
        this(world, null, 20);
    }

    public CustomSnowman(World world, String owner) {
        this(world, owner, 20);
    }

    public CustomSnowman(World world, int cooldown) {
        this(world, null, cooldown);
    }

    public CustomSnowman(World world, String owner, int cooldown) {
        super(world);

        List targetB = (List) EntityTypes.getPrivateField("b", PathfinderGoalSelector.class, targetSelector);
        targetB.clear();
        List targetC = (List) EntityTypes.getPrivateField("c", PathfinderGoalSelector.class, targetSelector);
        targetC.clear();

        this.targetSelector.a(1, new SnowmanPathfinderGoalNearestAttackableTarget(this, EntityHuman.class, true, false, null, owner));
        this.cooldown = cooldown;
    }

    @Override
    public void a(EntityLiving entityliving, float f) {
        if(this.canShoot) {
            this.canShoot = false;
            Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> canShoot = true, cooldown);
            super.a(entityliving, f);
        } else {
            return;
        }
    }
}
