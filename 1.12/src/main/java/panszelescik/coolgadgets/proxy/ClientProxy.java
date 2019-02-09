package panszelescik.coolgadgets.proxy;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import panszelescik.coolgadgets.CoolGadgets;
import panszelescik.coolgadgets.gui.ModGuis;
import panszelescik.coolgadgets.helper.BotaniaClientHelper;
import panszelescik.morelibs.api.Helper;

public class ClientProxy extends CommonProxy {

    @Override
    public void init() {
        NetworkRegistry.INSTANCE.registerGuiHandler(CoolGadgets.instance, new ModGuis());
        if (Helper.isLoaded("botania")) {
            MinecraftForge.EVENT_BUS.register(BotaniaClientHelper.class);
        }
    }
}