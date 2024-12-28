package dev.cammiescorner.armaments.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.cammiescorner.armaments.ArmamentsConfig;
import dev.cammiescorner.armaments.common.registry.ModItems;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(TridentItem.class)
public class TridentItemMixin {
	@ModifyExpressionValue(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;isInWaterOrRain()Z"))
	private boolean useRiptide(boolean original, Level world, Player user) {
		return original || (ArmamentsConfig.SeaCrown.enablesRiptide && user.getItemBySlot(EquipmentSlot.HEAD).is(ModItems.SEA_CROWN.get()));
	}

	@ModifyExpressionValue(method = "releaseUsing", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;isInWaterOrRain()Z"))
	private boolean stopUsingRiptide(boolean original, ItemStack stack, Level world, LivingEntity user) {
		return original || (ArmamentsConfig.SeaCrown.enablesRiptide && user.getItemBySlot(EquipmentSlot.HEAD).is(ModItems.SEA_CROWN.get()));
	}
}
