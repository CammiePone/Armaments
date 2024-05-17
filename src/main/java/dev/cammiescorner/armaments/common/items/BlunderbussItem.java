package dev.cammiescorner.armaments.common.items;

import dev.cammiescorner.armaments.common.registry.ModTags;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

import java.util.function.Predicate;

public class BlunderbussItem extends Item implements SpecialRenderItem {
	private static final Predicate<ItemStack> AMMO = stack -> stack.isIn(ModTags.BLUNDERBUSS_AMMO);
	private static final Predicate<ItemStack> PROPELLANT = stack -> stack.isIn(ModTags.BLUNDERBUSS_PROPELLANT);

	public BlunderbussItem(Item.Settings settings) {
		super(settings);
	}

	@Override
	public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
		if(!world.isClient && user instanceof PlayerEntity player) {
			if(!isLoaded(stack)) {
				ItemStack propellant = findItem(player, PROPELLANT);

				if(remainingUseTicks != getMaxUseTime(stack) && remainingUseTicks % 20 == 0 && (PROPELLANT.test(propellant) || player.isCreative())) {
					setCharge(stack, getCharge(stack) + 1);

					if(!player.isCreative())
						propellant.decrement(1);

					System.out.println(getCharge(stack));
				}
			}
			else {
				stack.getOrCreateNbt().putBoolean("Loaded", false);
				setCharge(stack, 0);
			}
		}
	}

	@Override
	public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
		if(!world.isClient) {
			boolean loaded = isLoaded(stack);

			if((!(user instanceof PlayerEntity player) || (AMMO.test(findItem(player, AMMO)) || player.isCreative()))) {
				if(getCharge(stack) > 0 && !loaded) {
					loadGun(stack);
				}
			}
		}
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		ItemStack stack = user.getStackInHand(hand);
		boolean loaded = isLoaded(stack);

		if(loaded || AMMO.test(findItem(user, AMMO)) || user.isCreative()) {
			user.setCurrentHand(hand);
			return TypedActionResult.success(stack);
		}

		return TypedActionResult.fail(stack);
	}

	@Override
	public int getMaxUseTime(ItemStack stack) {
		return 72000;
	}

	@Override
	public UseAction getUseAction(ItemStack stack) {
		return UseAction.CROSSBOW;
	}

	public ItemStack findItem(PlayerEntity player, Predicate<ItemStack> predicate) {
		PlayerInventory inv = player.getInventory();
		ItemStack returnStack = ItemStack.EMPTY;

		for(int i = 0; i < inv.size(); i++) {
			ItemStack stack = inv.getStack(i);

			if(predicate.test(stack)) {
				returnStack = stack;
				break;
			}
		}

		return returnStack;
	}

	public void loadGun(ItemStack stack) {
		NbtCompound tag = stack.getOrCreateNbt();
		tag.putBoolean("Loaded", true);
	}

	public boolean isLoaded(ItemStack stack) {
		NbtCompound tag = stack.getOrCreateNbt();
		return tag.getBoolean("Loaded");
	}

	public void setCharge(ItemStack stack, int charge) {
		NbtCompound tag = stack.getOrCreateNbt();
		tag.putInt("Charge", charge);
	}

	public int getCharge(ItemStack stack) {
		NbtCompound tag = stack.getOrCreateNbt();
		return tag.getInt("Charge");
	}

	public Predicate<ItemStack> getHeldProjectiles() {
		return AMMO;
	}
}
