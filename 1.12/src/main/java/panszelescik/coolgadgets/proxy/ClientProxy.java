package panszelescik.coolgadgets.proxy;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import panszelescik.coolgadgets.CoolGadgets;
import panszelescik.coolgadgets.gui.ModGuis;

public class ClientProxy extends CommonProxy {

    @Override
    public void init() {
        NetworkRegistry.INSTANCE.registerGuiHandler(CoolGadgets.instance, new ModGuis());
    }
}