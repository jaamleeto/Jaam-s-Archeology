
package net.jaams.jaamsarcheology.item;

import net.minecraftforge.common.ToolActions;
import net.minecraftforge.common.ToolAction;

import net.minecraft.world.level.Level;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionHand;

public class ArcheoTrowelItem extends ShovelItem {
	public ArcheoTrowelItem() {
		super(new Tier() {
			public int getUses() {
				return 200;
			}

			public float getSpeed() {
				return 4f;
			}

			public float getAttackDamageBonus() {
				return 0f;
			}

			public int getLevel() {
				return 1;
			}

			public int getEnchantmentValue() {
				return 10;
			}

			public Ingredient getRepairIngredient() {
				return Ingredient.of(new ItemStack(Items.COPPER_INGOT));
			}
		}, 1, -2.6f, new Item.Properties());
	}

	@Override
	public boolean canPerformAction(ItemStack stack, ToolAction toolAction) {
		// Ensure only basic sword hit action is allowed and disallow sweeping
		return toolAction.equals(ToolActions.SHOVEL_DIG);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		// Verifica si el jugador está usando miel
		if (player.isCrouching() && player.getItemInHand(InteractionHand.OFF_HAND).getItem() == Items.HONEYCOMB) {
			stack.getOrCreateTag().putBoolean("Waxed", true); // Marca el ítem como encerado
			player.getItemInHand(InteractionHand.OFF_HAND).shrink(1); // Reduce el stack de miel en la mano
			return InteractionResultHolder.sidedSuccess(stack, world.isClientSide());
		}
		return InteractionResultHolder.pass(stack);
	}
}
