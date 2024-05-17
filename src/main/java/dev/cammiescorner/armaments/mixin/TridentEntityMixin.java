package dev.cammiescorner.armaments.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(TridentEntity.class)
public abstract class TridentEntityMixin extends Entity {
	public TridentEntityMixin(EntityType<?> variant, World world) {
		super(variant, world);
	}

	@ModifyExpressionValue(method = "onEntityHit", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;isThundering()Z"))
	private boolean lightningInRain(boolean original) {
		return original || getWorld().isRaining();
	}
}
