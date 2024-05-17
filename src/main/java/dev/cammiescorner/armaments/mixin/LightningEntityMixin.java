package dev.cammiescorner.armaments.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LightningEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(LightningEntity.class)
public abstract class LightningEntityMixin {
	@Shadow @Nullable public abstract ServerPlayerEntity getChanneler();

	@Inject(method = "spawnFire", at = @At("HEAD"), cancellable = true)
	private void noLightningFire(int spreadAttempts, CallbackInfo info) {
		if(getChanneler() != null)
			info.cancel();
	}

	@ModifyExpressionValue(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getOtherEntities(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/Box;Ljava/util/function/Predicate;)Ljava/util/List;"))
	private List<Entity> dontStrikeItems(List<Entity> original) {
		return getChanneler() != null ? original.stream().filter(entity -> !(entity instanceof ItemEntity)).toList() : original;
	}
}
