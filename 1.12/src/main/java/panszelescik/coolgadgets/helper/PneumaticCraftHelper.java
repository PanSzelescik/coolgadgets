package panszelescik.coolgadgets.helper;

import me.desht.pneumaticcraft.api.block.IPneumaticWrenchable;
import me.desht.pneumaticcraft.common.item.ItemPneumaticWrench;
import me.desht.pneumaticcraft.common.item.Itemss;
import me.desht.pneumaticcraft.common.network.NetworkHandler;
import me.desht.pneumaticcraft.common.network.PacketPlaySound;
import me.desht.pneumaticcraft.common.thirdparty.ModInteractionUtils;
import me.desht.pneumaticcraft.lib.PneumaticValues;
import me.desht.pneumaticcraft.lib.Sounds;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import panszelescik.morelibs.api.ServerHelper;

public class PneumaticCraftHelper {
	
	public static EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, EnumHand hand, ItemStack stack, Block block) {
		if (ServerHelper.isServerWorld(world)) {
			IPneumaticWrenchable wrenchable;
			if (block instanceof IPneumaticWrenchable) {
				wrenchable = (IPneumaticWrenchable) block;
			} else {
				wrenchable = ModInteractionUtils.getInstance().getWrenchable(world.getTileEntity(pos));
			}
			boolean didWork = true;
			if (wrenchable != null) {
				if (wrenchable.rotateBlock(world, player, pos, side)) {
					if (!player.capabilities.isCreativeMode) {
						((ItemPneumaticWrench) Itemss.PNEUMATIC_WRENCH).addAir(stack, -PneumaticValues.USAGE_PNEUMATIC_WRENCH);
					}
				}
			}
			if (didWork) {
				if (ServerHelper.isClientWorld(world)) {
					player.swingArm(hand);
				}
				playWrenchSound(world, pos);
			}
			return didWork ? EnumActionResult.SUCCESS : EnumActionResult.PASS;
		} else {
			return EnumActionResult.SUCCESS;
		}
	}
	
	private static void playWrenchSound(World world, BlockPos pos) {
		NetworkHandler.sendToAllAround(new PacketPlaySound(Sounds.PNEUMATIC_WRENCH, SoundCategory.PLAYERS, pos, 1.0F, 1.0F, false), world);
	}
}