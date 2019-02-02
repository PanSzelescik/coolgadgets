package panszelescik.coolgadgets.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import panszelescik.coolgadgets.CoolGadgets;
import panszelescik.coolgadgets.gui.ModGuis;

public class ItemCrafting extends Item {

    public ItemCrafting() {
        setCreativeTab(CoolGadgets.TAB);
        setMaxStackSize(1);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        player.addStat(StatList.CRAFTING_TABLE_INTERACTION);
        if (!world.isRemote) {
            player.openGui(CoolGadgets.instance, ModGuis.CRAFTING, world, (int) player.posX, (int) player.posY, (int) player.posZ);
        }
        return new ActionResult<>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
    }

    @Override
    public EnumRarity getRarity(ItemStack stack) {
        return EnumRarity.EPIC;
    }
}
