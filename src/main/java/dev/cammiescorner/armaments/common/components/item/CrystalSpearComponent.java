package dev.cammiescorner.armaments.common.components.item;

import dev.onyxstudios.cca.api.v3.item.ItemComponent;
import net.minecraft.item.ItemStack;

public class CrystalSpearComponent extends ItemComponent {
	public CrystalSpearComponent(ItemStack stack) {
		super(stack);
	}

	public int getCharge() {
		return Math.min(getInt("Charge"), 4);
	}

	public void setCharge(int charge) {
		putInt("Charge", charge);
	}

	public long startTime() {
		return getLong("StartTime");
	}

	public void setStartTime(long time) {
		putLong("StartTime", time);
	}
}
