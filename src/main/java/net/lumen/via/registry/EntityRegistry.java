package net.lumen.via.registry;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.lumen.via.VIA;
import net.lumen.via.entity.Parrotfish;
import net.minecraft.entity.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.Heightmap;

import java.util.LinkedHashMap;
import java.util.Map;

public class EntityRegistry {
    private static final Map<EntityType<?>, Identifier> ENTITY_TYPES = new LinkedHashMap<>();

    public static final EntityType<Parrotfish> PARROTFISH = register("parrotfish",
            FabricEntityTypeBuilder.<Parrotfish>create(SpawnGroup.WATER_CREATURE, Parrotfish::new).dimensions(EntityDimensions.fixed(0.7f, 0.4f)).build());

    private static <T extends Entity> EntityType<T> register(String name, EntityType<T> type) {
        ENTITY_TYPES.put(type, new Identifier(VIA.MOD_ID, name));
        return type;
    }

    public static void registerEntities() {
        ENTITY_TYPES.keySet().forEach(entityType -> Registry.register(Registry.ENTITY_TYPE, ENTITY_TYPES.get(entityType), entityType));
        FabricDefaultAttributeRegistry.register(PARROTFISH, Parrotfish.createParrotfishEntityAttributes());
    }
}
