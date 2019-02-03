package panszelescik.coolgadgets.helper;

import com.mojang.authlib.GameProfile;
import com.valkyrieofnight.vlib.core.util.client.ColorUtil;
import com.valkyrieofnight.vlib.core.util.client.LangUtil;
import com.valkyrieofnight.vlib.core.util.player.ChatUtil;
import com.valkyrieofnight.vlib.multiblock.api.tile.ITileController;
import com.valkyrieofnight.vlib.multiblock.api.tile.ITileSlave;
import com.valkyrieofnight.vliblegacy.api.multiblock.structure.IMultiblockStructure;
import com.valkyrieofnight.vliblegacy.lib.sys.owner.Owner;
import com.valkyrieofnight.vliblegacy.m_multiblocks.features.VLMConfigs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import panszelescik.coolgadgets.items.ModItems;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class EnvironmentalTechHelper {

    @SubscribeEvent
    public static void onPlayerRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (!event.getWorld().isRemote) {
            ItemStack mh = event.getEntityPlayer().getHeldItem(EnumHand.MAIN_HAND);
            if (mh != null && mh.getItem() == ModItems.superWrench) {
                BlockPos pos = event.getPos();
                TileEntity te = event.getWorld().getTileEntity(pos);
                if (te instanceof ITileController) {
                    if (event.getEntityPlayer().isCreative()) {
                        ITileController ct;
                        if (event.getEntityPlayer().isSneaking()) {
                            ct = (ITileController) te;
                            ct.getStructure().pickupMultiBlock(event.getWorld(), te.getPos(), event.getEntityPlayer());
                        } else {
                            ct = (ITileController) te;
                            ct.getStructure().placeAllInWorld(event.getWorld(), te.getPos(), event.getEntityPlayer());
                        }
                    } else if (event.getEntityPlayer().isSneaking()) {
                        World world = event.getWorld();
                        TileEntity te2 = world.getTileEntity(pos);
                        if (te instanceof ITileController && event.getEntity() instanceof EntityPlayer) {
                            if (VLMConfigs.STRUCTURE_PROTECTION) {
                                if (isOwner((ITileController) te2, event.getEntityPlayer())) {
                                    event.getWorld().destroyBlock(pos, true);
                                } else if (!hasOwner((ITileController) te2)) {
                                    event.getWorld().destroyBlock(pos, true);
                                }
                            } else {
                                event.getWorld().destroyBlock(pos, true);
                            }
                        }
                    } else if (!((ITileController) te).getStructure().placeNextBlock(event.getWorld(), te.getPos(), event.getEntityPlayer())) {
                    }

                    if (((ITileController) te).isFormed()) {
                        ChatUtil.noSpamServer(event.getEntityPlayer(), new ArrayList<ITextComponent>() {
                            {
                                this.add(new TextComponentTranslation("gui.environmentaltech.mbcomplete"));
                            }
                        });
                    }
                } else if (te instanceof ITileSlave && event.getEntityPlayer().isSneaking() && event.getEntity() instanceof EntityPlayer) {
                    if (VLMConfigs.STRUCTURE_PROTECTION) {
                        if (isOwner((ITileSlave) te, event.getEntityPlayer())) {
                            event.getWorld().destroyBlock(pos, true);
                        } else if (!hasOwner((ITileSlave) te)) {
                            event.getWorld().destroyBlock(pos, true);
                        }
                    } else {
                        event.getWorld().destroyBlock(pos, true);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerInteractWith(PlayerInteractEvent.LeftClickBlock event) {
        if (event.getWorld().isRemote && event.getEntityPlayer().getHeldItemMainhand() != null && event.getEntityPlayer().getHeldItemMainhand().getItem() == ModItems.superWrench) {
            EntityPlayer player = event.getEntityPlayer();
            TileEntity te = player.getEntityWorld().getTileEntity(event.getPos());
            if (te instanceof ITileController && !player.isSneaking()) {
                List<String> list = ((ITileController) te).getStructure().getListOfRequiredBlocks(true);
                IMultiblockStructure next = ((ITileController) te).getStructure().getNextTier();
                if (next != null) {
                    list.add(ColorUtil.DARK_AQUA + LangUtil.toLoc("gui.environmentaltech.nexttier"));
                    list.addAll(next.getListOfRequiredBlocks(false));
                }

                List<ITextComponent> comps = new ArrayList();
                Iterator var7 = list.iterator();

                while (var7.hasNext()) {
                    String string = (String) var7.next();
                    comps.add(new TextComponentString(string));
                }

                ChatUtil.noSpamClient(comps);
            }
        }

    }

    public static boolean isOwner(ITileController tile, EntityPlayer player) {
        UUID id = tile.getOwnerUUID();
        GameProfile pp = player.getGameProfile();
        return (id != null && pp != null) && id.equals(pp.getId());
    }

    public static boolean hasOwner(ITileSlave tile) {
        Owner tp = tile.getOwner();
        return tp != null && tp != Owner.NO_OWNER;
    }

    public static boolean isOwner(ITileSlave tile, EntityPlayer player) {
        Owner own = tile.getOwner();
        if (own != null && !own.equals(Owner.NO_OWNER)) {
            return own.isOwner(player);
        } else {
            return false;
        }
    }

    public static boolean hasOwner(ITileController tile) {
        Owner own = tile.getOwner();
        return own != null && own != Owner.NO_OWNER;
    }
}
