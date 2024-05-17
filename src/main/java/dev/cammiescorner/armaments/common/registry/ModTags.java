package dev.cammiescorner.armaments.common.registry;

import dev.cammiescorner.armaments.Armaments;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

public class ModTags {
	public static final TagKey<Item> BLUNDERBUSS_AMMO = TagKey.of(RegistryKeys.ITEM, Armaments.id("blunderbuss_ammo"));
	public static final TagKey<Item> BLUNDERBUSS_PROPELLANT = TagKey.of(RegistryKeys.ITEM, Armaments.id("blunderbuss_propellant"));
	public static final TagKey<Item> HORNS = TagKey.of(RegistryKeys.ITEM, Armaments.id("horns"));
}
