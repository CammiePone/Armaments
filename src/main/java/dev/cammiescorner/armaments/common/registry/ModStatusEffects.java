package dev.cammiescorner.armaments.common.registry;

import dev.cammiescorner.armaments.Armaments;
import dev.cammiescorner.armaments.common.status_effects.ModStatusEffect;
import dev.upcraft.sparkweave.api.registry.RegistryHandler;
import dev.upcraft.sparkweave.api.registry.RegistrySupplier;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.registry.RegistryKeys;

public class ModStatusEffects {
	public static final RegistryHandler<StatusEffect> STATUS_EFFECTS = RegistryHandler.create(RegistryKeys.STATUS_EFFECT, Armaments.MOD_ID);

	public static final RegistrySupplier<StatusEffect> ECHO = STATUS_EFFECTS.register("echo", () -> new ModStatusEffect(StatusEffectType.NEUTRAL, 0x3a8e99));
}
