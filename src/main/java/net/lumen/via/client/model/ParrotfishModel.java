package net.lumen.via.client.model;

import net.lumen.via.VIA;
import net.lumen.via.entity.Parrotfish;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ParrotfishModel extends AnimatedGeoModel<Parrotfish> {

    @Override
    public Identifier getModelLocation(Parrotfish object) {
        return new Identifier(VIA.MOD_ID, "geo/parrotfish.geo.json");
    }

    @Override
    public Identifier getTextureLocation(Parrotfish object) {
        return new Identifier(VIA.MOD_ID, "textures/entity/parrotfish/parrotfish.png");
    }

    @Override
    public Identifier getAnimationFileLocation(Parrotfish animatable) {
        return new Identifier(VIA.MOD_ID, "animations/parrotfish.animation.json");
    }
}
