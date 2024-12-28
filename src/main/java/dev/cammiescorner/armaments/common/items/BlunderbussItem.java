package dev.cammiescorner.armaments.common.items;

import dev.cammiescorner.armaments.common.registry.ModTags;
import java.util.function.Predicate;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

public class BlunderbussItem extends Item implements SpecialRenderItem {
	private static final Predicate<ItemStack> AMMO = stack -> stack.is(ModTags.BLUNDERBUSS_AMMO);
	private static final Predicate<ItemStack> PROPELLANT = stack -> stack.is(ModTags.BLUNDERBUSS_PROPELLANT);

	public BlunderbussItem(Item.Properties settings) {
		super(settings);
	}

	@Override
	public void onUseTick(Level world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
		if(!world.isClientSide && user instanceof Player player) {
			if(!isLoaded(stack)) {
				ItemStack propellant = findItem(player, PROPELLANT);

				if(remainingUseTicks != getUseDuration(stack) && remainingUseTicks % 20 == 0 && (PROPELLANT.test(propellant) || player.isCreative())) {
					setCharge(stack, getCharge(stack) + 1);

					if(!player.isCreative())
						propellant.shrink(1);

					System.out.println(getCharge(stack));
				}
			}
			else {
				stack.getOrCreateTag().putBoolean("Loaded", false);
				setCharge(stack, 0);
			}
		}
	}

	@Override
	public void releaseUsing(ItemStack stack, Level world, LivingEntity user, int remainingUseTicks) {
		if(!world.isClientSide) {
			boolean loaded = isLoaded(stack);

			if((!(user instanceof Player player) || (AMMO.test(findItem(player, AMMO)) || player.isCreative()))) {
				if(getCharge(stack) > 0 && !loaded) {
					loadGun(stack);
				}
			}
		}
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
		ItemStack stack = user.getItemInHand(hand);
		boolean loaded = isLoaded(stack);

		if(loaded || AMMO.test(findItem(user, AMMO)) || user.isCreative()) {
			user.startUsingItem(hand);
			return InteractionResultHolder.success(stack);
		}

		return InteractionResultHolder.fail(stack);
	}

	@Override
	public int getUseDuration(ItemStack stack) {
		return 72000;
	}

	@Override
	public UseAnim getUseAnimation(ItemStack stack) {
		return UseAnim.CROSSBOW;
	}

	public ItemStack findItem(Player player, Predicate<ItemStack> predicate) {
		Inventory inv = player.getInventory();
		ItemStack returnStack = ItemStack.EMPTY;

		for(int i = 0; i < inv.getContainerSize(); i++) {
			ItemStack stack = inv.getItem(i);

			if(predicate.test(stack)) {
				returnStack = stack;
				break;
			}
		}

		return returnStack;
	}

	public void loadGun(ItemStack stack) {
		CompoundTag tag = stack.getOrCreateTag();
		tag.putBoolean("Loaded", true);
	}

	public boolean isLoaded(ItemStack stack) {
		CompoundTag tag = stack.getOrCreateTag();
		return tag.getBoolean("Loaded");
	}

	public void setCharge(ItemStack stack, int charge) {
		CompoundTag tag = stack.getOrCreateTag();
		tag.putInt("Charge", charge);
	}

	public int getCharge(ItemStack stack) {
		CompoundTag tag = stack.getOrCreateTag();
		return tag.getInt("Charge");
	}

	public Predicate<ItemStack> getHeldProjectiles() {
		return AMMO;
	}
}
