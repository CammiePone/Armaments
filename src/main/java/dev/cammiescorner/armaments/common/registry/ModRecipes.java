package dev.cammiescorner.armaments.common.registry;

import dev.cammiescorner.armaments.Armaments;
import dev.cammiescorner.armaments.common.recipes.PotionCrownRecipe;
import dev.upcraft.sparkweave.api.registry.RegistryHandler;
import dev.upcraft.sparkweave.api.registry.RegistrySupplier;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.registry.RegistryKeys;

public class ModRecipes {
	public static final RegistryHandler<RecipeSerializer<?>> RECIPE_SERIALIZERS = RegistryHandler.create(RegistryKeys.RECIPE_SERIALIZER, Armaments.MOD_ID);
	public static final RegistrySupplier<SpecialRecipeSerializer<PotionCrownRecipe>> POTION_CROWN = RECIPE_SERIALIZERS.register("potion_crown", () -> new SpecialRecipeSerializer<>(PotionCrownRecipe::new));
}
