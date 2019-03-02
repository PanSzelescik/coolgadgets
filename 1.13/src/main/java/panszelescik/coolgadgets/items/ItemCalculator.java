package panszelescik.coolgadgets.items;

import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;
import panszelescik.coolgadgets.CoolGadgets;
import panszelescik.coolgadgets.gui.CalculatorGUI;

import javax.annotation.Nullable;
import java.util.List;

public class ItemCalculator extends Item {

    public ItemCalculator() {
        super(new Item.Properties()
                .group(CoolGadgets.TAB)
                .maxStackSize(1)
        );
        setRegistryName(CoolGadgets.MODID, "calculator");
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
            Minecraft.getInstance().displayGuiScreen(new CalculatorGUI(player.getHeldItem(hand)));
        });
        return new ActionResult<>(EnumActionResult.PASS, player.getHeldItem(hand));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(new TextComponentTranslation(getTranslationKey() + ".tooltip"));
        if (stack.getOrCreateTag().contains("display") && stack.getOrCreateTag().contains("firstTyped")) {
            tooltip.add(new TextComponentTranslation(getTranslationKey() + ".tooltip2", TextFormatting.ITALIC));
        }
    }
}