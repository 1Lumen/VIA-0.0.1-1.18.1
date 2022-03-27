package net.lumen.via.registry;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.mixin.object.builder.SpawnRestrictionAccessor;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.WaterCreatureEntity;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.Biome;

public class WorldRegistry {
    public static void registerSpawns() {
        BiomeModifications.addSpawn(
            BiomeSelectors.categories(Biome.Category.OCEAN),
            SpawnGroup.WATER_CREATURE,
            EntityRegistry.PARROTFISH, 30, 1, 2
        );
        SpawnRestrictionAccessor.callRegister(EntityRegistry.PARROTFISH, SpawnRestriction.Location.IN_WATER, Heightmap.Type.OCEAN_FLOOR, MobEntity::canMobSpawn);
    }
}
