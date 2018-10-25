package panszelescik.coolgadgets.helper;

import java.util.List;

import de.ellpeck.actuallyadditions.api.ActuallyAdditionsAPI;
import de.ellpeck.actuallyadditions.mod.ActuallyAdditions;
import de.ellpeck.actuallyadditions.mod.items.ItemPhantomConnector;
import de.ellpeck.actuallyadditions.mod.tile.TileEntityLaserRelay;
import de.ellpeck.actuallyadditions.mod.util.StringUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import panszelescik.morelibs.api.ServerHelper;

public class AAWrenchHelper {
	
	public static EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, ItemStack stack, TileEntity tile){
		if (tile instanceof TileEntityLaserRelay) {
			TileEntityLaserRelay relay = (TileEntityLaserRelay) tile;
			if (ServerHelper.isServerWorld(world)) {
				if (ItemPhantomConnector.getStoredPosition(stack) == null) {
					ItemPhantomConnector.storeConnection(stack, pos.getX(), pos.getY(), pos.getZ(), world);
					player.sendStatusMessage(new TextComponentTranslation("tooltip."+ ActuallyAdditions.MODID + ".laser.stored.desc"), true);
				} else {
					BlockPos savedPos = ItemPhantomConnector.getStoredPosition(stack);
					if (savedPos != null) {
						TileEntity savedTile = world.getTileEntity(savedPos);
						if (savedTile instanceof TileEntityLaserRelay) {
							int distanceSq = (int) savedPos.distanceSq(pos);
							TileEntityLaserRelay savedRelay = (TileEntityLaserRelay) savedTile;
							int lowestRange = Math.min(relay.getMaxRange(), savedRelay.getMaxRange());
							int range = lowestRange * lowestRange;
							if (ItemPhantomConnector.getStoredWorld(stack) == world && savedRelay.type == relay.type && distanceSq <= range && ActuallyAdditionsAPI.connectionHandler.addConnection(savedPos, pos, relay.type, world, false, true)){
								ItemPhantomConnector.clearStorage(stack, "XCoordOfTileStored", "YCoordOfTileStored", "ZCoordOfTileStored", "WorldOfTileStored");
								((TileEntityLaserRelay) savedTile).sendUpdate();
								relay.sendUpdate();
								player.sendStatusMessage(new TextComponentTranslation("tooltip."+ActuallyAdditions.MODID+".laser.connected.desc"), true);
								return EnumActionResult.SUCCESS;
							}
						}
						player.sendMessage(new TextComponentTranslation("tooltip." + ActuallyAdditions.MODID + ".laser.cantConnect.desc"));
						ItemPhantomConnector.clearStorage(stack, "XCoordOfTileStored", "YCoordOfTileStored", "ZCoordOfTileStored", "WorldOfTileStored");
					}
				}
			}
			return EnumActionResult.SUCCESS;
		}
		return EnumActionResult.FAIL;
	}
	
	public static void addInformation(ItemStack stack, List<String> tooltip) {
		BlockPos coords = ItemPhantomConnector.getStoredPosition(stack);
		if (coords != null) {
			tooltip.add(StringUtil.localize("tooltip." + ActuallyAdditions.MODID + ".boundTo.desc") + ":");
			tooltip.add("X: " + coords.getX());
			tooltip.add("Y: " + coords.getY());
			tooltip.add("Z: " + coords.getZ());
			tooltip.add(TextFormatting.ITALIC + StringUtil.localize("tooltip." + ActuallyAdditions.MODID + ".clearStorage.desc"));
		}
	}
}