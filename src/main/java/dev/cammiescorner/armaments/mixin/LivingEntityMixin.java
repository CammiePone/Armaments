package dev.cammiescorner.armaments.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.cammiescorner.armaments.Armaments;
import dev.cammiescorner.armaments.common.components.entity.EchoComponent;
import dev.cammiescorner.armaments.common.echos.Echo;
import dev.cammiescorner.armaments.common.registry.ModComponents;
import dev.cammiescorner.armaments.common.registry.ModItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
	@Unique private final EchoComponent echo = getComponent(ModComponents.ECHO);

	public LivingEntityMixin(EntityType<?> variant, World world) {
		super(variant, world);
	}

	@ModifyExpressionValue(method = "tickRiptide", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getOtherEntities(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/Box;)Ljava/util/List;"))
	private List<Entity> dontHitYourHorseStupid(List<Entity> original) {
		if(hasVehicle())
			return original.stream().filter(entity -> !entity.getPassengerList().contains(this)).collect(Collectors.toCollection(ArrayList::new));

		return original;
	}

	@ModifyVariable(method = "damage", at = @At("HEAD"), argsOnly = true)
	private DamageSource changeEchoDaggerDamage(DamageSource source) {
		if(!source.isIndirect() && source.getSource() instanceof LivingEntity attacker) {
			ItemStack stack = attacker.getMainHandStack();

			if(stack.isOf(ModItems.ECHO_DAGGER.get()))
				return Armaments.echoDamage(getWorld());
			if(stack.isOf(ModItems.ELDER_GUARDIAN_SPIKE.get()))
				return Armaments.pokeyDamage(getWorld());
		}

		return source;
	}

	@Inject(method = "damage", at = @At("HEAD"))
	private void echoDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> info) {
		amount *= 0.5f;

		if(amount >= 1 && source.isType(Armaments.ECHO)) {
			timeUntilRegen = 0;
			echo.addEcho(new Echo(Echo.Type.DAMAGE, amount, getWorld().getTime()));
			timeUntilRegen = 0;
		}
	}

	@Inject(method = "heal", at = @At("HEAD"))
	public void heal(float amount, CallbackInfo info) {
		amount *= 0.5f;

		if(amount >= 1)
			echo.addEcho(new Echo(Echo.Type.HEAL, amount, getWorld().getTime()));
	}
}
