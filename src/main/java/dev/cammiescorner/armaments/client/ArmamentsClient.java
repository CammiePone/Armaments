package dev.cammiescorner.armaments.client;

import dev.cammiescorner.armaments.Armaments;
import dev.cammiescorner.armaments.client.models.armor.SeaCrownArmorModel;
import dev.cammiescorner.armaments.client.renderers.armor.SeaCrownArmorRenderer;
import dev.cammiescorner.armaments.client.renderers.item.SpecialItemRenderer;
import dev.cammiescorner.armaments.common.components.item.CrystalSpearComponent;
import dev.cammiescorner.armaments.common.items.SpecialRenderItem;
import dev.cammiescorner.armaments.common.registry.ModComponents;
import dev.cammiescorner.armaments.common.registry.ModItems;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.impl.client.rendering.ArmorRendererRegistryImpl;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.item.Item;

public class ArmamentsClient implements ClientModInitializer {
	public static final Minecraft client = Minecraft.getInstance();

	@Override
	public void onInitializeClient() {
		EntityModelLayerRegistry.registerModelLayer(SeaCrownArmorModel.MODEL_LAYER, SeaCrownArmorModel::getTexturedModelData);

		ArmorRendererRegistryImpl.register(new SeaCrownArmorRenderer(), ModItems.SEA_CROWN.get());

		ItemProperties.register(ModItems.CRYSTAL_SPEAR.get(), Armaments.id("charge"), (itemStack, clientWorld, livingEntity, i) -> {
			CrystalSpearComponent component = ModComponents.CRYSTAL_SPEAR.get(itemStack);

			return component.getCharge() / 4f;
		});

		ModItems.ITEMS.stream().forEach(holder -> {
			Item item = holder.get();

			if(item instanceof SpecialRenderItem) {
				ResourceLocation id = holder.getId();
				SpecialItemRenderer specialItemRenderer = new SpecialItemRenderer(id);
				ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(specialItemRenderer);
				BuiltinItemRendererRegistry.INSTANCE.register(item, specialItemRenderer);

				ModelLoadingPlugin.register(ctx -> ctx.addModels(
					new ModelResourceLocation(id.withPath(id.getPath() + "_gui"), "inventory"),
					new ModelResourceLocation(id.withPath(id.getPath() + "_handheld"), "inventory")
				));
			}
		});
	}
}
