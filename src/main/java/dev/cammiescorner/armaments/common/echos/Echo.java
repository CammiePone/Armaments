package dev.cammiescorner.armaments.common.echos;

import org.jetbrains.annotations.NotNull;

public record Echo(@NotNull Type type, float amount, long time) {
	public enum Type {
		HEAL, DAMAGE
	}
}
