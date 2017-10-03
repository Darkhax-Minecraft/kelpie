package net.darkhax.kelpie;

import net.darkhax.bookshelf.registry.RegistryHelper;
import net.darkhax.bookshelf.util.BiomeUtils;
import net.darkhax.kelpie.entity.EntityKelpie;
import net.minecraft.entity.EnumCreatureType;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityMountEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod(modid = "kelpie", name = "Kelpie", version = "@VERSION@", dependencies = "required-after:bookshelf@[2.2.457,);", certificateFingerprint = "@FINGERPRINT@")
public class Kelpie {

    public static final RegistryHelper helper = new RegistryHelper("kelpie").enableAutoRegistration();

    @EventHandler
    public void onPreInit (FMLPreInitializationEvent event) {
        
        MinecraftForge.EVENT_BUS.register(this);
        helper.registerMob(EntityKelpie.class, "kelpie", 0, 100, -100).spawn(EnumCreatureType.CREATURE, 5, 1, 1, BiomeUtils.getBiomesForTypes(Type.OCEAN, Type.WATER, Type.BEACH, Type.RIVER));
    }

    @SubscribeEvent
    public void onChunkPopulate (EntityMountEvent event) {
        
        if (event.getEntityMounting().isDead || event.getEntityBeingMounted().isDead) {
            
            return;
        }
        
        if (event.isDismounting() && event.getEntityBeingMounted() instanceof EntityKelpie) {
            
            //TODO NO DISMOUNT >_<
            event.setCanceled(true);
        }
    }
}
