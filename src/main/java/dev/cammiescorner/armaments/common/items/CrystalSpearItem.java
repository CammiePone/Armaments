package dev.cammiescorner.armaments.common.items;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import dev.cammiescorner.armaments.common.components.item.CrystalSpearComponent;
import dev.cammiescorner.armaments.common.registry.ModComponents;
import net.fabricmc.fabric.api.item.v1.FabricItem;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.Vanishable;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CrystalSpearItem extends ToolItem implements Vanishable, SpecialRenderItem, FabricItem {
	public CrystalSpearItem(ToolMaterial material, Settings settings) {
		super(material, settings);
	}

	@Override
	public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
		stack.damage(1, attacker, e -> e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));
		return true;
	}

	@Override
	public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
		if(state.getHardness(world, pos) != 0.0F)
			stack.damage(2, miner, e -> e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));

		return true;
	}

	@Override
	public boolean canMine(BlockState state, World world, BlockPos pos, PlayerEntity miner) {
		return !miner.isCreative();
	}

	@Override
	public boolean allowNbtUpdateAnimation(PlayerEntity player, Hand hand, ItemStack oldStack, ItemStack newStack) {
		CrystalSpearComponent component = ModComponents.CRYSTAL_SPEAR.get(newStack);

		return component.getCharge() != 0 && super.allowNbtUpdateAnimation(player, hand, oldStack, newStack);
	}

	@Override
	public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(ItemStack stack, EquipmentSlot slot) {
		if(slot == EquipmentSlot.MAINHAND) {
			ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
			double attackDamage = 10 * (1 + (ModComponents.CRYSTAL_SPEAR.get(stack).getCharge() / 4d));

			builder.put(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(ATTACK_DAMAGE_MODIFIER_ID, "Weapon modifier", attackDamage - 1, EntityAttributeModifier.Operation.ADDITION));
			builder.put(EntityAttributes.GENERIC_ATTACK_SPEED, new EntityAttributeModifier(ATTACK_SPEED_MODIFIER_ID, "Weapon modifier", -3.2, EntityAttributeModifier.Operation.ADDITION));
			return builder.build();
		}

		return super.getAttributeModifiers(stack, slot);
	}
}
