package panszelescik.coolgadgets;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import panszelescik.coolgadgets.items.ModItems;

@Mod(CoolGadgets.MODID)
public class CoolGadgets {

    public static final String MODID = "coolgadgets";
    public static final Logger logger = LogManager.getFormatterLogger(MODID);

    public CoolGadgets() {
        //FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
    }

    public static final ItemGroup TAB = new ItemGroup(MODID) {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(ModItems.superWrench);
        }
    };

    /*private void setup(final FMLCommonSetupEvent e) {

    }*/

    /*private void enqueueIMC(final InterModEnqueueEvent e) {
        if (Helper.isLoaded("craftingtweaks")) {
            CraftingHelper.registerCraftingTweaks();
        }
    }*/
}