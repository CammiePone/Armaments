package dev.cammiescorner.armaments;

import dev.cammiescorner.armaments.common.components.item.CrystalSpearComponent;
import dev.cammiescorner.armaments.common.items.CrystalSpearItem;
import dev.cammiescorner.armaments.common.registry.*;
import dev.upcraft.sparkweave.api.registry.RegistryService;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.HorseBaseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Holder;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
import org.quiltmc.qsl.entity.event.api.ServerEntityTickCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Armaments implements ModInitializer {
	public static final String MOD_ID = "armaments";
	public static final Logger LOGGER = LoggerFactory.getLogger("Armaments");
	public static final RegistryKey<DamageType> ECHO = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, id("echo"));

	@Override
	public void onInitialize(ModContainer mod) {
		RegistryService registryService = RegistryService.get();

		ModItems.ITEMS.accept(registryService);
		ModRecipes.RECIPE_SERIALIZERS.accept(registryService);
		ModStatusEffects.STATUS_EFFECTS.accept(registryService);

		ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> {
			entries.addAfter(Items.TURTLE_HELMET, ModItems.SEA_CROWN.get());
			entries.addAfter(Items.TRIDENT, ModItems.CRYSTAL_SPEAR.get(), ModItems.ECHO_DAGGER.get());
			entries.addAfter(Items.CROSSBOW, ModItems.BLUNDERBUSS.get());
		});

		UseItemCallback.EVENT.register((player, world, hand) -> {
			ItemStack stack = player.getStackInHand(hand);

			if(stack.isIn(ModTags.HORNS) && player.getVehicle() instanceof HorseBaseEntity honse) {
				honse.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 600, 1, true, false));
				honse.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 300, 0, true, false));
			}

			return TypedActionResult.pass(stack);
		});

		ServerEntityTickCallback.EVENT.register((entity, isPassengerTick) -> {
			if(entity.getWorld() instanceof ServerWorld world && entity instanceof LivingEntity rider) {
				if(rider.getMainHandStack().getItem() instanceof CrystalSpearItem && rider.hasVehicle() && rider.getControlledVehicle() instanceof HorseBaseEntity) {
					ItemStack stack = rider.getMainHandStack();
					CrystalSpearComponent component = ModComponents.CRYSTAL_SPEAR.get(stack);
					long timer = world.getTime() - component.startTime();
					int interval = 40;

					if(rider.forwardSpeed > 0) {
						if(component.getCharge() == 0 && component.startTime() + interval < world.getTime())
							component.setStartTime(world.getTime());

						if(timer % interval == 0 && component.getCharge() < 4) {
							component.setCharge(component.getCharge() + 1);
							world.playSound(null, rider.getX(), rider.getY(), rider.getZ(), SoundEvents.BLOCK_RESPAWN_ANCHOR_CHARGE, SoundCategory.NEUTRAL, 1f, component.getCharge() / 4f);

							if(rider instanceof PlayerEntity player) {
								String charge = "⬤";
								String bar = "◯";

								Formatting formatting = switch(component.getCharge()) {
									default -> Formatting.RED;
									case 2 -> Formatting.GOLD;
									case 3 -> Formatting.YELLOW;
									case 4 -> Formatting.GREEN;
								};

								player.sendMessage(Text.of(charge.repeat(component.getCharge()) + bar.repeat(4 - component.getCharge())).copy().formatted(formatting), true);
							}
						}
					}
					else if(component.getCharge() > 0) {
						component.setCharge(0);
						world.playSound(null, rider.getX(), rider.getY(), rider.getZ(), SoundEvents.BLOCK_RESPAWN_ANCHOR_CHARGE, SoundCategory.NEUTRAL, 1f, 0f);

						if(rider instanceof PlayerEntity player)
							player.sendMessage(Text.of("◯◯◯◯").copy().formatted(Formatting.DARK_RED), true);
					}
				}

				if(rider instanceof PlayerEntity player) {
					PlayerInventory inv = player.getInventory();

					for(int i = 0; i < inv.size(); i++) {
						ItemStack stack = inv.getStack(i);

						if(!(stack.getItem() instanceof CrystalSpearItem))
							continue;

						CrystalSpearComponent component = ModComponents.CRYSTAL_SPEAR.get(stack);

						if((!player.getMainHandStack().equals(stack) || !player.hasVehicle()) && component.getCharge() > 0)
							component.setCharge(0);
					}
				}
			}
		});
	}

	public static Identifier id(String name) {
		return new Identifier(MOD_ID, name);
	}

	public static MutableText translate(@Nullable String prefix, String... value) {
		return Text.translatable(translationKey(prefix, value));
	}

	public static String translationKey(@Nullable String prefix, String... value) {
		String translationKey = MOD_ID + "." + String.join(".", value);
		return prefix != null ? (prefix + "." + translationKey) : translationKey;
	}

	public static Holder<DamageType> echo(World world) {
		return world.getRegistryManager().get(RegistryKeys.DAMAGE_TYPE).getHolderOrThrow(Armaments.ECHO);
	}

	public static DamageSource echoDamage(World world) {
		return new DamageSource(echo(world));
	}
}
