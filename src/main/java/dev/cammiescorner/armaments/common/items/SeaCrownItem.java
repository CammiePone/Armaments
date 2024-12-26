package dev.cammiescorner.armaments.common.items;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import dev.cammiescorner.armaments.ArmamentsConfig;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtil;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class SeaCrownItem extends ArmorItem implements Equippable {
	private static final UUID HP_REDUCTION = UUID.fromString("1ed2ecfe-4ba3-4137-a110-dcb9db597d89");
	private final Multimap<EntityAttribute, EntityAttributeModifier> attributeModifiers;

	public SeaCrownItem(Settings settings) {
		super(ArmorMaterials.TURTLE, ArmorSlot.HELMET, settings);

		ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
		builder.put(EntityAttributes.GENERIC_MAX_HEALTH, new EntityAttributeModifier(HP_REDUCTION, "Crown modifier", -2, EntityAttributeModifier.Operation.ADDITION));
		this.attributeModifiers = builder.build();
	}

	@Override
	public boolean canRepair(ItemStack stack, ItemStack ingredient) {
		return ingredient.isOf(Items.PRISMARINE_SHARD);
	}

	@Override
	public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
		if(entity instanceof LivingEntity wearer && wearer.getEquippedStack(EquipmentSlot.HEAD) == stack) {
			Potion potion = PotionUtil.getPotion(stack);

			for(StatusEffectInstance effect : potion.getEffects())
				wearer.addStatusEffect(new StatusEffectInstance(effect.getEffectType(), 100, ArmamentsConfig.SeaCrown.potionAmplifier, true, false, true));

			if(wearer instanceof ServerPlayerEntity player)
				player.markHealthDirty();
		}
	}

	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		NbtCompound tag = stack.getNbt();

		if(tag == null)
			return;

		Potion potion = Potion.byId(tag.getString("Potion"));

		for(StatusEffectInstance effect : potion.getEffects())
			tooltip.add(Text.translatable(effect.getTranslationKey()).formatted(effect.getEffectType().isBeneficial() ? Formatting.BLUE : Formatting.RED));
	}

	@Override
	public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot slot) {
		return slot == EquipmentSlot.HEAD ? this.attributeModifiers : super.getAttributeModifiers(slot);
	}
}
