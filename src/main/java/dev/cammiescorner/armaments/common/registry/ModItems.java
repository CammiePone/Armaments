package dev.cammiescorner.armaments.common.registry;

import dev.cammiescorner.armaments.Armaments;
import dev.cammiescorner.armaments.common.items.*;
import dev.upcraft.sparkweave.api.registry.RegistryHandler;
import dev.upcraft.sparkweave.api.registry.RegistrySupplier;
import net.minecraft.item.*;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Lazy;
import org.quiltmc.qsl.item.setting.api.QuiltItemSettings;

import java.util.function.Supplier;

public class ModItems {
	public static final RegistryHandler<Item> ITEMS = RegistryHandler.create(RegistryKeys.ITEM, Armaments.MOD_ID);

	public static final RegistrySupplier<Item> SEA_CROWN = ITEMS.register("sea_crown", () -> new SeaCrownItem(new QuiltItemSettings()));
	public static final RegistrySupplier<Item> CRYSTAL_SPEAR = ITEMS.register("crystal_spear", () -> new CrystalSpearItem(ToolMaterials.AMETHYST, new QuiltItemSettings()));
	public static final RegistrySupplier<Item> BLUNDERBUSS = ITEMS.register("blunderbuss", () -> new BlunderbussItem(new QuiltItemSettings().maxCount(1)));
	public static final RegistrySupplier<Item> ECHO_DAGGER = ITEMS.register("echo_dagger", () -> new EchoDaggerItem(new QuiltItemSettings().maxDamage(400)));
	public static final RegistrySupplier<Item> COPPER_GAUNTLET = ITEMS.register("copper_gauntlet", () -> new CopperGauntletItem(new QuiltItemSettings().maxCount(1)));
	public static final RegistrySupplier<Item> ELDER_GUARDIAN_SPIKE = ITEMS.register("elder_guardian_spike", () -> new ElderGuardianSpikeItem(new QuiltItemSettings().maxDamage(64)));

	public enum ToolMaterials implements ToolMaterial {
		AMETHYST(1, 193, 5f, 7f, 22, () -> Ingredient.ofItems(Items.AMETHYST_SHARD));

		private final int miningLevel;
		private final int itemDurability;
		private final float miningSpeed;
		private final float attackDamage;
		private final int enchantability;
		private final Lazy<Ingredient> repairIngredient;

		ToolMaterials(int miningLevel, int itemDurability, float miningSpeed, float attackDamage, int enchantability, Supplier<Ingredient> repairIngredient) {
			this.miningLevel = miningLevel;
			this.itemDurability = itemDurability;
			this.miningSpeed = miningSpeed;
			this.attackDamage = attackDamage;
			this.enchantability = enchantability;
			this.repairIngredient = new Lazy<>(repairIngredient);
		}

		public int getDurability() {
			return itemDurability;
		}

		public float getMiningSpeedMultiplier() {
			return miningSpeed;
		}

		public float getAttackDamage() {
			return attackDamage;
		}

		public int getMiningLevel() {
			return miningLevel;
		}

		public int getEnchantability() {
			return enchantability;
		}

		public Ingredient getRepairIngredient() {
			return repairIngredient.get();
		}
	}
}
