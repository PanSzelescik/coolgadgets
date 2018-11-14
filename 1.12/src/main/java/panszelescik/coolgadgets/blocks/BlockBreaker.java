package panszelescik.coolgadgets.blocks;

import static panszelescik.coolgadgets.CoolGadgets.*;

import net.minecraft.block.material.Material;
import net.minecraft.util.ResourceLocation;
import panszelescik.morelibs.api.BlockBase;

public class BlockBreaker extends BlockBase {

	public BlockBreaker() {
		super(TAB, Material.IRON);
		setTranslationKey(MODID + ".block_breaker");
		setRegistryName(new ResourceLocation(MODID, "block_breaker"));
	}
}