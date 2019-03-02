package panszelescik.coolgadgets.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import panszelescik.coolgadgets.CoolGadgets;
import panszelescik.morelibs.api.ZoomHelper.IZoomable;

public class ItemLunette extends Item implements IZoomable {

    public ItemLunette() {
        super(new Item.Properties()
                .group(CoolGadgets.TAB)
                .maxStackSize(1)
        );
        setRegistryName(CoolGadgets.MODID, "lunette");
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