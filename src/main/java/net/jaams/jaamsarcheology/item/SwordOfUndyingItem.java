package net.jaams.jaamsarcheology.item;

import org.jetbrains.annotations.NotNull;

import net.minecraftforge.fml.ModList;
import net.minecraftforge.common.ToolActions;

import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.network.chat.Component;
import net.minecraft.nbt.CompoundTag;

import net.jaams.jaamsarcheology.procedures.SwordOfUndyingRightclickedProcedure;
import net.jaams.jaamsarcheology.procedures.SwordOfUndyingLivingEntityIsHitWithToolProcedure;
import net.jaams.jaamsarcheology.init.JaamsArcheologyModItems;

import java.util.List;

public class SwordOfUndyingItem extends SwordItem {
	public SwordOfUndyingItem() {
		super(new Tier() {
			public int getUses() {
				return 632;
			}

			public float getSpeed() {
				return 4f;
			}

			public float getAttackDamageBonus() {
				return 6f;
			}

			public int getLevel() {
				return 1;
			}

			public int getEnchantmentValue() {
				return 2;
			}

			public Ingredient getRepairIngredient() {
				return Ingredient.of(new ItemStack(JaamsArcheologyModItems.PRIMORDIAL_GOLD.get()), new ItemStack(JaamsArcheologyModItems.PRIMORDIAL_GOLD_FRAGMENT.get()));
			}
		}, 3, -2.8f, new Item.Properties());
	}

	@Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
		return false;
	}

	@Override
	public boolean hurtEnemy(ItemStack itemstack, LivingEntity entity, LivingEntity sourceentity) {
		boolean retval = super.hurtEnemy(itemstack, entity, sourceentity);
		SwordOfUndyingLivingEntityIsHitWithToolProcedure.execute(entity.level(), entity.getX(), entity.getY(), entity.getZ());
		return retval;
	}

	@Override
	public void appendHoverText(ItemStack itemstack, Level world, List<Component> list, TooltipFlag flag) {
		super.appendHoverText(itemstack, world, list, flag);
	}

	@Override
	public int getUseDuration(ItemStack itemstack) {
		return 72000;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player entity, InteractionHand hand) {
		ItemStack itemStack = entity.getItemInHand(hand);
		if (!entity.isShiftKeyDown() && !ModList.get().isLoaded("epicfight")) {
			entity.startUsingItem(hand);
			return new InteractionResultHolder<>(InteractionResult.CONSUME, itemStack);
		}
		if (entity.isShiftKeyDown()) {
			InteractionResultHolder<ItemStack> ar = super.use(level, entity, hand);
			SwordOfUndyingRightclickedProcedure.execute(level, entity.getX(), entity.getY(), entity.getZ(), entity, ar.getObject());
			ar.getObject().hurtAndBreak(60, entity, (p_43388_) -> {
				p_43388_.broadcastBreakEvent(entity.getUsedItemHand());
			});
			return ar;
		}
		return new InteractionResultHolder<>(InteractionResult.CONSUME, itemStack);
	}

	@Override
	public UseAnim getUseAnimation(ItemStack itemstack) {
		return UseAnim.BLOCK;
	}

	@Override
	public boolean canPerformAction(ItemStack stack, net.minecraftforge.common.ToolAction toolAction) {
		return toolAction.equals(ToolActions.SWORD_DIG) || toolAction.equals(ToolActions.SWORD_SWEEP) || toolAction.equals(ToolActions.SHIELD_BLOCK);
	}

	@NotNull
	@Override
	public AABB getSweepHitBox(@NotNull ItemStack stack, @NotNull Player player, @NotNull Entity target) {
		return target.getBoundingBox().inflate(2.0D, 0.25D, 2.0D);
	}

	@Override
	public void inventoryTick(ItemStack itemstack, Level world, Entity entity, int slot, boolean selected) {
		super.inventoryTick(itemstack, world, entity, slot, selected);
		if (entity instanceof Player) {
			Player player = (Player) entity;
			CompoundTag tag = itemstack.getOrCreateTag();
			boolean isUsing = player.isUsingItem() && player.getUseItem() == itemstack;
			if (isUsing) {
				tag.putInt("CustomModelData", 1);
			} else {
				tag.putInt("CustomModelData", 0);
			}
		}
	}
}
