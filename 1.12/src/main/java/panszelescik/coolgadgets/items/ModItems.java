package panszelescik.coolgadgets.items;

import static panszelescik.coolgadgets.CoolGadgets.*;

import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@EventBusSubscriber(modid = MODID)
public class ModItems {
	
	public static final ItemCalculator calculator = new ItemCalculator();
	public static final ItemLunette lunette = new ItemLunette();
	public static final ItemSuperWrench superWrench = new ItemSuperWrench();
	
	@SubscribeEvent
	public static void registerRecipes(RegistryEvent.Register<Item> e) {
		e.getRegistry().registerAll(calculator, lunette, superWrench);
	}
}