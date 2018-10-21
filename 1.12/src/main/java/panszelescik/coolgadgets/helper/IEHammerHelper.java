package panszelescik.coolgadgets.helper;

import javax.annotation.Nonnull;

import blusunrize.immersiveengineering.api.MultiblockHandler;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IConfigurableSides;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IDirectionalTile;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IHammerInteraction;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import blusunrize.immersiveengineering.common.util.RotationUtil;
import blusunrize.immersiveengineering.common.util.advancements.IEAdvancements;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class IEHammerHelper {
	
	@Nonnull
	public static EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, EnumHand hand) {
		ItemStack stack = player.getHeldItem(hand);
		for (MultiblockHandler.IMultiblock mb : MultiblockHandler.getMultiblocks()) {
			if (mb.isBlockTrigger(world.getBlockState(pos))) {
				if (MultiblockHandler.postMultiblockFormationEvent(player, mb, pos, stack).isCanceled()) {
					continue;
				}
				if (mb.createStructure(world, pos, side, player)) {
					if (player instanceof EntityPlayerMP) {
						IEAdvancements.TRIGGER_MULTIBLOCK.trigger((EntityPlayerMP) player, mb, stack);
					}
					return EnumActionResult.SUCCESS;
				}
			}
		}
		return EnumActionResult.PASS;
	}
	
	@Nonnull
	public static EnumActionResult onItemUse(World world, BlockPos pos, EnumFacing side) {
		TileEntity tileEntity = world.getTileEntity(pos);
		if (!(tileEntity instanceof IDirectionalTile) &&! (tileEntity instanceof IHammerInteraction) &&! (tileEntity instanceof IConfigurableSides))
			return RotationUtil.rotateBlock(world, pos, side) ? EnumActionResult.SUCCESS : EnumActionResult.PASS;
		return EnumActionResult.PASS;
	}
}