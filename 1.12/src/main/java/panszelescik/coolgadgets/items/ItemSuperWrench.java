package panszelescik.coolgadgets.items;

import appeng.api.implementations.items.IAEWrench;
import blusunrize.immersiveengineering.api.tool.ITool;
import buildcraft.api.tools.IToolWrench;
import cofh.api.item.IToolHammer;
import com.brandon3055.draconicevolution.api.ICrystalBinder;
import com.rwtema.extrautils2.api.tools.IWrench;
import com.zeitheron.hammercore.utils.wrench.IWrenchItem;
import crazypants.enderio.api.tool.IConduitControl;
import li.cil.oc.api.internal.Wrench;
import mcjty.lib.api.smartwrench.SmartWrench;
import mcjty.lib.api.smartwrench.SmartWrenchMode;
import mekanism.api.IMekWrench;
import mrtjp.projectred.api.IScrewdriver;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Optional.Interface;
import net.minecraftforge.fml.common.Optional.InterfaceList;
import net.minecraftforge.fml.common.Optional.Method;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import panszelescik.coolgadgets.CoolGadgets;
import panszelescik.coolgadgets.helper.*;
import panszelescik.morelibs.api.BlockHelper;
import panszelescik.morelibs.api.Helper;
import panszelescik.morelibs.api.ServerHelper;
import reborncore.api.IToolHandler;
import vazkii.botania.api.wand.ICoordBoundItem;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

@InterfaceList({
        @Interface(iface = "appeng.api.implementations.items.IAEWrench", modid = "appliedenergistics2"),
        @Interface(iface = "vazkii.botania.api.wand.ICoordBoundItem", modid = "botania"),
        @Interface(iface = "buildcraft.api.tools.IToolWrench", modid = "buildcraftcore"),
        @Interface(iface = "cofh.api.item.IToolHammer", modid = "cofhcore"),
        @Interface(iface = "com.brandon3055.draconicevolution.api.ICrystalBinder", modid = "draconicevolution"),
        @Interface(iface = "crazypants.enderio.api.tool.IConduitControl", modid = "enderio"),
        @Interface(iface = "crazypants.enderio.api.tool.ITool", modid = "enderio"),
        @Interface(iface = "com.rwtema.extrautils2.api.tools.IWrench", modid = "extrautils2"),
        @Interface(iface = "com.zeitheron.hammercore.utils.wrench.IWrenchItem", modid = "hammercore"),
        @Interface(iface = "blusunrize.immersiveengineering.api.tool.ITool", modid = "immersiveengineering"),
        @Interface(iface = "mcjty.lib.api.smartwrench.SmartWrench", modid = "mcjtylib"),
        @Interface(iface = "mekanism.api.IMekWrench", modid = "mekanism"),
        @Interface(iface = "li.cil.oc.api.internal.Wrench", modid = "opencomputers"),
        @Interface(iface = "mrtjp.projectred.api.IScrewdriver", modid = "projectred-core"),
        @Interface(iface = "reborncore.api.IToolHandler", modid = "reborncore")
})
public class ItemSuperWrench extends Item implements IAEWrench, ICoordBoundItem, IToolWrench, IToolHammer, ICrystalBinder, IConduitControl, crazypants.enderio.api.tool.ITool, IWrench, IWrenchItem, ITool, IMekWrench, Wrench, IScrewdriver, IToolHandler, SmartWrench {

    public ItemSuperWrench() {
        setCreativeTab(CoolGadgets.TAB);
        setHarvestLevel("wrench", 1);
        setMaxStackSize(1);
    }

