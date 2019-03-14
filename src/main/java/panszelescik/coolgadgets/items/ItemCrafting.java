package panszelescik.coolgadgets.items;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import panszelescik.coolgadgets.CoolGadgets;
import panszelescik.coolgadgets.helper.CraftingHelper;

public class ItemCrafting extends Item {

    public ItemCrafting() {
        super(new Item.Properties()
                .group(CoolGadgets.TAB)
                .maxStackSize(1)
                .rarity(EnumRarity.EPIC)
        );
        setRegistryName(CoolGadgets.MODID, "crafting");
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        player.addStat(StatList.INTERACT_WITH_CRAFTING_TABLE);
        DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
            Minecraft.getInstance().displayGuiScreen(CraftingHelper.getGuiElement(player, world, (int) player.posX, (int) player.posY, (int) player.posZ));
        });
        DistExecutor.runWhenOn(Dist.DEDICATED_SERVER, () -> () -> {
            CraftingHelper.getContainerElement(player, world, (int) player.posX, (int) player.posY, (int) player.posZ);
        });
        return new ActionResult<>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
    }
}