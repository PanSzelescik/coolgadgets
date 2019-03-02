package panszelescik.coolgadgets.items;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.Event.Result;
import panszelescik.coolgadgets.CoolGadgets;

import javax.annotation.Nullable;
import java.util.List;

public class ItemSuperWrench extends Item {

    public ItemSuperWrench() {
        super(new Item.Properties()
                .group(CoolGadgets.TAB)
                .maxStackSize(1)
                .rarity(EnumRarity.EPIC)
        );
        setRegistryName(CoolGadgets.MODID, "superwrench");
    }

    @Override
    public boolean doesSneakBypassUse(ItemStack stack, IWorldReader world, BlockPos pos, EntityPlayer player) {
        return true;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(new TextComponentTranslation(getTranslationKey() + ".tooltip"));
    }

    @Override
    public EnumActionResult onItemUseFirst(ItemStack stack, ItemUseContext context) {
        World world = context.getWorld();
        BlockPos pos = context.getPos();
        EnumFacing side = context.getFace();
        EntityPlayer player = context.getPlayer();
        EnumHand hand = player.getActiveHand();
        if (hand == null) {
            hand = EnumHand.MAIN_HAND;
        }
        float hitX = context.getHitX();
        float hitY = context.getHitY();
        float hitZ = context.getHitZ();

        if (world.isAirBlock(pos)) {
            return EnumActionResult.PASS;
        }
        PlayerInteractEvent.RightClickBlock event = new PlayerInteractEvent.RightClickBlock(player, hand, pos, side, new Vec3d(hitX, hitY, hitZ));
        if (MinecraftForge.EVENT_BUS.post(event) || event.getResult() == Result.DENY || event.getUseBlock() == Result.DENY || event.getUseItem() == Result.DENY) {
            return EnumActionResult.PASS;
        }
        IBlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        /*if (BlockHelper.canRotate(block)) {
            world.setBlockState(pos, BlockHelper.rotateVanillaBlock(world, state, pos), 3);
            player.swingArm(hand);
            return ServerHelper.isServerWorld(world) ? EnumActionResult.SUCCESS : EnumActionResult.PASS;
        } else if (!BlockHelper.startWith(block, "botania:") && !BlockHelper.startWith(block, "factorytech:") && !BlockHelper.startWith(block, "ic2:")) {
            */
        if (!context.isPlacerSneaking()) {
            block.rotate(state, world, pos, Rotation.CLOCKWISE_90);
            player.swingArm(hand);
            return EnumActionResult.SUCCESS;
        }
        //}
        return EnumActionResult.PASS;
    }
}