package panszelescik.coolgadgets.helper;

import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import shadows.fastbench.gui.ContainerFastBench;
import shadows.fastbench.gui.GuiFastBench;

public class FastCraftingHelper {

    public static Container getContainerElement(EntityPlayer player, World world) {
        return new ContainerFastBench(player, world, BlockPos.ORIGIN) {
            @Override
            public boolean canInteractWith(EntityPlayer playerIn) {
                return true;
            }
        };
    }

    @SideOnly(Side.CLIENT)
    public static Gui getGuiElement(EntityPlayer player, World world) {
        return new GuiFastBench(player.inventory, world, BlockPos.ORIGIN);
    }
}
