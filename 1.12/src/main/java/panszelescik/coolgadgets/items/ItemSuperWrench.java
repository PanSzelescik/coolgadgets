package panszelescik.coolgadgets.items;

import static panszelescik.coolgadgets.CoolGadgets.*;

import java.util.List;

import javax.annotation.Nullable;

import com.rwtema.extrautils2.api.tools.IWrench;
import com.zeitheron.hammercore.utils.wrench.IWrenchItem;

import appeng.api.implementations.items.IAEWrench;
import blusunrize.immersiveengineering.api.tool.ITool;
import buildcraft.api.tools.IToolWrench;
import cofh.api.item.IToolHammer;
import crazypants.enderio.api.tool.IConduitControl;
import li.cil.oc.api.internal.Wrench;
import mekanism.api.IMekWrench;
import mrtjp.projectred.api.IScrewdriver;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Optional.Interface;
import net.minecraftforge.fml.common.Optional.InterfaceList;
import net.minecraftforge.fml.common.Optional.Method;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import panszelescik.coolgadgets.helper.*;
import panszelescik.morelibs.api.BlockHelper;
import panszelescik.morelibs.api.Helper;
import panszelescik.morelibs.api.ItemBase;
import panszelescik.morelibs.api.ServerHelper;
import reborncore.api.IToolHandler;

@InterfaceList({
	@Interface(iface = "appeng.api.implementations.items.IAEWrench", modid = "appliedenergistics2"),
	@Interface(iface = "buildcraft.api.tools.IToolWrench", modid = "buildcraftcore"),
	@Interface(iface = "cofh.api.item.IToolHammer", modid = "cofhcore"),
	@Interface(iface = "crazypants.enderio.api.tool.IConduitControl", modid = "enderio"),
	@Interface(iface = "crazypants.enderio.api.tool.ITool", modid = "enderio"),
	@Interface(iface = "com.rwtema.extrautils2.api.tools.IWrench", modid = "extrautils2"),
	@Interface(iface = "com.zeitheron.hammercore.utils.wrench.IWrenchItem", modid = "hammercore"),
	@Interface(iface = "blusunrize.immersiveengineering.api.tool.ITool", modid = "immersiveengineering"),
	@Interface(iface = "mekanism.api.IMekWrench", modid = "mekanism"),
	@Interface(iface = "li.cil.oc.api.internal.Wrench", modid = "opencomputers"),
	@Interface(iface = "mrtjp.projectred.api.IScrewdriver", modid = "projectred-core"),
	@Interface(iface = "reborncore.api.IToolHandler", modid = "reborncore")
})
public class ItemSuperWrench extends ItemBase implements IAEWrench, IToolWrench, IToolHammer, IConduitControl, crazypants.enderio.api.tool.ITool, IWrench, IWrenchItem, ITool, IMekWrench, Wrench, IScrewdriver, IToolHandler {
	
	public ItemSuperWrench() {
		super(TAB);
		setTranslationKey(MODID + ".superwrench");
		setRegistryName(new ResourceLocation(MODID, "superwrench"));
		setHarvestLevel("wrench", 1);
		setMaxStackSize(1);
		setContainerItem(this);
	}
	
	@Override
	public boolean doesSneakBypassUse(ItemStack stack, IBlockAccess world, BlockPos pos, EntityPlayer player) {
		return true;
	}
	
	@Override
	public boolean isFull3D() {
		return true;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add(Helper.translate(getTranslationKey() + ".tooltip"));
	}
	
