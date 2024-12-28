package dev.cammiescorner.armaments.common.recipes;

import dev.cammiescorner.armaments.common.registry.ModItems;
import dev.cammiescorner.armaments.common.registry.ModRecipes;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

public class PotionCrownRecipe extends CustomRecipe {
	public PotionCrownRecipe(ResourceLocation id, CraftingBookCategory category) {
		super(id, category);
	}

	@Override
	public boolean matches(CraftingContainer inventory, Level world) {
		boolean hasCrown = false;
		boolean hasPotion = false;

		for(ItemStack stack : inventory.getItems()) {
			if(stack.is(ModItems.SEA_CROWN.get())) {
				if(hasCrown)
					return false;

				hasCrown = true;
			}
			if(stack.is(Items.POTION)) {
				Potion potion = PotionUtils.getPotion(stack);

				if(hasPotion || potion == Potions.EMPTY || potion.hasInstantEffects())
					return false;

				hasPotion = true;
			}
		}

		return hasCrown && hasPotion;
	}

	@Override
	public ItemStack assemble(CraftingContainer inventory, RegistryAccess registryManager) {
		Potion potion = Potions.EMPTY;
		ItemStack seaCrown = ItemStack.EMPTY;

		for(ItemStack stack : inventory.getItems()) {
			if(stack.is(ModItems.SEA_CROWN.get()))
				seaCrown = stack.copy();
			if(stack.is(Items.POTION))
				potion = PotionUtils.getPotion(stack);
		}

		return potion != Potions.EMPTY && !seaCrown.isEmpty() ? PotionUtils.setPotion(seaCrown, potion) : ItemStack.EMPTY;
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return width * height >= 2;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return ModRecipes.POTION_CROWN.get();
	}
}
