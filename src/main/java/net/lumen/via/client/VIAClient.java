package net.lumen.via.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.lumen.via.client.model.ParrotfishModel;
import net.lumen.via.registry.EntityRegistry;
import software.bernie.example.client.renderer.entity.ExampleGeoRenderer;

@Environment(EnvType.CLIENT)
public class VIAClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(EntityRegistry.PARROTFISH, ParrotfishRenderer::new);
    }
}
