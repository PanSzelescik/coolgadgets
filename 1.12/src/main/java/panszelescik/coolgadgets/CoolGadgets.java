package panszelescik.coolgadgets;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import panszelescik.coolgadgets.items.ItemSuperWrench;

@Mod(modid = CoolGadgets.MODID, name = CoolGadgets.NAME, version = CoolGadgets.VERSION, acceptedMinecraftVersions = CoolGadgets.ACCEPTED_MINECRAFT_VERSIONS, updateJSON = CoolGadgets.UPDATE_JSON, dependencies = CoolGadgets.DEPENDENCIES)
public class CoolGadgets {
	
	public static final String
		MODID = "coolgadgets",
		NAME = "Cool Gadgets",
		VERSION = "@version@",
		ACCEPTED_MINECRAFT_VERSIONS = "[1.12.2,1.13)",
		UPDATE_JSON = "https://raw.githubusercontent.com/PanSzelescik/coolgadgets/master/update.json",
		DEPENDENCIES = 
			  "required-after:morelibs@[1.0.0,);"
			+ "after:appliedenergistics2;"
			+ "after:buildcraftcore;"
			+ "after:cofhcore;"
			+ "after:enderio;"
			+ "after:extrautils2;"
			+ "after:hammercore;"
			+ "after:immersiveengineering;"
			+ "after:mekanism;"
			+ "after:opencomputers;"
			+ "after:projectred-core;"
			+ "after:reborncore";
	
	@Mod.Instance(MODID)
	public static CoolGadgets instance;
	public static ItemSuperWrench superWrench;
	
	public static final CreativeTabs TAB = new CreativeTabs(MODID) {
		@Override
		public ItemStack createIcon() {
			return new ItemStack(superWrench);
		}
	};
	
	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		superWrench = new ItemSuperWrench();
		ForgeRegistries.ITEMS.register(superWrench);
	}
}