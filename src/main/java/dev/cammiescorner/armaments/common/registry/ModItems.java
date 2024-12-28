package dev.cammiescorner.armaments.common.registry;

import dev.cammiescorner.armaments.Armaments;
import dev.cammiescorner.armaments.common.items.*;
import dev.upcraft.sparkweave.api.registry.RegistryHandler;
import dev.upcraft.sparkweave.api.registry.RegistrySupplier;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.function.Supplier;

public class ModItems {
	public static final RegistryHandler<Item> ITEMS = RegistryHandler.create(Registries.ITEM, Armaments.MOD_ID);

	public static final RegistrySupplier<Item> SEA_CROWN = ITEMS.register("sea_crown", () -> new SeaCrownItem(new FabricItemSettings()));
	public static final RegistrySupplier<Item> CRYSTAL_SPEAR = ITEMS.register("crystal_spear", () -> new CrystalSpearItem(ToolMaterials.AMETHYST, new FabricItemSettings()));
//	public static final RegistrySupplier<Item> BLUNDERBUSS = ITEMS.register("blunderbuss", () -> new BlunderbussItem(new FabricItemSettings().maxCount(1)));
	public static final RegistrySupplier<Item> ECHO_DAGGER = ITEMS.register("echo_dagger", () -> new EchoDaggerItem(new FabricItemSettings().stacksTo(1)));
//	public static final RegistrySupplier<Item> COPPER_GAUNTLET = ITEMS.register("copper_gauntlet", () -> new CopperGauntletItem(new FabricItemSettings().maxCount(1)));
	public static final RegistrySupplier<Item> ELDER_GUARDIAN_SPIKE = ITEMS.register("elder_guardian_spike", () -> new ElderGuardianSpikeItem(new FabricItemSettings().durability(64)));

	public enum ToolMaterials implements Tier {
		AMETHYST(1, 193, 5f, 7f, 22, () -> Ingredient.of(Items.AMETHYST_SHARD));

		private final int miningLevel;
		private final int itemDurability;
		private final float miningSpeed;
		private final float attackDamage;
		private final int enchantability;
		private final LazyLoadedValue<Ingredient> repairIngredient;

		ToolMaterials(int miningLevel, int itemDurability, float miningSpeed, float attackDamage, int enchantability, Supplier<Ingredient> repairIngredient) {
			this.miningLevel = miningLevel;
			this.itemDurability = itemDurability;
			this.miningSpeed = miningSpeed;
			this.attackDamage = attackDamage;
			this.enchantability = enchantability;
			this.repairIngredient = new LazyLoadedValue<>(repairIngredient);
		}

		public int getUses() {
			return itemDurability;
		}

		public float getSpeed() {
			return miningSpeed;
		}

		public float getAttackDamageBonus() {
			return attackDamage;
		}

		public int getLevel() {
			return miningLevel;
		}

		public int getEnchantmentValue() {
			return enchantability;
		}

		public Ingredient getRepairIngredient() {
			return repairIngredient.get();
		}
	}
}
