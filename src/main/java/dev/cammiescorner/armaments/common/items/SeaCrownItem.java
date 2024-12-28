package dev.cammiescorner.armaments.common.items;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import dev.cammiescorner.armaments.ArmamentsConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
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
	private final Multimap<Attribute, AttributeModifier> attributeModifiers;

	public SeaCrownItem(Properties settings) {
		super(ArmorMaterials.TURTLE, Type.HELMET, settings);

		ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
		builder.put(Attributes.MAX_HEALTH, new AttributeModifier(HP_REDUCTION, "Crown modifier", -2, AttributeModifier.Operation.ADDITION));
		this.attributeModifiers = builder.build();
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
				wearer.addEffect(new MobEffectInstance(effect.getEffect(), 100, ArmamentsConfig.SeaCrown.potionAmplifier, true, false, true));

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

		for(MobEffectInstance effect : potion.getEffects())
			tooltip.add(Component.translatable(effect.getDescriptionId()).withStyle(effect.getEffect().isBeneficial() ? ChatFormatting.BLUE : ChatFormatting.RED));
	}

	@Override
	public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot slot) {
		return slot == EquipmentSlot.HEAD ? this.attributeModifiers : super.getDefaultAttributeModifiers(slot);
	}
}