	@Override
	public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
		if (world.isAirBlock(pos)) {
			return EnumActionResult.PASS;
		}
		PlayerInteractEvent.RightClickBlock event = new PlayerInteractEvent.RightClickBlock(player, hand, pos, side, new Vec3d(hitX, hitY, hitZ));
		if (MinecraftForge.EVENT_BUS.post(event) || event.getResult() == Result.DENY || event.getUseBlock() == Result.DENY || event.getUseItem() == Result.DENY) {
			return EnumActionResult.PASS;
		}
		IBlockState state = world.getBlockState(pos);
		Block block = state.getBlock();
		if (BlockHelper.canRotate(block)) {
			world.setBlockState(pos, BlockHelper.rotateVanillaBlock(world, state, pos), 3);
			player.swingArm(hand);
			return ServerHelper.isServerWorld(world) ? EnumActionResult.SUCCESS : EnumActionResult.PASS;
		} else if (!player.isSneaking() && block.rotateBlock(world, pos, side)) {
			player.swingArm(hand);
			return ServerHelper.isServerWorld(world) ? EnumActionResult.SUCCESS : EnumActionResult.PASS;
		}
		if (Helper.isLoaded("ic2")) {
			if (BlockHelper.startWith(block, "ic2:") || BlockHelper.startWith(block, "advanced_solar_panels:")) {
				try {
					if (ServerHelper.isClientWorld(world)) {
						player.swingArm(hand);
						return EnumActionResult.PASS;
					}
					return IC2WrenchHelper.wrenchBlock(world, pos, side, player);
				} catch (Exception e) {}
			}
		}
		ItemStack stack = player.getHeldItem(hand);
		if (Helper.isLoaded("immersiveengineering")) {
			if (BlockHelper.startWith(block, "immersiveengineering:") || BlockHelper.startWith(block, "immersivepetroleum:") || BlockHelper.startWith(block, "immersivetech:")) {
				try {
					return IEHammerHelper.onItemUseFirst(player, world, pos, state, side, stack);
				} catch (Exception e) {}
			}
		}
		if (Helper.isLoaded("pneumaticcraft")) {
			if (BlockHelper.startWith(block, "pneumaticcraft:")) {
				try {
					return PCWrenchHelper.onItemUseFirst(player, world, pos, side, hand, stack, block);
				} catch (Exception e) {}
			}
		}
		return EnumActionResult.PASS;
	}
	
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		IBlockState state = world.getBlockState(pos);
		Block block = state.getBlock();
		TileEntity tile = world.getTileEntity(pos);
		if (Helper.isLoaded("immersiveengineering")) {
			if (BlockHelper.startWith(block, "immersiveengineering:") || BlockHelper.startWith(block, "immersivepetroleum:") || BlockHelper.startWith(block, "immersivetech:")) {
				try {
					if (ServerHelper.isClientWorld(world)) {
						player.swingArm(hand);
						return EnumActionResult.PASS;
					}
					return IEHammerHelper.onItemUse(world, pos, facing, tile);
				} catch (Exception e) {}
			}
		}
		if (Helper.isLoaded("refinedstorage")) {
			if (BlockHelper.startWith(block, "refinedstorage:")) {
				try {
					if (ServerHelper.isClientWorld(world)) {
						player.swingArm(hand);
						return EnumActionResult.PASS;
					}
					return RSWrenchHelper.wrenchBlock(player, world, pos, facing, state, block, tile);
				} catch (Exception e) {}
			}
		}
		return EnumActionResult.PASS;
	}
	
	/* IAEWrench */
	@Method(modid = "appliedenergistics2")
	@Override
	public boolean canWrench(ItemStack arg0, EntityPlayer arg1, BlockPos arg2) {
		arg1.swingArm(EnumHand.MAIN_HAND);
		return true;
	}
	
	/* IToolWrench */
	@Method(modid = "buildcraftcore")
	@Override
	public boolean canWrench(EntityPlayer player, EnumHand hand, ItemStack wrench, RayTraceResult rayTrace) {
		return true;
	}
	
	@Method(modid = "buildcraftcore")
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
	
	/* ITool - Ender IO */
	@Method(modid = "enderio")
	@Override
	public boolean canUse(EnumHand arg0, EntityPlayer arg1, BlockPos arg2) {
		return true;
	}
	
	@Method(modid = "enderio")
	@Override
	public void used(EnumHand arg0, EntityPlayer arg1, BlockPos arg2) {
		arg1.swingArm(arg0);
	}
	
	/* IWrenchItem */
	@Method(modid = "hammercore")
	@Override
	public boolean canWrench(ItemStack arg0) {
		return true;
	}
	
	@Method(modid = "hammercore")
	@Override
	public void onWrenchUsed(EntityPlayer arg0, BlockPos arg1, EnumHand arg2) {
		arg0.swingArm(arg2);
	}
	
	/* ITool - Immersive Engineering*/
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
	
	/* Wrench */
	@Method(modid = "opencomputers")
	@Override
	public boolean useWrenchOnBlock(EntityPlayer player, World world, BlockPos pos, boolean simulate) {
		return true;
	}
	
	/* IScrewdriver */
	@Method(modid = "projectred-core")
	@Override
	public boolean canUse(EntityPlayer arg0, ItemStack arg1) {
		return true;
	}
	
	@Method(modid = "projectred-core")
	@Override
	public void damageScrewdriver(EntityPlayer arg0, ItemStack arg1) {
		arg0.swingArm(EnumHand.MAIN_HAND);
	}
	
	/* IToolHandler */
	@Method(modid = "reborncore")
	@Override
	public boolean handleTool(ItemStack stack, BlockPos pos, World world, EntityPlayer player, EnumFacing side, boolean damage) {
		player.swingArm(EnumHand.MAIN_HAND);
		return true;
	}
}