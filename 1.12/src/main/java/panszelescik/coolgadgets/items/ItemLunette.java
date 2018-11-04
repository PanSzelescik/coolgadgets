package panszelescik.coolgadgets.items;

import static panszelescik.coolgadgets.CoolGadgets.*;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import panszelescik.morelibs.api.ItemBase;
import panszelescik.morelibs.api.ZoomHelper.IZoomable;

public class ItemLunette extends ItemBase implements IZoomable {
	
	float[] zoomSteps = new float[]{.1f, .15625f, .2f, .25f, .3125f, .4f, .5f, .625f};
	
	public ItemLunette() {
		super(TAB);
		setTranslationKey(MODID + ".lunette");
		setRegistryName(new ResourceLocation(MODID, "lunette"));
		setMaxStackSize(1);
	}

	@Override
	public boolean canZoom(ItemStack stack, EntityPlayer player) {
		return true;
	}

	@Override
	public float[] getZoomSteps(ItemStack stack, EntityPlayer player) {
		return zoomSteps;
	}

}