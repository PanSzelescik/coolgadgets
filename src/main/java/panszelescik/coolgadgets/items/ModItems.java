package panszelescik.coolgadgets.items;

import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModItems {

    public static ItemCalculator calculator = new ItemCalculator();
    public static ItemCrafting crafting = new ItemCrafting();
    public static ItemLunette lunette = new ItemLunette();
    public static ItemSuperWrench superWrench = new ItemSuperWrench();

    @SubscribeEvent
    public static void register(RegistryEvent.Register<Item> e) {
        e.getRegistry().registerAll(calculator, crafting, lunette, superWrench);
    }
}