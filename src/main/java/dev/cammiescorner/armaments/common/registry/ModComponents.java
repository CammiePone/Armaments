package dev.cammiescorner.armaments.common.registry;

import dev.cammiescorner.armaments.Armaments;
import dev.cammiescorner.armaments.common.components.entity.EchoComponent;
import dev.cammiescorner.armaments.common.components.item.CrystalSpearComponent;
import dev.onyxstudios.cca.api.v3.component.Component;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.item.ItemComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.item.ItemComponentInitializer;
import net.minecraft.world.entity.LivingEntity;

public class ModComponents implements ItemComponentInitializer, EntityComponentInitializer {
	public static final ComponentKey<CrystalSpearComponent> CRYSTAL_SPEAR = createComponent("crystal_spear", CrystalSpearComponent.class);
	public static final ComponentKey<EchoComponent> ECHO = createComponent("echo", EchoComponent.class);

	@Override
	public void registerItemComponentFactories(ItemComponentFactoryRegistry registry) {
		registry.register(ModItems.CRYSTAL_SPEAR.get(), CRYSTAL_SPEAR, CrystalSpearComponent::new);
	}

	@Override
	public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
		registry.registerFor(LivingEntity.class, ECHO, EchoComponent::new);
	}

	private static <T extends Component> ComponentKey<T> createComponent(String name, Class<T> component) {
		return ComponentRegistry.getOrCreate(Armaments.id(name), component);
	}
}


