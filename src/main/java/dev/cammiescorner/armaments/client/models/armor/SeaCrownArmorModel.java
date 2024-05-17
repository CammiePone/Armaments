package dev.cammiescorner.armaments.client.models.armor;

import dev.cammiescorner.armaments.Armaments;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.entity.LivingEntity;

public class SeaCrownArmorModel<T extends LivingEntity> extends BipedEntityModel<T> {
	public static final EntityModelLayer MODEL_LAYER = new EntityModelLayer(Armaments.id("sea_crown"), "main");
	public final ModelPart crown;

	public SeaCrownArmorModel(ModelPart root) {
		super(root);
		crown = head.getChild("crown");
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData data = BipedEntityModel.getModelData(Dilation.NONE, 0);
		ModelPartData head = data.getRoot().getChild(EntityModelPartNames.HEAD);

		head.addChild("crown", ModelPartBuilder.create().uv(0, 64).cuboid(-4.0F, -13.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(0.5F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		return TexturedModelData.of(data, 128, 128);
	}
}
