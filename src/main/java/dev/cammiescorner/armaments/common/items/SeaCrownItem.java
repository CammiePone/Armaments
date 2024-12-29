package dev.cammiescorner.armaments.common.items;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import dev.cammiescorner.armaments.ArmamentsConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.Equipable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class SeaCrownItem extends ArmorItem implements Equipable {
	private static final UUID HP_REDUCTION = UUID.fromString("1ed2ecfe-4ba3-4137-a110-dcb9db597d89");

	public SeaCrownItem(Properties settings) {
		super(ArmorMaterials.TURTLE, Type.HELMET, settings);
	}

	@Override
	public boolean isValidRepairItem(ItemStack stack, ItemStack ingredient) {
		return ingredient.is(Items.PRISMARINE_SHARD);
	}

	@Override
	public void inventoryTick(ItemStack stack, Level world, Entity entity, int slot, boolean selected) {
		if(entity instanceof LivingEntity wearer && wearer.getItemBySlot(EquipmentSlot.HEAD) == stack) {
			Potion potion = PotionUtils.getPotion(stack);

			for(MobEffectInstance effect : potion.getEffects())
				wearer.addEffect(new MobEffectInstance(effect.getEffect(), 100, Math.min(effect.getAmplifier(), ArmamentsConfig.SeaCrown.potionAmplifier), true, false, true));

			if(wearer instanceof ServerPlayer player)
				player.resetSentInfo();
		}
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag context) {
		CompoundTag tag = stack.getTag();

		if(tag == null)
			return;

		Potion potion = Potion.byName(tag.getString("Potion"));

		for(MobEffectInstance effect : potion.getEffects()) {
			tooltip.add(Component.translatable(
				"potion.withAmplifier",
				Component.translatable(effect.getDescriptionId()),
				Component.translatable("potion.potency." + effect.getAmplifier())
			).withStyle(effect.getEffect().isBeneficial() ? ChatFormatting.BLUE : ChatFormatting.RED));
		}
	}

	@Override
	public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot slot) {
		ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();

		if(ArmamentsConfig.SeaCrown.healthModifier != 0)
			builder.put(Attributes.MAX_HEALTH, new AttributeModifier(HP_REDUCTION, "Crown modifier", ArmamentsConfig.SeaCrown.healthModifier, AttributeModifier.Operation.ADDITION));
		if(ArmamentsConfig.SeaCrown.armorPoints != 0)
			builder.put(Attributes.ARMOR, new AttributeModifier(ArmorItem.ARMOR_MODIFIER_UUID_PER_TYPE.get(getType()), "Crown modifier", ArmamentsConfig.SeaCrown.armorPoints, AttributeModifier.Operation.ADDITION));

		return slot == EquipmentSlot.HEAD ? builder.build() : super.getDefaultAttributeModifiers(slot);
	}
}
