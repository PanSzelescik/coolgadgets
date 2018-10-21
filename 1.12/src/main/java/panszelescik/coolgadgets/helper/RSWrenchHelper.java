package panszelescik.coolgadgets.helper;

import com.raoulvdberge.refinedstorage.api.network.security.Permission;
import com.raoulvdberge.refinedstorage.apiimpl.network.node.ICoverable;
import com.raoulvdberge.refinedstorage.apiimpl.network.node.cover.CoverManager;
import com.raoulvdberge.refinedstorage.block.BlockCable;
import com.raoulvdberge.refinedstorage.item.ItemCover;
import com.raoulvdberge.refinedstorage.render.collision.AdvancedRayTraceResult;
import com.raoulvdberge.refinedstorage.render.collision.AdvancedRayTracer;
import com.raoulvdberge.refinedstorage.tile.TileNode;
import com.raoulvdberge.refinedstorage.util.WorldUtils;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/*
 * @author raoulvdberge
 * https://github.com/raoulvdberge/refinedstorage/blob/mc1.12/src/main/java/com/raoulvdberge/refinedstorage/item/ItemWrench.java
 */
public class RSWrenchHelper {
	
	public static EnumActionResult wrenchBlock(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (!player.isSneaking()) {
			return EnumActionResult.FAIL;
		}
		if (world.isRemote) {
			return EnumActionResult.SUCCESS;
		}
		TileEntity tile = world.getTileEntity(pos);
		if (tile instanceof TileNode && ((TileNode) tile).getNode().getNetwork() != null && !((TileNode) tile).getNode().getNetwork().getSecurityManager().hasPermission(Permission.BUILD, player)) {
			WorldUtils.sendNoPermissionMessage(player);
			return EnumActionResult.FAIL;
		}
        IBlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        if (block instanceof BlockCable && tile instanceof TileNode && ((TileNode) tile).getNode() instanceof ICoverable) {
        	CoverManager manager = ((ICoverable) ((TileNode) tile).getNode()).getCoverManager();
        	
        	@SuppressWarnings("deprecation")
        	AdvancedRayTraceResult result = AdvancedRayTracer.rayTrace(
        		pos,
        		AdvancedRayTracer.getStart(player),
        		AdvancedRayTracer.getEnd(player),
        		((BlockCable) block).getCollisions(tile, block.getActualState(state, world, pos))
        	);
        	if (result != null && result.getGroup().getDirection() != null) {
        		EnumFacing facingSelected = result.getGroup().getDirection();
        		if (manager.hasCover(facingSelected)) {
        			ItemStack cover = manager.getCover(facingSelected).getType().createStack();
        			ItemCover.setItem(cover, manager.getCover(facingSelected).getStack());
        			manager.setCover(facingSelected, null);
        			WorldUtils.updateBlock(world, pos);
        			InventoryHelper.spawnItemStack(world, pos.getX(), pos.getY(), pos.getZ(), cover);
        			return EnumActionResult.SUCCESS;
        		}
        	}
        }
        block.rotateBlock(world, pos, player.getHorizontalFacing().getOpposite());
        return EnumActionResult.SUCCESS;
	}
}