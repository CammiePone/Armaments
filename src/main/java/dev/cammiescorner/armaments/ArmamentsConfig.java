package dev.cammiescorner.armaments;

import com.teamresourceful.resourcefulconfig.common.annotations.*;
import com.teamresourceful.resourcefulconfig.common.config.EntryType;

@Config(Armaments.MOD_ID)
public final class ArmamentsConfig {
	@Category(
		id = "seaCrownConfig",
		translation = "config." + Armaments.MOD_ID + ".sea_crown",
		sortOrder = 0
	)
	public static final class SeaCrown {
		@ConfigEntry(
			id = "enablesRiptide",
			type = EntryType.BOOLEAN,
			translation = "config." + Armaments.MOD_ID + ".sea_crown.enables_riptide"
		)
		public static boolean enablesRiptide = true;

		@ConfigEntry(
			id = "potionAmplifier",
			type = EntryType.INTEGER,
			translation = "config." + Armaments.MOD_ID + ".sea_crown.potion_amplifier"
		)
		@IntRange(min = 0, max = 255)
		public static int potionAmplifier = 0;
	}

	@Category(
		id = "echoDaggerConfig",
		translation = "config." + Armaments.MOD_ID + ".echo_dagger",
		sortOrder = 1
	)
	public static final class EchoDagger {
		@ConfigEntry(
			id = "echoMultiplier",
			type = EntryType.FLOAT,
			translation = "config." + Armaments.MOD_ID + ".echo_dagger.echo_multiplier"
		)
		@FloatRange(min = 0f, max = 0.9f)
		public static float echoMultiplier = 0.5f;

		@ConfigEntry(
			id = "echoDelay",
			type = EntryType.INTEGER,
			translation = "config." + Armaments.MOD_ID + ".echo_dagger.echo_delay"
		)
		public static int echoDelay = 20;

		@ConfigEntry(
			id = "potionDuration",
			type = EntryType.INTEGER,
			translation = "config." + Armaments.MOD_ID + ".echo_dagger.potion_duration"
		)
		public static int potionDuration = 300;
	}

	@Category(
		id = "crystalSpearConfig",
		translation = "config." + Armaments.MOD_ID + ".crystal_spear",
		sortOrder = 2
	)
	public static final class CrystalSpear {
		@ConfigEntry(
			id = "chargeInterval",
			type = EntryType.INTEGER,
			translation = "config." + Armaments.MOD_ID + ".crystal_spear.charge_interval"
		)
		public static int chargeInterval = 40;
	}

	@Category(
		id = "goatHornConfig",
		translation = "config." + Armaments.MOD_ID + ".goat_horn",
		sortOrder = 3
	)
	public static final class GoatHorn {
		@ConfigEntry(
			id = "speedDuration",
			type = EntryType.INTEGER,
			translation = "config." + Armaments.MOD_ID + ".goat_horn.speed_duration"
		)
		public static int speedDuration = 600;

		@ConfigEntry(
			id = "speedAmplifier",
			type = EntryType.INTEGER,
			translation = "config." + Armaments.MOD_ID + ".goat_horn.speed_amplifier"
		)
		@IntRange(min = 0, max = 255)
		public static int speedAmplifier = 1;

		@ConfigEntry(
			id = "resistanceDuration",
			type = EntryType.INTEGER,
			translation = "config." + Armaments.MOD_ID + ".goat_horn.resistance_duration"
		)
		public static int resistanceDuration = 300;

		@ConfigEntry(
			id = "speedAmplifier",
			type = EntryType.INTEGER,
			translation = "config." + Armaments.MOD_ID + ".goat_horn.resistance_amplifier"
		)
		@IntRange(min = 0, max = 255)
		public static int resistanceAmplifier = 0;
	}
}
