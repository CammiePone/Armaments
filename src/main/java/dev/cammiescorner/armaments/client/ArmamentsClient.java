package dev.cammiescorner.armaments.client;

import dev.cammiescorner.armaments.Armaments;
import dev.cammiescorner.armaments.client.models.armor.SeaCrownArmorModel;
import dev.cammiescorner.armaments.client.renderers.armor.SeaCrownArmorRenderer;
import dev.cammiescorner.armaments.client.renderers.item.SpecialItemRenderer;
import dev.cammiescorner.armaments.common.components.item.CrystalSpearComponent;
import dev.cammiescorner.armaments.common.items.SpecialRenderItem;
import dev.cammiescorner.armaments.common.registry.ModComponents;
import dev.cammiescorner.armaments.common.registry.ModItems;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.impl.client.rendering.ArmorRendererRegistryImpl;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.item.Item;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;
import org.quiltmc.qsl.resource.loader.api.ResourceLoader;

public class ArmamentsClient implements ClientModInitializer {
	public static final MinecraftClient client = MinecraftClient.getInstance();

	@Override
	public void onInitializeClient(ModContainer mod) {
		EntityModelLayerRegistry.registerModelLayer(SeaCrownArmorModel.MODEL_LAYER, SeaCrownArmorModel::getTexturedModelData);

		ArmorRendererRegistryImpl.register(new SeaCrownArmorRenderer(), ModItems.SEA_CROWN.get());

		ModelPredicateProviderRegistry.register(ModItems.CRYSTAL_SPEAR.get(), Armaments.id("charge"), (itemStack, clientWorld, livingEntity, i) -> {
			CrystalSpearComponent component = ModComponents.CRYSTAL_SPEAR.get(itemStack);

			return component.getCharge() / 4f;
		});

		ModItems.ITEMS.stream().forEach(holder -> {
			Item item = holder.get();

			if(item instanceof SpecialRenderItem) {
				Identifier id = holder.getId();
				SpecialItemRenderer staffItemRenderer = new SpecialItemRenderer(id);
				ResourceLoader.get(ResourceType.CLIENT_RESOURCES).registerReloader(staffItemRenderer);
				BuiltinItemRendererRegistry.INSTANCE.register(item, staffItemRenderer);

				ModelLoadingPlugin.register(ctx -> ctx.addModels(
					new ModelIdentifier(id.withPath(id.getPath() + "_gui"), "inventory"),
					new ModelIdentifier(id.withPath(id.getPath() + "_handheld"), "inventory")
				));
			}
		});
	}
}
