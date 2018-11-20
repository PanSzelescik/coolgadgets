package panszelescik.coolgadgets.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import panszelescik.morelibs.api.ZoomHelper.IZoomable;

public class ItemLunette extends BaseItem implements IZoomable {
	
	public ItemLunette() {
		super("lunette");
		setMaxStackSize(1);
	}

	@Override
	public boolean canZoom(ItemStack stack, EntityPlayer player) {
		return true;
	}

	@Override
	public float[] getZoomSteps(ItemStack stack, EntityPlayer player) {
		return new float[]{.1f, .15625f, .2f, .25f, .3125f, .4f, .5f, .625f};
	}
}