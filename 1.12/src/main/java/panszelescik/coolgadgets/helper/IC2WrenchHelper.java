package panszelescik.coolgadgets.helper;

import ic2.core.IC2;
import ic2.core.audio.PositionSpec;
import ic2.core.item.tool.ItemToolWrench;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class IC2WrenchHelper {
	
	public static EnumActionResult wrenchBlock(World world, BlockPos pos, EnumFacing side, EntityPlayer player) {
		ItemToolWrench.wrenchBlock(world, pos, side, player, true);
		IC2.audioManager.playOnce(player, PositionSpec.Hand, "Tools/wrench.ogg", true, IC2.audioManager.getDefaultVolume());
		return EnumActionResult.SUCCESS;
	}
}