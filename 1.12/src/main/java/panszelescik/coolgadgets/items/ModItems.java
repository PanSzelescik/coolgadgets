package panszelescik.coolgadgets.items;

import static panszelescik.coolgadgets.CoolGadgets.*;

import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@EventBusSubscriber(modid = MODID)
public class ModItems {
	
	public static final Item calculator = new ItemCalculator();
	public static final Item lunette = new ItemLunette();
	public static final Item superWrench = new ItemSuperWrench();
	
	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> e) {
		e.getRegistry().registerAll(calculator, lunette, superWrench);
	}
}