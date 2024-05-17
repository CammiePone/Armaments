package dev.cammiescorner.armaments.common.recipes;

import dev.cammiescorner.armaments.common.registry.ModItems;
import dev.cammiescorner.armaments.common.registry.ModRecipes;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.recipe.CraftingCategory;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class PotionCrownRecipe extends SpecialCraftingRecipe {
	public PotionCrownRecipe(Identifier id, CraftingCategory category) {
		super(id, category);
	}

	@Override
	public boolean matches(RecipeInputInventory inventory, World world) {
		boolean hasCrown = false;
		boolean hasPotion = false;

		for(ItemStack stack : inventory.getIngredients()) {
			if(stack.isOf(ModItems.SEA_CROWN.get())) {
				if(hasCrown)
					return false;

				hasCrown = true;
			}
			if(stack.isOf(Items.POTION)) {
				Potion potion = PotionUtil.getPotion(stack);

				if(hasPotion || potion == Potions.EMPTY || potion.hasInstantEffect())
					return false;

				hasPotion = true;
			}
		}

		return hasCrown && hasPotion;
	}

	@Override
	public ItemStack craft(RecipeInputInventory inventory, DynamicRegistryManager registryManager) {
		Potion potion = Potions.EMPTY;
		ItemStack seaCrown = ItemStack.EMPTY;

		for(ItemStack stack : inventory.getIngredients()) {
			if(stack.isOf(ModItems.SEA_CROWN.get()))
				seaCrown = stack.copy();
			if(stack.isOf(Items.POTION))
				potion = PotionUtil.getPotion(stack);
		}

		return potion != Potions.EMPTY && !seaCrown.isEmpty() ? PotionUtil.setPotion(seaCrown, potion) : ItemStack.EMPTY;
	}

	@Override
	public boolean fits(int width, int height) {
		return width * height >= 2;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return ModRecipes.POTION_CROWN.get();
	}
}
