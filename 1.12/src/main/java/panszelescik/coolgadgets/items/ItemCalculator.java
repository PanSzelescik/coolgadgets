package panszelescik.coolgadgets.items;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import panszelescik.coolgadgets.CoolGadgets;
import panszelescik.coolgadgets.gui.ModGuis;
import panszelescik.morelibs.api.HandHelper;
import panszelescik.morelibs.api.Helper;
import panszelescik.morelibs.api.NBTHelper;

import javax.annotation.Nullable;
import java.util.List;

public class ItemCalculator extends Item {

    public ItemCalculator() {
        setCreativeTab(CoolGadgets.TAB);
        setMaxStackSize(1);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        playerIn.openGui(CoolGadgets.instance, ModGuis.CALCULATOR, worldIn, 0, 0, HandHelper.encrypt(handIn));
        return new ActionResult<>(EnumActionResult.PASS, playerIn.getHeldItem(handIn));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(Helper.translate(getTranslationKey() + ".tooltip"));
        if (NBTHelper.keyExists(stack, "display") && NBTHelper.keyExists(stack, "firstTyped")) {
            tooltip.add(TextFormatting.ITALIC + Helper.translate(getTranslationKey() + ".tooltip2"));
        }
    }
}