    @Override
    public boolean doesSneakBypassUse(ItemStack stack, IBlockAccess world, BlockPos pos, EntityPlayer player) {
        if (Helper.isLoaded("botania")) {
            if (!BlockHelper.startWith(world.getBlockState(pos).getBlock(), "botania:")) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean isFull3D() {
        return true;
    }

    @Nonnull
    @Override
    public EnumRarity getRarity(ItemStack stack) {
        return EnumRarity.EPIC;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(Helper.translate(getTranslationKey() + ".tooltip"));
        if (Helper.isLoaded("actuallyadditions")) {
            ActuallyAdditionsHelper.addInformation(stack, tooltip);
        }
    }

    @Nonnull
    @Override
    public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
        if (world.isAirBlock(pos)) {
            return EnumActionResult.PASS;
        }
        PlayerInteractEvent.RightClickBlock event = new PlayerInteractEvent.RightClickBlock(player, hand, pos, side, new Vec3d(hitX, hitY, hitZ));
        if (MinecraftForge.EVENT_BUS.post(event) || event.getResult() == Result.DENY || event.getUseBlock() == Result.DENY || event.getUseItem() == Result.DENY) {
            return EnumActionResult.PASS;
        }
        IBlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        if (BlockHelper.canRotate(block)) {
            world.setBlockState(pos, BlockHelper.rotateVanillaBlock(world, state, pos), 3);
            player.swingArm(hand);
            return ServerHelper.isServerWorld(world) ? EnumActionResult.SUCCESS : EnumActionResult.PASS;
        } else if (!BlockHelper.startWith(block, "botania:") && !BlockHelper.startWith(block, "factorytech:") && !BlockHelper.startWith(block, "ic2:")) {
            if (!player.isSneaking() && block.rotateBlock(world, pos, side)) {
                player.swingArm(hand);
                return EnumActionResult.SUCCESS;
            }
        }
        ItemStack stack = player.getHeldItem(hand);
        TileEntity tile = world.getTileEntity(pos);
        if (Helper.isLoaded("actuallyadditions")) {
            if (BlockHelper.startWith(block, "actuallyadditions:")) {
                try {
                    return ActuallyAdditionsHelper.onItemUse(player, world, pos, stack, tile);
                } catch (Exception e) {
                }
            }
        }
        if (Helper.isLoaded("botania")) {
            if (BlockHelper.startWith(block, "botania:") || BotaniaHelper.isWandable(block) || BotaniaHelper.isWandBindable(tile) || block == Blocks.LAPIS_BLOCK) {
                try {
                    return BotaniaHelper.onItemUse(player, world, pos, hand, side, stack, block);
                } catch (Exception e) {
                }
            }
        }
        if (Helper.isLoaded("factorytech")) {
            if (BlockHelper.startWith(block, "factorytech:")) {
                try {
                    return FactoryTechHelper.onItemUseFirst(player, world, pos, side, stack);
                } catch (Exception e) {
                }
            }
        }
        if (Helper.isLoaded("ic2")) {
            if (BlockHelper.startWith(block, "ic2:") || BlockHelper.startWith(block, "advanced_solar_panels:")) {
                try {
                    if (ServerHelper.isClientWorld(world)) {
                        player.swingArm(hand);
                        return EnumActionResult.PASS;
                    }
                    return IndustrialCraft2Helper.wrenchBlock(player, world, pos, side);
                } catch (Exception e) {
                }
            }
        }
        if (Helper.isLoaded("immersiveengineering")) {
            if (BlockHelper.startWith(block, "immersiveengineering:") || BlockHelper.startWith(block, "immersivepetroleum:") || BlockHelper.startWith(block, "immersivetech:")) {
                try {
                    return ImmersiveEngineeringHelper.onItemUseFirst(player, world, pos, side, state, stack);
                } catch (Exception e) {
                }
            }
        }
        if (Helper.isLoaded("pneumaticcraft")) {
            if (BlockHelper.startWith(block, "pneumaticcraft:")) {
                try {
                    return PneumaticCraftHelper.onItemUseFirst(player, world, pos, side, hand, stack, block);
                } catch (Exception e) {
                }
            }
        }
        return EnumActionResult.PASS;
    }

    @Nonnull
    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        IBlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        TileEntity tile = world.getTileEntity(pos);
        ItemStack stack = player.getHeldItem(hand);
        if (Helper.isLoaded("immersiveengineering")) {
            if (BlockHelper.startWith(block, "immersiveengineering:") || BlockHelper.startWith(block, "immersivepetroleum:") || BlockHelper.startWith(block, "immersivetech:")) {
                try {
                    if (ServerHelper.isClientWorld(world)) {
                        player.swingArm(hand);
                        return EnumActionResult.PASS;
                    }
                    return ImmersiveEngineeringHelper.onItemUse(world, pos, side, tile);
                } catch (Exception e) {
                }
            }
        }
        if (Helper.isLoaded("refinedstorage")) {
            if (BlockHelper.startWith(block, "refinedstorage:")) {
                try {
                    if (ServerHelper.isClientWorld(world)) {
                        player.swingArm(hand);
                        return EnumActionResult.PASS;
                    }
                    return RefinedStorageHelper.wrenchBlock(player, world, pos, state, block, tile);
                } catch (Exception e) {
                }
            }
        }
        return EnumActionResult.PASS;
    }

