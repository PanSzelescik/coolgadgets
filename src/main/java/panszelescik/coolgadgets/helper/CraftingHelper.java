package panszelescik.coolgadgets.helper;

import net.minecraft.client.gui.inventory.GuiCrafting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ContainerWorkbench;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import panszelescik.morelibs.api.Helper;

public class CraftingHelper {

    @SideOnly(Side.CLIENT)
    public static Object getGuiElement(EntityPlayer player, World world, int x, int y, int z) {
        if (Helper.isLoaded("fastbench"))
            return FastCraftingHelper.getGuiElement(player, world);
        return new GuiCrafting(player.inventory, world, new BlockPos(x, y, z));
    }

    public static Object getContainerElement(EntityPlayer player, World world, int x, int y, int z) {
        if (Helper.isLoaded("fastbench"))
            return FastCraftingHelper.getContainerElement(player, world);
        return new ContainerWorkbench(player.inventory, world, new BlockPos(x, y, z)) {
            public boolean canInteractWith(EntityPlayer playerIn) {
                return true;
            }
        };
    }

    public static void registerCraftingTweaks() {
        NBTTagCompound t = new NBTTagCompound();
        if (Helper.isLoaded("fastbench"))
            t.setString("ContainerClass", "panszelescik.coolgadgets.helper.FastCraftingHelper$1");
        else
            t.setString("ContainerClass", "panszelescik.coolgadgets.helper.CraftingHelper$1");
        FMLInterModComms.sendMessage("craftingtweaks", "RegisterProvider", t);
    }
}
