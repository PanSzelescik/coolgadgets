package panszelescik.coolgadgets.items;

import static panszelescik.coolgadgets.CoolGadgets.*;

import appeng.api.implementations.items.IAEWrench;
import blusunrize.immersiveengineering.api.tool.ITool;
import buildcraft.api.tools.IToolWrench;
import cofh.api.item.IToolHammer;
import crazypants.enderio.api.tool.IConduitControl;
import mekanism.api.IMekWrench;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.common.Optional.Interface;
import net.minecraftforge.fml.common.Optional.InterfaceList;
import net.minecraftforge.fml.common.Optional.Method;
import panszelescik.morelibs.api.ItemBase;

@InterfaceList({
	@Interface(iface = "appeng.api.implementations.items.IAEWrench", modid = "appliedenergistics2"),
	@Interface(iface = "buildcraft.api.tools.IToolWrench", modid = "buildcraftcore"),
	@Interface(iface = "cofh.api.item.IToolHammer", modid = "cofhcore"),
	@Interface(iface = "crazypants.enderio.api.tool.IConduitControl", modid = "enderio"),
	@Interface(iface = "blusunrize.immersiveengineering.api.tool.ITool", modid = "immersiveengineering"),
	@Interface(iface = "mekanism.api.IMekWrench", modid = "mekanism")
})
public class ItemSuperWrench extends ItemBase implements IAEWrench, IToolWrench, IConduitControl, IToolHammer, IMekWrench, ITool {

	public ItemSuperWrench() {
		super(TAB);
		setTranslationKey(MODID + ".superwrench");
		setRegistryName(new ResourceLocation(MODID, "superwrench"));
		setHarvestLevel("wrench", 1);
		setMaxStackSize(1);
	}
	
	@Override
	public boolean doesSneakBypassUse(ItemStack stack, IBlockAccess world, BlockPos pos, EntityPlayer player) {
		return true;
	}
	
	@Override
	public boolean isFull3D() {
		return true;
	}
	
	/* IAEWrench */
	@Method(modid = "appliedenergistics2")
	@Override
	public boolean canWrench(ItemStack arg0, EntityPlayer arg1, BlockPos arg2) {
		return true;
	}
	
	/* IToolWrench */
	@Method(modid = "buildcraftcore")
	@Override
	public boolean canWrench(EntityPlayer player, EnumHand hand, ItemStack wrench, RayTraceResult rayTrace) {
		return true;
	}

	@Override
	public void wrenchUsed(EntityPlayer player, EnumHand hand, ItemStack wrench, RayTraceResult rayTrace) {
		player.swingArm(hand);
	}
	
	/* IToolHammer */
	@Method(modid = "cofhcore")
	@Override
	public boolean isUsable(ItemStack item, EntityLivingBase user, BlockPos pos) {
		return true;
	}
	
	@Method(modid = "cofhcore")
	@Override
	public boolean isUsable(ItemStack item, EntityLivingBase user, Entity entity) {
		return true;
	}
	
	@Method(modid = "cofhcore")
	@Override
	public void toolUsed(ItemStack item, EntityLivingBase user, BlockPos pos) {
		user.swingArm(EnumHand.MAIN_HAND);
	}
	
	@Method(modid = "cofhcore")
	@Override
	public void toolUsed(ItemStack item, EntityLivingBase user, Entity entity) {
		user.swingArm(EnumHand.MAIN_HAND);
	}
	
	/* IConduitControl */
	@Method(modid = "enderio")
	@Override
	public boolean showOverlay(ItemStack stack, EntityPlayer player) {
		return true;
	}
	
	@Method(modid = "enderio")
	@Override
	public boolean shouldHideFacades(ItemStack stack, EntityPlayer player) {
		return true;
	}
	
	/* ITool */
	@Method(modid = "immersiveengineering")
	@Override
	public boolean isTool(ItemStack arg0) {
		return true;
	}
	
	/* IMekWrench */
	@Method(modid = "mekanism")
	@Override
	public boolean canUseWrench(ItemStack stack, EntityPlayer player, BlockPos pos) {
		return true;
	}
}