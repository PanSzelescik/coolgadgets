package panszelescik.coolgadgets.blocks;

import static panszelescik.coolgadgets.CoolGadgets.*;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@EventBusSubscriber(modid = MODID)
public class ModBlocks {
	
	public static final Block blockBreaker = new BlockBreaker();
	
	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> e) {
		e.getRegistry().register(blockBreaker);
	}
	
	@SubscribeEvent
	public static void registerItemBlocks(RegistryEvent.Register<Item> e) {
		e.getRegistry().register(getItemBlock(blockBreaker));
	}
	
	private static final Item getItemBlock(Block block) {
		return new ItemBlock(block).setRegistryName(block.getRegistryName());
	}
}