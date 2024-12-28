package dev.cammiescorner.armaments.common.registry;

import dev.cammiescorner.armaments.Armaments;
import dev.cammiescorner.armaments.common.status_effects.ModStatusEffect;
import dev.upcraft.sparkweave.api.registry.RegistryHandler;
import dev.upcraft.sparkweave.api.registry.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class ModStatusEffects {
	public static final RegistryHandler<MobEffect> STATUS_EFFECTS = RegistryHandler.create(Registries.MOB_EFFECT, Armaments.MOD_ID);

	public static final RegistrySupplier<MobEffect> ECHO = STATUS_EFFECTS.register("echo", () -> new ModStatusEffect(MobEffectCategory.NEUTRAL, 0x3a8e99));
}
