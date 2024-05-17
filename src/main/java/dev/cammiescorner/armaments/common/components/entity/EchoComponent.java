package dev.cammiescorner.armaments.common.components.entity;

import dev.cammiescorner.armaments.Armaments;
import dev.cammiescorner.armaments.common.echos.Echo;
import dev.cammiescorner.armaments.common.registry.ModStatusEffects;
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.world.ServerWorld;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class EchoComponent implements ServerTickingComponent {
	private final LivingEntity entity;
	private final List<Echo> echoes = new ArrayList<>();

	public EchoComponent(LivingEntity entity) {
		this.entity = entity;
	}

	@Override
	public void serverTick() {
		ServerWorld world = entity.getServer().getWorld(entity.getWorld().getRegistryKey());

		if(world != null) {
			if(!entity.hasStatusEffect(ModStatusEffects.ECHO.get())) {
				echoes.clear();
				return;
			}

			Iterator<Echo> iter = echoes.iterator();
			List<Echo> queue = new ArrayList<>();

			while(iter.hasNext()) {
				Echo echo = iter.next();

				if(world.getTime() - echo.time() > 40) {
					queue.add(echo);
					iter.remove();
				}
			}

			for(Echo echo : queue) {
				switch(echo.type()) {
					case HEAL -> entity.heal(echo.amount());
					case DAMAGE -> entity.damage(Armaments.echoDamage(world), echo.amount());
				}
			}
		}
	}

	@Override
	public void readFromNbt(NbtCompound tag) {
		NbtList echoes = tag.getList("Echoes", NbtElement.COMPOUND_TYPE);
		this.echoes.clear();

		for(int i = 0; i < echoes.size(); i++) {
			NbtCompound nbt = echoes.getCompound(i);
			this.echoes.add(new Echo(Echo.Type.valueOf(nbt.getString("EchoType")), nbt.getFloat("Amount"), nbt.getLong("TimeApplied")));
		}
	}

	@Override
	public void writeToNbt(NbtCompound tag) {
		NbtList echoes = new NbtList();

		for(Echo echo : this.echoes) {
			NbtCompound nbt = new NbtCompound();

			nbt.putString("EchoType", echo.type().toString());
			nbt.putFloat("Amount", echo.amount());
			nbt.putLong("TimeApplied", echo.time());

			echoes.add(nbt);
		}

		tag.put("Echoes", echoes);
	}

	public void addEcho(Echo echo) {
		echoes.add(echo);
	}

	public List<Echo> viewOfEchoes() {
		return Collections.unmodifiableList(echoes);
	}
}
