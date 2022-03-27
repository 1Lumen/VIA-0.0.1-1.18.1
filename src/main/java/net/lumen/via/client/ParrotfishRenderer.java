package net.lumen.via.client;

import net.lumen.via.client.model.ParrotfishModel;
import net.lumen.via.entity.Parrotfish;
import net.minecraft.client.render.entity.EntityRendererFactory;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class ParrotfishRenderer extends GeoEntityRenderer<Parrotfish> {
    public ParrotfishRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new ParrotfishModel());
    }
}
