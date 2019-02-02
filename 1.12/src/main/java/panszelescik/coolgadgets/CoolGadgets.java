package panszelescik.coolgadgets;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import panszelescik.coolgadgets.helper.BotaniaClientHelper;
import panszelescik.coolgadgets.helper.BotaniaHelper;
import panszelescik.coolgadgets.helper.CraftingHelper;
import panszelescik.coolgadgets.items.ModItems;
import panszelescik.coolgadgets.proxy.CommonProxy;
import panszelescik.morelibs.api.Helper;

@Mod(modid = CoolGadgets.MODID, name = "Cool Gadgets", version = "@version@", acceptedMinecraftVersions = "[1.12.2,1.13)", updateJSON = "https://raw.githubusercontent.com/PanSzelescik/coolgadgets/master/update.json", dependencies = "required-after:morelibs@[1.2.0,);after:patchouli@[1.0-13,);after:actuallyadditions;after:appliedenergistics2;after:botania;after:buildcraftcore;after:cofhcore;after:draconicevolution;after:enderio;after:extrautils2;after:factorytech;after:hammercore;after:ic2;after:immersiveengineering;after:integrateddynamics;after:mcjtylib;after:mekanism;after:opencomputers;after:pneumaticcraft;after:projectred-core;after:reborncore;after:refinedstorage")
public class CoolGadgets {

    public static final String MODID = "coolgadgets";

    @Mod.Instance(MODID)
    public static CoolGadgets instance;

    @SidedProxy(serverSide = "panszelescik.coolgadgets.proxy.CommonProxy", clientSide = "panszelescik.coolgadgets.proxy.ClientProxy")
    public static CommonProxy proxy;

    public static final Logger logger = LogManager.getFormatterLogger(MODID);
    public static final CreativeTabs TAB = new CreativeTabs(MODID) {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(ModItems.superWrench);
        }
    };

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent e) {
        if (Helper.isLoaded("craftingtweaks")) {
            CraftingHelper.registerCraftingTweaks();
        }
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent e) {
        proxy.init();
        if (Helper.isLoaded("botania")) {
            MinecraftForge.EVENT_BUS.register(BotaniaHelper.class);
            MinecraftForge.EVENT_BUS.register(BotaniaClientHelper.class);
        }
    }
}