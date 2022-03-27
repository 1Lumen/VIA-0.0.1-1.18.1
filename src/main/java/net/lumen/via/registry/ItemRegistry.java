package net.lumen.via.registry;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.lumen.via.VIA;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.EntityBucketItem;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ItemRegistry {
    public static final Item PARROTFISH_BUCKET = registerItem("parrotfish_bucket",
            new EntityBucketItem(EntityRegistry.PARROTFISH, Fluids.WATER, SoundEvents.ITEM_BUCKET_EMPTY_FISH, new FabricItemSettings().maxCount(1).group(VIA.VIA)));
    public static final Item PARROTFISH = registerItem("parrotfish",
            new Item(new FabricItemSettings().food(new FoodComponent.Builder().hunger(2).saturationModifier(0.2f).build()).group(VIA.VIA)));
    public static final Item COOKED_PARROTFISH = registerItem("cooked_parrotfish",
            new Item(new FabricItemSettings().food(new FoodComponent.Builder().hunger(6).saturationModifier(0.9f).build()).group(VIA.VIA)));
    public static final Item PARROTFISH_SPAWNEGG = registerItem("parrotfish_spawnegg",
            new SpawnEggItem(EntityRegistry.PARROTFISH,3654642 ,16167425 , new FabricItemSettings().group(VIA.VIA)));

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registry.ITEM, new Identifier(VIA.MOD_ID, name), item);
    }

    public static void registerItems() {
        VIA.LOGGER.info("Registering Items for " + VIA.MOD_ID);
    }
}
