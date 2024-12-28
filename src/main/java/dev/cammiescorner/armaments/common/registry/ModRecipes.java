package dev.cammiescorner.armaments.common.registry;

import dev.cammiescorner.armaments.Armaments;
import dev.cammiescorner.armaments.common.recipes.PotionCrownRecipe;
import dev.upcraft.sparkweave.api.registry.RegistryHandler;
import dev.upcraft.sparkweave.api.registry.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;

public class ModRecipes {
	public static final RegistryHandler<RecipeSerializer<?>> RECIPE_SERIALIZERS = RegistryHandler.create(Registries.RECIPE_SERIALIZER, Armaments.MOD_ID);
	public static final RegistrySupplier<SimpleCraftingRecipeSerializer<PotionCrownRecipe>> POTION_CROWN = RECIPE_SERIALIZERS.register("potion_crown", () -> new SimpleCraftingRecipeSerializer<>(PotionCrownRecipe::new));
}
