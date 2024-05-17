package dev.cammiescorner.armaments.client.renderers.item;

import com.mojang.blaze3d.lighting.DiffuseLighting;
import dev.cammiescorner.armaments.client.ArmamentsClient;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.Unit;
import net.minecraft.util.profiler.Profiler;
import org.jetbrains.annotations.NotNull;
import org.quiltmc.qsl.resource.loader.api.reloader.IdentifiableResourceReloader;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class SpecialItemRenderer implements BuiltinItemRendererRegistry.DynamicItemRenderer, IdentifiableResourceReloader {
	private final Identifier id;
	private final Identifier itemId;
	private ItemRenderer itemRenderer;
	private BakedModel inventoryItemModel;
	private BakedModel worldItemModel;

	public SpecialItemRenderer(Identifier itemId) {
		this.id = new Identifier(itemId.getNamespace(), itemId.getPath() + "_renderer");
		this.itemId = itemId;
	}

	@NotNull
	@Override
	public Identifier getQuiltId() {
		return this.id;
	}

	@Override
	public CompletableFuture<Void> reload(Synchronizer synchronizer, ResourceManager manager, Profiler prepareProfiler, Profiler applyProfiler, Executor prepareExecutor, Executor applyExecutor) {
		return synchronizer.whenPrepared(Unit.INSTANCE).thenRunAsync(() -> {
			applyProfiler.startTick();
			applyProfiler.push("listener");
			itemRenderer = ArmamentsClient.client.getItemRenderer();
			inventoryItemModel = ArmamentsClient.client.getBakedModelManager().getModel(new ModelIdentifier(itemId.withPath(itemId.getPath() + "_gui"), "inventory"));
			worldItemModel = ArmamentsClient.client.getBakedModelManager().getModel(new ModelIdentifier(itemId.withPath(itemId.getPath() + "_handheld"), "inventory"));
			applyProfiler.pop();
			applyProfiler.endTick();
		}, applyExecutor);
	}

	@Override
	public void render(ItemStack stack, ModelTransformationMode mode, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		matrices.pop();
		matrices.push();
		DiffuseLighting.setupFlatGuiLighting();

		if(mode != ModelTransformationMode.FIRST_PERSON_LEFT_HAND && mode != ModelTransformationMode.FIRST_PERSON_RIGHT_HAND && mode != ModelTransformationMode.THIRD_PERSON_LEFT_HAND && mode != ModelTransformationMode.THIRD_PERSON_RIGHT_HAND) {
			itemRenderer.renderItem(stack, mode, false, matrices, vertexConsumers, light, overlay, getModel(inventoryItemModel, stack));
		}
		else {
			boolean leftHanded;

			switch(mode) {
				case FIRST_PERSON_LEFT_HAND, THIRD_PERSON_LEFT_HAND -> leftHanded = true;
				default -> leftHanded = false;
			}

			itemRenderer.renderItem(stack, mode, leftHanded, matrices, vertexConsumers, light, overlay, getModel(worldItemModel, stack));
		}
	}

	private BakedModel getModel(BakedModel model, ItemStack stack) {
		BakedModel overrides = model.getOverrides().apply(model, stack, ArmamentsClient.client.world, null, 0);

		return overrides != null ? overrides : model;
	}
}
