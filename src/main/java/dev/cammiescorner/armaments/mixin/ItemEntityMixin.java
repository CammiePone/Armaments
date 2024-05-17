package dev.cammiescorner.armaments.mixin;

import dev.cammiescorner.armaments.common.components.item.CrystalSpearComponent;
import dev.cammiescorner.armaments.common.items.CrystalSpearItem;
import dev.cammiescorner.armaments.common.registry.ModComponents;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin {
	@ModifyArg(method = "<init>(Lnet/minecraft/world/World;DDDLnet/minecraft/item/ItemStack;DDD)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ItemEntity;setStack(Lnet/minecraft/item/ItemStack;)V"))
	private ItemStack createItem(ItemStack stack) {
		if(stack.getItem() instanceof CrystalSpearItem) {
			CrystalSpearComponent component = ModComponents.CRYSTAL_SPEAR.get(stack);
			component.setCharge(0);
		}

		return stack;
	}
}
