package dev.cammiescorner.armaments.common.components.entity;

import dev.cammiescorner.armaments.Armaments;
import dev.cammiescorner.armaments.ArmamentsConfig;
import dev.cammiescorner.armaments.common.echos.Echo;
import dev.cammiescorner.armaments.common.registry.ModStatusEffects;
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;

public class EchoComponent implements ServerTickingComponent {
	private final LivingEntity entity;
	private final List<Echo> echoes = new ArrayList<>();

	public EchoComponent(LivingEntity entity) {
		this.entity = entity;
	}

	@Override
	public void serverTick() {
		ServerLevel world = entity.getServer().getLevel(entity.level().dimension());

		if(world != null) {
			if(!entity.hasEffect(ModStatusEffects.ECHO.get())) {
				echoes.clear();
				return;
			}

			Iterator<Echo> iter = echoes.iterator();
			List<Echo> queue = new ArrayList<>();

			while(iter.hasNext()) {
				Echo echo = iter.next();

				if(world.getGameTime() - echo.time() > ArmamentsConfig.EchoDagger.echoDelay) {
					queue.add(echo);
					iter.remove();
				}
			}

			for(Echo echo : queue) {
				switch(echo.type()) {
					case HEAL -> entity.heal(echo.amount());
					case DAMAGE -> entity.hurt(Armaments.echoDamage(world), echo.amount());
				}
			}
		}
	}

	@Override
	public void readFromNbt(CompoundTag tag) {
		ListTag echoes = tag.getList("Echoes", Tag.TAG_COMPOUND);
		this.echoes.clear();

		for(int i = 0; i < echoes.size(); i++) {
			CompoundTag nbt = echoes.getCompound(i);
			this.echoes.add(new Echo(Echo.Type.valueOf(nbt.getString("EchoType")), nbt.getFloat("Amount"), nbt.getLong("TimeApplied")));
		}
	}

	@Override
	public void writeToNbt(CompoundTag tag) {
		ListTag echoes = new ListTag();

		for(Echo echo : this.echoes) {
			CompoundTag nbt = new CompoundTag();

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
