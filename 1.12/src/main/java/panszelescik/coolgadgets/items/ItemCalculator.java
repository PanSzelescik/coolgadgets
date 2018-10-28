package panszelescik.coolgadgets.items;

import static panszelescik.coolgadgets.CoolGadgets.*;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import panszelescik.coolgadgets.CoolGadgets;
import panszelescik.coolgadgets.gui.GuiHandler;
import panszelescik.morelibs.api.HandHelper;
import panszelescik.morelibs.api.Helper;
import panszelescik.morelibs.api.ItemBase;
import panszelescik.morelibs.api.NBTHelper;

public class ItemCalculator extends ItemBase {

	public ItemCalculator() {
		super(TAB);
		setTranslationKey(MODID + ".calculator");
		setRegistryName(new ResourceLocation(MODID, "calculator"));
		setMaxStackSize(1);
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		playerIn.openGui(CoolGadgets.instance, GuiHandler.CALCULATOR, worldIn, 0, 0, HandHelper.encrypt(handIn));
		return new ActionResult<ItemStack>(EnumActionResult.PASS, playerIn.getHeldItem(handIn));
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