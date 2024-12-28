package dev.cammiescorner.armaments.client.renderers.armor;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.cammiescorner.armaments.Armaments;
import dev.cammiescorner.armaments.client.models.armor.SeaCrownArmorModel;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class SeaCrownArmorRenderer implements ArmorRenderer {
	private static final ResourceLocation TEXTURE = Armaments.id("textures/entity/armor/sea_crown.png");
	private final Minecraft client = Minecraft.getInstance();
	private SeaCrownArmorModel<LivingEntity> model;

	@Override
	public void render(PoseStack matrices, MultiBufferSource vertexConsumers, ItemStack stack, LivingEntity entity, EquipmentSlot slot, int light, HumanoidModel<LivingEntity> contextModel) {
		if(model == null)
			model = new SeaCrownArmorModel<>(client.getEntityModels().bakeLayer(SeaCrownArmorModel.MODEL_LAYER));

		contextModel.copyPropertiesTo(model);
		model.setAllVisible(true);
		model.crown.visible = slot == EquipmentSlot.HEAD;

		model.renderToBuffer(matrices, ItemRenderer.getArmorFoilBuffer(vertexConsumers, RenderType.armorCutoutNoCull(TEXTURE), false, true), light, OverlayTexture.NO_OVERLAY, 1f, 1f, 1f, 1f);
	}
}
