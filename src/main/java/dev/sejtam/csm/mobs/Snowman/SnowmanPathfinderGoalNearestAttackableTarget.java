package dev.sejtam.csm.mobs.Snowman;

import com.google.common.base.Predicate;
import dev.sejtam.csm.Log;
import net.minecraft.server.v1_8_R3.EntityCreature;
import net.minecraft.server.v1_8_R3.EntityLiving;
import net.minecraft.server.v1_8_R3.PathfinderGoalNearestAttackableTarget;

public class SnowmanPathfinderGoalNearestAttackableTarget<T extends EntityLiving> extends PathfinderGoalNearestAttackableTarget {

    private String owner;

    public SnowmanPathfinderGoalNearestAttackableTarget(EntityCreature entitycreature, Class<T> oclass, boolean flag, String owner) {
        this(entitycreature, oclass, flag, false, owner);
    }

    public SnowmanPathfinderGoalNearestAttackableTarget(EntityCreature entitycreature, Class<T> oclass, boolean flag, boolean flag1, String owner) {
        this(entitycreature, oclass, 10, flag, flag1, (Predicate)null, owner);
    }

    public SnowmanPathfinderGoalNearestAttackableTarget(EntityCreature entitycreature, Class<T> oclass, int i, boolean flag, boolean flag1, final Predicate<? super T> predicate, String owner) {
        super(entitycreature, oclass, i, flag, flag1, predicate);
        this.owner = owner;
    }

    public SnowmanPathfinderGoalNearestAttackableTarget(EntityCreature entitycreature, Class<T> oclass, boolean flag) {
        this(entitycreature, oclass, flag, false, null);
    }

    public SnowmanPathfinderGoalNearestAttackableTarget(EntityCreature entitycreature, Class<T> oclass, boolean flag, boolean flag1) {
        this(entitycreature, oclass, 10, flag, flag1, (Predicate)null, null);
    }

    public SnowmanPathfinderGoalNearestAttackableTarget(EntityCreature entitycreature, Class<T> oclass, int i, boolean flag, boolean flag1, final Predicate<? super T> predicate) {
        this(entitycreature, oclass, i, flag, flag1, predicate, null);
    }


    // Field d == Target Entity

    @Override
    public void c() {
        String name = this.d.getName();

        if(name.equals(owner))
            return;

        super.c();
    }
}
