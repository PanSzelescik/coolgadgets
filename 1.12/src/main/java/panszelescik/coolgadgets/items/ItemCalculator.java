package panszelescik.coolgadgets.items;

import static panszelescik.coolgadgets.CoolGadgets.*;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import panszelescik.coolgadgets.gui.CalculatorGUI;
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
}