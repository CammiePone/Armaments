package dev.cammiescorner.armaments.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.item.ItemEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mixin(LightningBolt.class)
public abstract class LightningEntityMixin {
	@Shadow @Nullable public abstract ServerPlayer getCause();

	@Inject(method = "spawnFire", at = @At("HEAD"), cancellable = true)
	private void noLightningFire(int spreadAttempts, CallbackInfo info) {
		if(getCause() != null)
			info.cancel();
	}

	@ModifyExpressionValue(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;getEntities(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/AABB;Ljava/util/function/Predicate;)Ljava/util/List;"))
	private List<Entity> dontStrikeItems(List<Entity> original) {
		return getCause() != null ? original.stream().filter(entity -> !(entity instanceof ItemEntity)).collect(Collectors.toCollection(ArrayList::new)) : original;
	}
}
