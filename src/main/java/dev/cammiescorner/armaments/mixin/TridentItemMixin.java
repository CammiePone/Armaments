package dev.cammiescorner.armaments.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.cammiescorner.armaments.ArmamentsConfig;
import dev.cammiescorner.armaments.common.registry.ModItems;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.TridentItem;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(TridentItem.class)
public class TridentItemMixin {
	@ModifyExpressionValue(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;isTouchingWaterOrRain()Z"))
	private boolean useRiptide(boolean original, World world, PlayerEntity user) {
		return original || (ArmamentsConfig.SeaCrown.enablesRiptide && user.getEquippedStack(EquipmentSlot.HEAD).isOf(ModItems.SEA_CROWN.get()));
	}

	@ModifyExpressionValue(method = "onStoppedUsing", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;isTouchingWaterOrRain()Z"))
	private boolean stopUsingRiptide(boolean original, ItemStack stack, World world, LivingEntity user) {
		return original || (ArmamentsConfig.SeaCrown.enablesRiptide && user.getEquippedStack(EquipmentSlot.HEAD).isOf(ModItems.SEA_CROWN.get()));
	}
}
