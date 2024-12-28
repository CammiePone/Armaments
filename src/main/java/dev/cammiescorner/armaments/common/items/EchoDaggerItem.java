package dev.cammiescorner.armaments.common.items;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import dev.cammiescorner.armaments.Armaments;
import dev.cammiescorner.armaments.ArmamentsConfig;
import dev.cammiescorner.armaments.common.registry.ModStatusEffects;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Vanishable;
import net.minecraft.world.level.Level;

public class EchoDaggerItem extends Item implements Vanishable {
	private static final int MAX_CHARGE = 100;
	private final Multimap<Attribute, AttributeModifier> attributeModifiers;

	public EchoDaggerItem(Properties settings) {
		super(settings);

		ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
		builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", 1, AttributeModifier.Operation.ADDITION));
		builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", 0, AttributeModifier.Operation.ADDITION));
		this.attributeModifiers = builder.build();
	}

	@Override
	public boolean isBarVisible(ItemStack stack) {
		return true;
	}

	@Override
	public int getBarWidth(ItemStack stack) {
		return Math.round(13f - (100 - getCharge(stack)) * 13f / MAX_CHARGE);
	}

	@Override
	public int getBarColor(ItemStack stack) {
		return 0x009295;
	}

	@Override
	public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
		if(!attacker.level().isClientSide() && (isUsable(stack) || (attacker instanceof Player player && player.isCreative())))
			target.addEffect(new MobEffectInstance(ModStatusEffects.ECHO.get(), ArmamentsConfig.EchoDagger.potionDuration, 0, true, false, true), attacker);

		return true;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
		ItemStack stack = user.getItemInHand(hand).copy();

		if(user.isShiftKeyDown()) {
			if(isUsable(stack) || user.isCreative()) {
				user.hurt(Armaments.echoDamage(world), 2);
				user.addEffect(new MobEffectInstance(ModStatusEffects.ECHO.get(), ArmamentsConfig.EchoDagger.potionDuration, 0, true, false, true), user);
				return InteractionResultHolder.sidedSuccess(stack, world.isClientSide);
			}
			else {
				Inventory inv = user.getInventory();

				for(int i = 0; i < inv.getContainerSize(); i++) {
					ItemStack itemStack = inv.getItem(i);

					if(itemStack.is(Items.ECHO_SHARD)) {
						itemStack.shrink(1);
						resetCharge(stack);
						return InteractionResultHolder.sidedSuccess(stack, world.isClientSide);
					}
				}
			}
		}

		return super.use(world, user, hand);
	}

	@Override
	public void inventoryTick(ItemStack stack, Level world, Entity entity, int slot, boolean selected) {
		if(isUsable(stack))
			decrementCharge(stack);
	}

	@Override
	public boolean isFoil(ItemStack stack) {
		return super.isFoil(stack) || isUsable(stack);
	}

	@Override
	public boolean allowNbtUpdateAnimation(Player player, InteractionHand hand, ItemStack oldStack, ItemStack newStack) {
		return !isUsable(newStack);
	}

	@Override
	public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot slot) {
		return slot == EquipmentSlot.MAINHAND ? this.attributeModifiers : super.getDefaultAttributeModifiers(slot);
	}

	public static boolean isUsable(ItemStack stack) {
		return getCharge(stack) > 0;
	}

	public static int getCharge(ItemStack stack) {
		CompoundTag tag = stack.getOrCreateTag();

		return tag.getInt("Charge");
	}

	public static void setCharge(ItemStack stack, int value) {
		CompoundTag tag = stack.getOrCreateTag();
		tag.putInt("Charge", Mth.clamp(value, 0, MAX_CHARGE));
	}

	public static void decrementCharge(ItemStack stack) {
		setCharge(stack, getCharge(stack) - 1);
	}

	public static void resetCharge(ItemStack stack) {
		setCharge(stack, MAX_CHARGE);
	}
}
