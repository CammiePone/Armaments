package dev.cammiescorner.armaments.common.items;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import dev.cammiescorner.armaments.Armaments;
import dev.cammiescorner.armaments.common.registry.ModStatusEffects;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.Vanishable;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class EchoDaggerItem extends Item implements Vanishable {
	private final Multimap<EntityAttribute, EntityAttributeModifier> attributeModifiers;

	public EchoDaggerItem(Settings settings) {
		super(settings);

		ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
		builder.put(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(ATTACK_DAMAGE_MODIFIER_ID, "Weapon modifier", 1, EntityAttributeModifier.Operation.ADDITION));
		builder.put(EntityAttributes.GENERIC_ATTACK_SPEED, new EntityAttributeModifier(ATTACK_SPEED_MODIFIER_ID, "Weapon modifier", 0, EntityAttributeModifier.Operation.ADDITION));
		this.attributeModifiers = builder.build();
	}

	@Override
	public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
		if(!attacker.getWorld().isClient() && isUsable(stack))
			target.addStatusEffect(new StatusEffectInstance(ModStatusEffects.ECHO.get(), 100, 0, true, false, true), attacker);

		return true;
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		ItemStack stack = user.getStackInHand(hand).copy();

		if(user.isSneaking()) {
			if(isUsable(stack)) {
				user.damage(Armaments.echoDamage(world), 2);
				user.addStatusEffect(new StatusEffectInstance(ModStatusEffects.ECHO.get(), 200, 0, true, false, true), user);
				return TypedActionResult.success(stack, world.isClient);
			}
			else {
				if(user.isCreative()) {
					stack.setDamage(0);
					return TypedActionResult.success(stack, world.isClient);
				}

				PlayerInventory inv = user.getInventory();

				for(int i = 0; i < inv.size(); i++) {
					ItemStack itemStack = inv.getStack(i);

					if(itemStack.isOf(Items.ECHO_SHARD)) {
						itemStack.decrement(1);
						stack.setDamage(0);
						return TypedActionResult.success(stack, world.isClient);
					}
				}
			}
		}

		return super.use(world, user, hand);
	}

	@Override
	public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
		if(entity instanceof PlayerEntity player && isUsable(stack))
			stack.damage(1, player, e -> e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));
	}

	@Override
	public boolean hasGlint(ItemStack stack) {
		return super.hasGlint(stack) || isUsable(stack);
	}

	@Override
	public boolean allowNbtUpdateAnimation(PlayerEntity player, Hand hand, ItemStack oldStack, ItemStack newStack) {
		return !isUsable(newStack);
	}

	@Override
	public void onCraft(ItemStack stack, World world, PlayerEntity player) {
		stack.setDamage(stack.getMaxDamage() - 1);
	}

	@Override
	public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot slot) {
		return slot == EquipmentSlot.MAINHAND ? this.attributeModifiers : super.getAttributeModifiers(slot);
	}

	public static boolean isUsable(ItemStack stack) {
		return stack.getDamage() < stack.getMaxDamage() - 1;
	}
}
