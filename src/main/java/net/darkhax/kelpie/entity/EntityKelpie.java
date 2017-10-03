package net.darkhax.kelpie.entity;

import javax.annotation.Nullable;

import net.darkhax.kelpie.entity.ai.EntityAIDrownPassenger;
import net.darkhax.kelpie.entity.ai.EntityAIFindDeepWater;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public class EntityKelpie extends EntityHorse {

    public EntityKelpie (World world) {

        super(world);
    }

    @Override
    protected void initEntityAI () {

        // Variation of swimming AI. Only works when no passengers.
        this.tasks.addTask(0, new EntityAIDrownPassenger(this));
        this.tasks.addTask(1, new EntityAIPanic(this, 1.2D));
        
        // When has a passenger, moves towards 3 block deep water sources
        this.tasks.addTask(3, new EntityAIFindDeepWater(this, 1.0D));
        this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 1.0D));
        this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<EntityPlayer>(this, EntityPlayer.class, true));
    }

    @Nullable
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
        
        IEntityLivingData data = super.onInitialSpawn(difficulty, livingdata);
        this.setGrowingAge(0);
        System.out.println("Let Me Spawn X: " + this.posX + " - Z: " + this.posZ);
        return data;
    }
    
    @Override
    public boolean getCanSpawnHere() {
        
        System.out.println("Let Me Spawn X: " + this.posX + " - Z: " + this.posZ);
        return true;
    }
    
    @Override
    public boolean attackEntityFrom (DamageSource source, float amount) {

        final Entity entity = source.getTrueSource();

        return entity != null && this.isRidingOrBeingRiddenBy(entity) ? super.attackEntityFrom(DamageSource.OUT_OF_WORLD, amount / 2F) : super.attackEntityFrom(source, amount);
    }

    @Override
    public boolean processInteract (EntityPlayer player, EnumHand hand) {

        // Player always mounts.
        this.mountTo(player);
        return true;
    }

    @Override
    public void dismountRidingEntity() {
        
        Entity entity = this.getRidingEntity();
        
        if (entity != null && entity.isDead) {
            
            super.dismountRidingEntity();
        }
    }
    
    @Override
    public boolean canPassengerSteer () {

        // Rider is never in control.
        return false;
    }

    @Override
    public boolean canRiderInteract () {

        // Allows the rider to attack and right click while riding.
        return true;
    }

    @Override
    public boolean shouldDismountInWater (Entity rider) {

        // Rider is forced to stay on in water.
        return false;
    }

    @Override
    public boolean canBreatheUnderwater () {

        // Can breath under water.
        return true;
    }
}
