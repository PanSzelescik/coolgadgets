package panszelescik.coolgadgets.helper;

import javax.annotation.Nullable;

import dalapo.factech.auxiliary.Linkable;
import dalapo.factech.auxiliary.Wrenchable;
import dalapo.factech.item.ItemWrench;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/*
 * @author dalapo
 * https://gitlab.com/dalapo/FactoryTech/blob/master/src/main/java/dalapo/factech/item/ItemWrench.java
 */
public class FactoryTechHelper {
	
	public static EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, ItemStack stack) {
		if (world.getBlockState(pos).getBlock() instanceof Wrenchable) {
			((Wrenchable) world.getBlockState(pos).getBlock()).onWrenched(player, player.isSneaking(), world, pos, side);
			return EnumActionResult.SUCCESS;
		}
		if (world.getBlockState(pos).getBlock() instanceof Linkable) {
			if (!hasLink(stack)) {
				setLink(stack, pos);
			} else {
				((Linkable) world.getBlockState(pos).getBlock()).onLinked(world, player, pos, getLink(stack), stack);
				clearLink(stack);
			}
			return EnumActionResult.SUCCESS;
		}
		return EnumActionResult.PASS;
	}
	
	public static boolean hasLink(ItemStack wrench) {
		return wrench.hasTagCompound() && wrench.getTagCompound().hasKey(ItemWrench.LINK_TAG);
	}
	
	@Nullable
	public static BlockPos getLink(ItemStack wrench) {
		return hasLink(wrench) ? BlockPos.fromLong(wrench.getTagCompound().getLong(ItemWrench.LINK_TAG)) : null;
	}
	
	public static void setLink(ItemStack wrench, BlockPos link) {
		NBTTagCompound tag;
		if (!wrench.hasTagCompound()) {
			wrench.setTagCompound(new NBTTagCompound());
		}
		wrench.getTagCompound().setLong(ItemWrench.LINK_TAG, link.toLong());
	}
	
	public static void clearLink(ItemStack wrench) {
		if (hasLink(wrench)) {
			wrench.getTagCompound().removeTag(ItemWrench.LINK_TAG);
		}
	}
}