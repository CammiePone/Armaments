package dev.cammiescorner.armaments.client.renderers.armor;

import dev.cammiescorner.armaments.Armaments;
import dev.cammiescorner.armaments.client.models.armor.SeaCrownArmorModel;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class SeaCrownArmorRenderer implements ArmorRenderer {
	private static final Identifier TEXTURE = Armaments.id("textures/entity/armor/sea_crown.png");
	private final MinecraftClient client = MinecraftClient.getInstance();
	private SeaCrownArmorModel<LivingEntity> model;

	@Override
	public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, ItemStack stack, LivingEntity entity, EquipmentSlot slot, int light, BipedEntityModel<LivingEntity> contextModel) {
		if(model == null)
			model = new SeaCrownArmorModel<>(client.getEntityModelLoader().getModelPart(SeaCrownArmorModel.MODEL_LAYER));

		contextModel.setAttributes(model);
		model.setVisible(true);
		model.crown.visible = slot == EquipmentSlot.HEAD;

		model.render(matrices, ItemRenderer.getArmorGlintConsumer(vertexConsumers, RenderLayer.getArmorCutoutNoCull(TEXTURE), false, true), light, OverlayTexture.DEFAULT_UV, 1f, 1f, 1f, 1f);
	}
}