    /* IAEWrench */
    @Method(modid = "appliedenergistics2")
    @Override
    public boolean canWrench(ItemStack stack, EntityPlayer player, BlockPos pos) {
        player.swingArm(EnumHand.MAIN_HAND);
        return true;
    }

    /* ICoordBoundItem */
    @Method(modid = "botania")
    @Override
    public BlockPos getBinding(ItemStack stack) {
        return BotaniaHelper.getBinding(stack);
    }

    @Method(modid = "botania")
    @Override
    public void onUpdate(ItemStack stack, World world, Entity par3Entity, int par4, boolean par5) {
        BotaniaHelper.onUpdate(stack, world);
    }

    /* IToolWrench */
    @Method(modid = "buildcraftcore")
    @Override
    public boolean canWrench(EntityPlayer player, EnumHand hand, ItemStack wrench, RayTraceResult rayTrace) {
        return true;
    }

    @Method(modid = "buildcraftcore")
    @Override
    public void wrenchUsed(EntityPlayer player, EnumHand hand, ItemStack wrench, RayTraceResult rayTrace) {
        player.swingArm(hand);
    }

    /* IToolHammer */
    @Method(modid = "cofhcore")
    @Override
    public boolean isUsable(ItemStack item, EntityLivingBase player, BlockPos pos) {
        return true;
    }

    @Method(modid = "cofhcore")
    @Override
    public boolean isUsable(ItemStack item, EntityLivingBase player, Entity entity) {
        return true;
    }

    @Method(modid = "cofhcore")
    @Override
    public void toolUsed(ItemStack item, EntityLivingBase player, BlockPos pos) {
        player.swingArm(EnumHand.MAIN_HAND);
    }

    @Method(modid = "cofhcore")
    @Override
    public void toolUsed(ItemStack item, EntityLivingBase player, Entity entity) {
        player.swingArm(EnumHand.MAIN_HAND);
    }

    /* IConduitControl */
    @Method(modid = "enderio")
    @Override
    public boolean showOverlay(ItemStack stack, @Nonnull EntityPlayer player) {
        return true;
    }

    @Method(modid = "enderio")
    @Override
    public boolean shouldHideFacades(@Nonnull ItemStack stack, @Nonnull EntityPlayer player) {
        return true;
    }

    /* ITool - Ender IO */
    @Method(modid = "enderio")
    @Override
    public boolean canUse(@Nonnull EnumHand hand, @Nonnull EntityPlayer player, @Nonnull BlockPos pos) {
        return true;
    }

    @Method(modid = "enderio")
    @Override
    public void used(@Nonnull EnumHand hand, @Nonnull EntityPlayer player, @Nonnull BlockPos pos) {
        player.swingArm(hand);
    }

    /* IWrenchItem */
    @Method(modid = "hammercore")
    @Override
    public boolean canWrench(ItemStack stack) {
        return true;
    }

    @Method(modid = "hammercore")
    @Override
    public void onWrenchUsed(EntityPlayer player, BlockPos pos, EnumHand hand) {
        player.swingArm(hand);
    }

    /* ITool - Immersive Engineering */
    @Method(modid = "immersiveengineering")
    @Override
    public boolean isTool(ItemStack stack) {
        return true;
    }

    /* Integrated Dynamics */
    @Method(modid = "integrateddynamics")
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
        return IntegratedDynamicsHelper.capability();
    }

    /* SmartWrench */
    @Method(modid = "mcjtylib")
    @Override
    public SmartWrenchMode getMode(ItemStack stack) {
        return SmartWrenchMode.MODE_WRENCH;
    }

    /* IMekWrench */
    @Method(modid = "mekanism")
    @Override
    public boolean canUseWrench(ItemStack stack, EntityPlayer player, BlockPos pos) {
        return true;
    }

    /* Wrench */
    @Method(modid = "opencomputers")
    @Override
    public boolean useWrenchOnBlock(EntityPlayer player, World world, BlockPos pos, boolean simulate) {
        return true;
    }

    /* IScrewdriver */
    @Method(modid = "projectred-core")
    @Override
    public boolean canUse(EntityPlayer player, ItemStack stack) {
        return true;
    }

    @Method(modid = "projectred-core")
    @Override
    public void damageScrewdriver(EntityPlayer player, ItemStack stack) {
        player.swingArm(EnumHand.MAIN_HAND);
    }

    /* IToolHandler */
    @Method(modid = "reborncore")
    @Override
    public boolean handleTool(ItemStack stack, BlockPos pos, World world, EntityPlayer player, EnumFacing side, boolean damage) {
        player.swingArm(EnumHand.MAIN_HAND);
        return true;
    }
}