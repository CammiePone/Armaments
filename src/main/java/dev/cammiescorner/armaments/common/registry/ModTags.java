package dev.cammiescorner.armaments.common.registry;

import dev.cammiescorner.armaments.Armaments;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class ModTags {
	public static final TagKey<Item> BLUNDERBUSS_AMMO = TagKey.create(Registries.ITEM, Armaments.id("blunderbuss_ammo"));
	public static final TagKey<Item> BLUNDERBUSS_PROPELLANT = TagKey.create(Registries.ITEM, Armaments.id("blunderbuss_propellant"));
	public static final TagKey<Item> HORNS = TagKey.create(Registries.ITEM, Armaments.id("horns"));
}
