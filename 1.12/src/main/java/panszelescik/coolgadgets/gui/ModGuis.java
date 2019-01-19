package panszelescik.coolgadgets.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import panszelescik.morelibs.api.HandHelper;

public class ModGuis implements IGuiHandler {

    public static final int CALCULATOR = 0;

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID) {
            case CALCULATOR:
                return null;
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID) {
            case CALCULATOR:
                return new CalculatorGUI(player.getHeldItem(HandHelper.decrypt(z)));
        }
        return null;
    }
}