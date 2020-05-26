package dev.sejtam.csm.mobs.Snowman;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import dev.sejtam.csm.Log;
import dev.sejtam.csm.Main;
import net.minecraft.server.v1_8_R3.EntityCreature;
import net.minecraft.server.v1_8_R3.EntityLiving;
import net.minecraft.server.v1_8_R3.IEntitySelector;
import net.minecraft.server.v1_8_R3.PathfinderGoalNearestAttackableTarget;
import org.bukkit.Bukkit;

import java.util.Collections;
import java.util.List;

public class SnowmanPathfinderGoalNearestAttackableTarget<T extends EntityLiving> extends PathfinderGoalNearestAttackableTarget {

    private String owner;
    //In ticks
    private int cooldown = 20;
    private boolean canShoot = true;

    public SnowmanPathfinderGoalNearestAttackableTarget(EntityCreature entitycreature, Class<T> oclass, boolean flag, String owner) {
        this(entitycreature, oclass, flag, false, owner);
    }

    public SnowmanPathfinderGoalNearestAttackableTarget(EntityCreature entitycreature, Class<T> oclass, boolean flag, boolean flag1, String owner) {
        this(entitycreature, oclass, 20, flag, flag1, (Predicate) null, owner);
    }

    public SnowmanPathfinderGoalNearestAttackableTarget(EntityCreature entitycreature, Class<T> oclass, int cooldown, boolean flag, boolean flag1, final Predicate<? super T> predicate, String owner) {
        super(entitycreature, oclass, cooldown, flag, flag1, predicate);
        this.owner = owner;
        this.cooldown = cooldown;
    }

    public SnowmanPathfinderGoalNearestAttackableTarget(EntityCreature entitycreature, Class<T> oclass, boolean flag) {
        this(entitycreature, oclass, flag, false, null);
    }

    public SnowmanPathfinderGoalNearestAttackableTarget(EntityCreature entitycreature, Class<T> oclass, boolean flag, boolean flag1) {
        this(entitycreature, oclass, 20, flag, flag1, (Predicate) null, null);
    }

    public SnowmanPathfinderGoalNearestAttackableTarget(EntityCreature entitycreature, Class<T> oclass, int cooldown, boolean flag, boolean flag1, final Predicate<? super T> predicate) {
        this(entitycreature, oclass, cooldown, flag, flag1, predicate, null);
    }

    //Shooting timing
    @Override
    public boolean a() {
        if (this.canShoot) {
            double d0 = this.f();
            List list = this.e.world.a(this.a, this.e.getBoundingBox().grow(d0, 4.0D, d0), Predicates.and(this.c, IEntitySelector.d));
            Collections.sort(list, this.b);
            if (list.isEmpty()) {
                return false;
            }

            // Field d == Target Entity
            this.d = (EntityLiving) list.get(0);
            this.canShoot = false;
            Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> canShoot = true, cooldown);
            return true;
        } else {
            return false;
        }
    }

    //IF right target
    @Override
    public void c() {
        // Field d == Target Entity
        String name = this.d.getName();

        Log.log(name);
        if (name.equals(owner))
            return;

        super.c();
    }
}
