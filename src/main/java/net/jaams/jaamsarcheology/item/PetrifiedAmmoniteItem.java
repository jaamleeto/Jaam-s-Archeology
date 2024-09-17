
package net.jaams.jaamsarcheology.item;

import net.minecraft.world.level.Level;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionHand;
import net.minecraft.stats.Stats;
import net.minecraft.sounds.SoundSource;
import net.minecraft.network.chat.Component;

import net.jaams.jaamsarcheology.init.JaamsArcheologyModSounds;
import net.jaams.jaamsarcheology.entity.AmmoniteProjectileEntity;
import net.jaams.jaamsarcheology.configuration.JaamsArcheologyCommonConfiguration;

import java.util.List;

public class PetrifiedAmmoniteItem extends Item {
	public PetrifiedAmmoniteItem() {
		super(new Item.Properties().stacksTo(64).rarity(Rarity.COMMON));
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
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
		ItemStack itemStack = player.getItemInHand(hand);
		if (!world.isClientSide()) {
			if (JaamsArcheologyCommonConfiguration.THROWAMMONITE.get() == true) {
				player.startUsingItem(hand);
				return InteractionResultHolder.consume(itemStack);
			} else {
				return InteractionResultHolder.fail(itemStack);
			}
		}
		return InteractionResultHolder.fail(itemStack);
	}

	@Override
	public void releaseUsing(ItemStack ammoniteItemStack, Level level, LivingEntity entity, int durationUsed) {
		if (JaamsArcheologyCommonConfiguration.THROWAMMONITE.get() == true) {
			if (entity instanceof Player player) {
				int remainingDuration = this.getUseDuration(ammoniteItemStack) - durationUsed;
				if (remainingDuration >= 5) {
					if (!level.isClientSide) {
						ammoniteItemStack.hurtAndBreak(1, player, (brokenItemStack) -> {
							brokenItemStack.broadcastBreakEvent(entity.getUsedItemHand());
						});
						AmmoniteProjectileEntity thrownAmmonite = new AmmoniteProjectileEntity(level, player, ammoniteItemStack);
						thrownAmmonite.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 2.0F + (float) 0.0F * 0.3F, 1.0F);
						thrownAmmonite.setBaseDamage(1);
						thrownAmmonite.setKnockback(1);
						thrownAmmonite.ammoniteItem = ammoniteItemStack.copy();
						if (player.getAbilities().instabuild) {
							thrownAmmonite.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
						}
						level.addFreshEntity(thrownAmmonite);
						level.playSound((Player) null, thrownAmmonite, JaamsArcheologyModSounds.AMMONITE_FIRED.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
						if (!player.getAbilities().instabuild) {
							player.getInventory().removeItem(ammoniteItemStack.split(1));
						}
						player.awardStat(Stats.ITEM_USED.get(this));
					}
				}
			}
		}
	}
}
