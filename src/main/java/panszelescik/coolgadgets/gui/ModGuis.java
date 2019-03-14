package panszelescik.coolgadgets.gui;

import de.ellpeck.actuallyadditions.mod.util.compat.CompatUtil;
import net.minecraft.client.gui.inventory.GuiCrafting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import panszelescik.coolgadgets.helper.CraftingHelper;
import panszelescik.morelibs.api.HandHelper;

public class ModGuis implements IGuiHandler {

    public static final int CALCULATOR = 0;
    public static final int CRAFTING = 1;

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID) {
            case CALCULATOR:
                return null;
            case CRAFTING:
                return CraftingHelper.getContainerElement(player, world, x, y, z);
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID) {
            case CALCULATOR:
                return new CalculatorGUI(player.getHeldItem(HandHelper.decrypt(z)));
            case CRAFTING:
                return CraftingHelper.getGuiElement(player, world, x, y, z);
        }
        return null;
    }
}