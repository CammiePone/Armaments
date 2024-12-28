package dev.cammiescorner.armaments;

import com.teamresourceful.resourcefulconfig.common.config.Configurator;
import dev.cammiescorner.armaments.common.registry.ModItems;
import dev.cammiescorner.armaments.common.registry.ModRecipes;
import dev.cammiescorner.armaments.common.registry.ModStatusEffects;
import dev.cammiescorner.armaments.common.registry.ModTags;
import dev.upcraft.sparkweave.api.registry.RegistryService;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Armaments implements ModInitializer {
	public static final String MOD_ID = "armaments";
	public static final Logger LOGGER = LoggerFactory.getLogger("Armaments");
	public static final ResourceKey<DamageType> ECHO = ResourceKey.create(Registries.DAMAGE_TYPE, id("echo"));
	public static final ResourceKey<DamageType> POKEY = ResourceKey.create(Registries.DAMAGE_TYPE, id("pokey"));
	public static final ResourceLocation ELDER_GUARDIAN = new ResourceLocation("entities/elder_guardian");
	public static final Configurator configurator = new Configurator();

	@Override
	public void onInitialize() {
		RegistryService registryService = RegistryService.get();
		configurator.registerConfig(ArmamentsConfig.class);

		ModItems.ITEMS.accept(registryService);
		ModRecipes.RECIPE_SERIALIZERS.accept(registryService);
		ModStatusEffects.STATUS_EFFECTS.accept(registryService);

		ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.COMBAT).register(entries -> {
			entries.addAfter(Items.TURTLE_HELMET, ModItems.SEA_CROWN.get());
			entries.addAfter(Items.TRIDENT, ModItems.CRYSTAL_SPEAR.get(), ModItems.ECHO_DAGGER.get(), ModItems.ELDER_GUARDIAN_SPIKE.get());
//			entries.addAfter(Items.CROSSBOW, ModItems.BLUNDERBUSS.get());
		});

		ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.INGREDIENTS).register(entries -> entries.addAfter(Items.GHAST_TEAR, ModItems.ELDER_GUARDIAN_SPIKE.get()));

		LootTableEvents.MODIFY.register((resourceManager, lootManager, id, tableBuilder, source) -> {
			if(source.isBuiltin() && ELDER_GUARDIAN.equals(id)) {
				LootPool.Builder builder = LootPool.lootPool().add(LootItem.lootTableItem(ModItems.ELDER_GUARDIAN_SPIKE.get()));

				tableBuilder.withPool(builder);
			}
		});

		UseItemCallback.EVENT.register((player, world, hand) -> {
			ItemStack stack = player.getItemInHand(hand);

			if(stack.is(ModTags.HORNS) && player.getVehicle() instanceof AbstractHorse honse) {
				honse.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, ArmamentsConfig.GoatHorn.speedDuration, ArmamentsConfig.GoatHorn.speedAmplifier, true, false));
				honse.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, ArmamentsConfig.GoatHorn.resistanceDuration, ArmamentsConfig.GoatHorn.resistanceAmplifier, true, false));
			}

			return InteractionResultHolder.pass(stack);
		});
	}

	public static ResourceLocation id(String name) {
		return new ResourceLocation(MOD_ID, name);
	}

	public static MutableComponent translate(@Nullable String prefix, String... value) {
		return Component.translatable(translationKey(prefix, value));
	}

	public static String translationKey(@Nullable String prefix, String... value) {
		String translationKey = MOD_ID + "." + String.join(".", value);
		return prefix != null ? (prefix + "." + translationKey) : translationKey;
	}

	public static Holder<DamageType> getDamageTypeHolder(Level world, ResourceKey<DamageType> key) {
		return world.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(key);
	}

	public static DamageSource echoDamage(Level world) {
		return new DamageSource(getDamageTypeHolder(world, Armaments.ECHO));
	}

	public static DamageSource pokeyDamage(Level world, Entity source) {
		return new DamageSource(getDamageTypeHolder(world, Armaments.POKEY), source);
	}
}
