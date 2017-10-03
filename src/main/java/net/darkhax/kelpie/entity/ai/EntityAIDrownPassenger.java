package net.darkhax.kelpie.entity.ai;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAISwimming;

public class EntityAIDrownPassenger extends EntityAISwimming {

    private final EntityLiving localEntity;

    public EntityAIDrownPassenger (EntityLiving entity) {

        super(entity);
        this.localEntity = entity;
    }

    @Override
    public boolean shouldExecute () {

        return this.localEntity.getPassengers().isEmpty() ? super.shouldExecute() : false;
    }
}
