package net.darkhax.kelpie.entity.ai;

import java.util.Random;

import javax.annotation.Nullable;

import net.darkhax.bookshelf.util.MathsUtils;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityAIFindDeepWater extends EntityAIBase {
    
    private final EntityCreature creature;
    private final World world;
    private BlockPos pos;

    public EntityAIFindDeepWater (EntityCreature theCreatureIn, double movementSpeedIn) {

        this.creature = theCreatureIn;
        this.world = theCreatureIn.world;
        this.setMutexBits(1);
    }

    @Override
    public boolean shouldExecute () {

        final boolean isRiderNotInWater = creature.isBeingRidden() && !creature.getPassengers().isEmpty() && !creature.getPassengers().get(0).isInsideOfMaterial(Material.WATER);
        
        if (isRiderNotInWater) {
            
            this.pos = findPossibleShelter();
            
            if (this.pos == null && !this.creature.isInWater() && MathsUtils.tryPercentage(0.25)) {
                
                this.creature.attackEntityFrom(DamageSource.STARVE, this.creature.getRNG().nextFloat() * 3f);
                
                for (Entity passenger : this.creature.getPassengers()) {
                    
                    passenger.attackEntityFrom(DamageSource.STARVE, this.creature.getRNG().nextFloat() * 3f);
                }
            }
        }
        
        return isRiderNotInWater && this.pos != null;
    }
    
    @Override
    public void startExecuting () {

        this.creature.setPositionAndUpdate(this.pos.getX() + 0.5f, this.pos.getY(), this.pos.getZ() + 0.5f);

        for (Entity passenger : this.creature.getPassengers()) {
            
            passenger.setAir(0);
        }
    }

    @Nullable
    private BlockPos findPossibleShelter () {
        
        if (this.pos != null) {
            
            return this.pos;
        }
        
        final Random random = this.creature.getRNG();
        final BlockPos startPos = new BlockPos(this.creature.posX, this.creature.getEntityBoundingBox().minY, this.creature.posZ);

        for (int i = 0; i < 20; ++i) {
            
            BlockPos randPos = startPos.add(random.nextInt(20) - 10, random.nextInt(10) - 3, random.nextInt(20) - 10);

            if (world.getBlockState(randPos).getMaterial() == Material.WATER) {
                
                for (int x = 0; x < 2; x++) {
                    
                    randPos = randPos.offset(EnumFacing.DOWN);
                    
                    if (world.getBlockState(randPos).getMaterial() == Material.WATER) {
                        
                        return randPos;
                    }
                }
            }
        }

        return this.pos;
    }
}