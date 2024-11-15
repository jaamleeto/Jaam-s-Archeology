
package net.jaams.jaamsarcheology.item;

import net.minecraft.world.level.Level;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionHand;
import net.minecraft.stats.Stats;
import net.minecraft.sounds.SoundSource;
import net.minecraft.network.chat.Component;

import net.jaams.jaamsarcheology.init.JaamsArcheologyModSounds;
import net.jaams.jaamsarcheology.entity.SpearFragmentProjectileEntity;

import java.util.List;

import com.google.common.collect.Multimap;
import com.google.common.collect.ImmutableMultimap;

public class SpearFragmentItem extends Item {
	private static final double BASE_DAMAGE = 2.0;
	private static final int KNOCKBACK = 1;
	private static final int MINIMUM_USE_DURATION = 5;

	public SpearFragmentItem() {
		super(new Item.Properties().stacksTo(16).rarity(Rarity.COMMON));
	}

	@Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
		return false;
	}

	@Override
	public int getEnchantmentValue() {
		return 0;
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
		return false;
	}

	@Override
	public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot equipmentSlot) {
		if (equipmentSlot == EquipmentSlot.MAINHAND) {
			ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
			builder.putAll(super.getDefaultAttributeModifiers(equipmentSlot));
			builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Item modifier", 0.5d, AttributeModifier.Operation.ADDITION));
			builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Item modifier", -1.8, AttributeModifier.Operation.ADDITION));
			return builder.build();
		}
		return super.getDefaultAttributeModifiers(equipmentSlot);
	}

	@Override
	public void appendHoverText(ItemStack itemstack, Level world, List<Component> list, TooltipFlag flag) {
		super.appendHoverText(itemstack, world, list, flag);
	}

	@Override
	public UseAnim getUseAnimation(ItemStack itemstack) {
		return UseAnim.SPEAR;
	}

	@Override
	public int getUseDuration(ItemStack itemstack) {
		return 72000;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack itemStack = player.getItemInHand(hand);
		player.startUsingItem(hand);
		return InteractionResultHolder.consume(itemStack);
	}

	@Override
	public void releaseUsing(ItemStack weaponItemStack, Level level, LivingEntity entity, int durationUsed) {
		if (!(entity instanceof Player player))
			return;
		int remainingDuration = this.getUseDuration(weaponItemStack) - durationUsed;
		if (remainingDuration >= MINIMUM_USE_DURATION) {
			if (!level.isClientSide) {
				weaponItemStack.hurtAndBreak(1, player, (p) -> p.broadcastBreakEvent(entity.getUsedItemHand()));
				SpearFragmentProjectileEntity thrownWeapon = new SpearFragmentProjectileEntity(level, player, weaponItemStack);
				thrownWeapon.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 2.0F, 1.0F);
				thrownWeapon.setBaseDamage(BASE_DAMAGE);
				thrownWeapon.setKnockback(KNOCKBACK);
				thrownWeapon.weaponItem = weaponItemStack.copy();
				if (player.getAbilities().instabuild) {
					thrownWeapon.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
				}
				level.addFreshEntity(thrownWeapon);
				level.playSound(null, thrownWeapon, JaamsArcheologyModSounds.SPEAR_FRAGMENT_FIRED.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
				if (!player.getAbilities().instabuild) {
					player.getInventory().removeItem(weaponItemStack.split(1));
				}
				player.awardStat(Stats.ITEM_USED.get(this));
			}
		}
	}
}
