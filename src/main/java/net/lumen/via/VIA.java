package net.lumen.via;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.lumen.via.entity.Parrotfish;
import net.lumen.via.registry.EntityRegistry;
import net.lumen.via.registry.ItemRegistry;
import net.lumen.via.registry.WorldRegistry;
import net.minecraft.entity.passive.FishEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.bernie.geckolib3.GeckoLib;

import static net.lumen.via.registry.EntityRegistry.PARROTFISH;

public class VIA implements ModInitializer {
	public static final String MOD_ID = "via";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final ItemGroup VIA = FabricItemGroupBuilder.build(new Identifier(MOD_ID, "via"),
			() -> new ItemStack(ItemRegistry.PARROTFISH));

	@Override
	public void onInitialize() {
		ItemRegistry.registerItems();
		EntityRegistry.registerEntities();
		WorldRegistry.registerSpawns();
		GeckoLib.initialize();
	}
}
