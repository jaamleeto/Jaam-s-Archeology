
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
import net.minecraft.world.entity.Entity;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.chat.Component;
import net.minecraft.nbt.CompoundTag;

import net.jaams.jaamsarcheology.init.JaamsArcheologyModItems;
import net.jaams.jaamsarcheology.dyeable.IDyeableItem;

import java.util.List;

public class BrokenSwordFragmentItem extends SwordItem implements IDyeableItem {
	public BrokenSwordFragmentItem() {
		super(new Tier() {
			public int getUses() {
				return 226;
			}

			public float getSpeed() {
				return 4f;
			}

			public float getAttackDamageBonus() {
				return -1f;
			}

			public int getLevel() {
				return 1;
			}

			public int getEnchantmentValue() {
				return 10;
			}

			public Ingredient getRepairIngredient() {
				return Ingredient.of(new ItemStack(JaamsArcheologyModItems.BROKEN_SWORD_FRAGMENT.get()));
			}
		}, 3, -2.2f, new Item.Properties());
	}

	@Override
	public int getDefaultColor() {
		return 0xFF44CC44;
	}

	@Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
		return false;
	}

	@Override
	public void inventoryTick(ItemStack itemstack, Level world, Entity entity, int slot, boolean selected) {
		super.inventoryTick(itemstack, world, entity, slot, selected);
		Player player = (Player) entity;
		CompoundTag tag = itemstack.getOrCreateTag();
		boolean isUsing = player.isUsingItem() && player.getUseItem() == itemstack;
		if (isUsing) {
			tag.putInt("CustomModelData", 1);
		} else {
			tag.putInt("CustomModelData", 0);
		}
		double deflectAmount = tag.getDouble("DeflectAmount");
		// Verificar el valor de DeflectAmount y cambiar el color del item
		if (deflectAmount > 20) {
			((IDyeableItem) itemstack.getItem()).setColor(itemstack, 0xFF0000); // Rojo
		} else if (deflectAmount > 10) {
			((IDyeableItem) itemstack.getItem()).setColor(itemstack, 0xFFA500); // Naranja
		} else if (deflectAmount > 1) {
			((IDyeableItem) itemstack.getItem()).setColor(itemstack, 0xFFFF00); // Amarillo
		} else {
			((IDyeableItem) itemstack.getItem()).setColor(itemstack, 0xFF44CC44); // Color original
		}
	}

	@Override
	public void appendHoverText(ItemStack itemstack, Level world, List<Component> list, TooltipFlag flag) {
		super.appendHoverText(itemstack, world, list, flag);
		double deflectAmount = itemstack.getOrCreateTag().getDouble("DeflectAmount");
		if (deflectAmount > 0) {
			String attackerName = itemstack.getOrCreateTag().getString("AttackerName");
			list.add(Component.literal("Deflect Amount: ").withStyle(style -> style.withColor(TextColor.fromRgb(0xFF5555))) // Color rojo para el título
					.append(Component.literal(String.valueOf(deflectAmount)).withStyle(style -> style.withColor(TextColor.fromRgb(0xAAAAAA)))) // Color gris para el valor
			);
			list.add(Component.literal("Attacker: ").withStyle(style -> style.withColor(TextColor.fromRgb(0xFF5555))) // Color rojo para el título
					.append(Component.literal(attackerName.isEmpty() ? "Projectile" : attackerName).withStyle(style -> style.withColor(TextColor.fromRgb(0xAAAAAA)))) // Color gris para el valor
			);
		}
	}

	@Override
	public int getUseDuration(ItemStack itemstack) {
		return 72000;
	}

	@Override
	public UseAnim getUseAnimation(ItemStack itemstack) {
		return UseAnim.BLOCK;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player entity, InteractionHand hand) {
		ItemStack itemStack = entity.getItemInHand(hand);
		if (!ModList.get().isLoaded("epicfight")) {
			entity.startUsingItem(hand);
			return new InteractionResultHolder<>(InteractionResult.CONSUME, entity.getItemInHand(hand));
		}
		return new InteractionResultHolder<>(InteractionResult.CONSUME, entity.getItemInHand(hand));
	}

	@Override
	public boolean canPerformAction(ItemStack stack, net.minecraftforge.common.ToolAction toolAction) {
		return toolAction.equals(ToolActions.SWORD_DIG) || toolAction.equals(ToolActions.SWORD_SWEEP) || toolAction.equals(ToolActions.SHIELD_BLOCK);
	}

	@NotNull
	@Override
	public AABB getSweepHitBox(@NotNull ItemStack stack, @NotNull Player player, @NotNull Entity target) {
		return target.getBoundingBox().inflate(0.5D, 0.25D, 0.5D);
	}
}
