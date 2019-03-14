package panszelescik.coolgadgets.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import net.minecraft.item.ItemStack;
import panszelescik.coolgadgets.items.ModItems;

@JEIPlugin
public class CoolGadgetsJEIPlugin implements IModPlugin {

    @Override
    public void register(IModRegistry registry) {
        registry.addRecipeCatalyst(new ItemStack(ModItems.crafting), VanillaRecipeCategoryUid.CRAFTING);
    }
}
