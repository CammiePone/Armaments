package dev.cammiescorner.armaments.client.renderers.item;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.cammiescorner.armaments.client.ArmamentsClient;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.Unit;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class SpecialItemRenderer implements BuiltinItemRendererRegistry.DynamicItemRenderer, IdentifiableResourceReloadListener {
	private final ResourceLocation id;
	private final ResourceLocation itemId;
	private ItemRenderer itemRenderer;
	private BakedModel inventoryItemModel;
	private BakedModel worldItemModel;

	public SpecialItemRenderer(ResourceLocation itemId) {
		this.id = new ResourceLocation(itemId.getNamespace(), itemId.getPath() + "_renderer");
		this.itemId = itemId;
	}

	@NotNull
	@Override
	public ResourceLocation getFabricId() {
		return this.id;
	}

	@Override
	public CompletableFuture<Void> reload(PreparationBarrier synchronizer, ResourceManager manager, ProfilerFiller prepareProfiler, ProfilerFiller applyProfiler, Executor prepareExecutor, Executor applyExecutor) {
		return synchronizer.wait(Unit.INSTANCE).thenRunAsync(() -> {
			applyProfiler.startTick();
			applyProfiler.push("listener");
			itemRenderer = ArmamentsClient.client.getItemRenderer();
			inventoryItemModel = ArmamentsClient.client.getModelManager().getModel(new ModelResourceLocation(itemId.withPath(itemId.getPath() + "_gui"), "inventory"));
			worldItemModel = ArmamentsClient.client.getModelManager().getModel(new ModelResourceLocation(itemId.withPath(itemId.getPath() + "_handheld"), "inventory"));
			applyProfiler.pop();
			applyProfiler.endTick();
		}, applyExecutor);
	}

	@Override
	public void render(ItemStack stack, ItemDisplayContext mode, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay) {
		matrices.popPose();
		matrices.pushPose();
		Lighting.setupForFlatItems();

		if(mode != ItemDisplayContext.FIRST_PERSON_LEFT_HAND && mode != ItemDisplayContext.FIRST_PERSON_RIGHT_HAND && mode != ItemDisplayContext.THIRD_PERSON_LEFT_HAND && mode != ItemDisplayContext.THIRD_PERSON_RIGHT_HAND) {
			itemRenderer.render(stack, mode, false, matrices, vertexConsumers, light, overlay, getModel(inventoryItemModel, stack));
		}
		else {
			boolean leftHanded;

			switch(mode) {
				case FIRST_PERSON_LEFT_HAND, THIRD_PERSON_LEFT_HAND -> leftHanded = true;
				default -> leftHanded = false;
			}

			itemRenderer.render(stack, mode, leftHanded, matrices, vertexConsumers, light, overlay, getModel(worldItemModel, stack));
		}
	}

	private BakedModel getModel(BakedModel model, ItemStack stack) {
		BakedModel overrides = model.getOverrides().resolve(model, stack, ArmamentsClient.client.level, null, 0);

		return overrides != null ? overrides : model;
	}
}
