package panszelescik.coolgadgets.items;

import static panszelescik.coolgadgets.CoolGadgets.*;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import panszelescik.coolgadgets.gui.CalculatorGUI;
import panszelescik.morelibs.api.Helper;
import panszelescik.morelibs.api.ItemBase;

public class ItemCalculator extends ItemBase {

	public ItemCalculator() {
		super(TAB);
		setTranslationKey(MODID + ".calculator");
		setRegistryName(new ResourceLocation(MODID, "calculator"));
		setMaxStackSize(1);
		setContainerItem(this);
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		Minecraft.getMinecraft().displayGuiScreen(new CalculatorGUI());
		return new ActionResult<ItemStack>(EnumActionResult.PASS, playerIn.getHeldItem(handIn));
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add(Helper.translate(getTranslationKey() + ".tooltip"));
	}
}