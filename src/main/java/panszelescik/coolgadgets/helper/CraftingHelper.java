package panszelescik.coolgadgets.helper;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiCrafting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ContainerWorkbench;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.InterModComms;

public class CraftingHelper {

    @OnlyIn(Dist.CLIENT)
    public static GuiScreen getGuiElement(EntityPlayer player, World world, int x, int y, int z) {
        /*if (Helper.isLoaded("fastbench"))
            return FastCraftingHelper.getGuiElement(player, world);*/
        return new GuiCrafting(player.inventory, world, new BlockPos(x, y, z));
    }

    public static Object getContainerElement(EntityPlayer player, World world, int x, int y, int z) {
        /*if (Helper.isLoaded("fastbench"))
            return FastCraftingHelper.getContainerElement(player, world);*/
        return new ContainerWorkbench(player.inventory, world, new BlockPos(x, y, z)) {
            public boolean canInteractWith(EntityPlayer playerIn) {
                return true;
            }
        };
    }

    public static void registerCraftingTweaks() {
        NBTTagCompound t = new NBTTagCompound();
        /*if (Helper.isLoaded("fastbench"))
            t.putString("ContainerClass", "panszelescik.coolgadgets.helper.FastCraftingHelper$1");
        else*/
        t.putString("ContainerClass", "panszelescik.coolgadgets.helper.CraftingHelper$1");
        InterModComms.sendTo("craftingtweaks", "RegisterProvider", () -> t);
    }
}
