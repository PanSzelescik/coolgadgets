package panszelescik.coolgadgets.helper;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.profiler.Profiler;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import panszelescik.coolgadgets.items.ModItems;
import vazkii.botania.api.wand.IWandHUD;
import vazkii.botania.common.core.helper.PlayerHelper;

public class BotaniaClientHelper {

    @SubscribeEvent
    public static void onDrawScreenPost(RenderGameOverlayEvent.Post event) {
        Minecraft mc = Minecraft.getMinecraft();
        Profiler profiler = mc.profiler;

        if (event.getType() == RenderGameOverlayEvent.ElementType.ALL) {
            profiler.startSection("coolgadgets-botania-hud");
            RayTraceResult pos = mc.objectMouseOver;

            if (pos != null) {
                IBlockState state = pos.typeOfHit == RayTraceResult.Type.BLOCK ? mc.world.getBlockState(pos.getBlockPos()) : null;
                Block block = state == null ? null : state.getBlock();
                TileEntity tile = pos.typeOfHit == RayTraceResult.Type.BLOCK ? mc.world.getTileEntity(pos.getBlockPos()) : null;

                if (PlayerHelper.hasAnyHeldItem(mc.player)) {
                    if (pos != null && PlayerHelper.hasHeldItem(mc.player, ModItems.superWrench)) {
                        if (block instanceof IWandHUD) {
                            profiler.startSection("coolgadgets-wandItem");
                            ((IWandHUD) block).renderHUD(mc, event.getResolution(), mc.world, pos.getBlockPos());
                            profiler.endSection();
                        }
                    }
                }
            }
        }
    }
}
