package panszelescik.coolgadgets.helper;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCommandBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import panszelescik.coolgadgets.CoolGadgets;
import panszelescik.coolgadgets.items.ItemSuperWrench;
import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.api.mana.spark.ISparkEntity;
import vazkii.botania.api.mana.spark.SparkHelper;
import vazkii.botania.api.mana.spark.SparkUpgradeType;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.api.wand.ITileBound;
import vazkii.botania.api.wand.IWandBindable;
import vazkii.botania.api.wand.IWandable;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.BlockPistonRelay;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.tile.TileEnchanter;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.core.handler.ModSounds;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.core.helper.PlayerHelper;
import vazkii.botania.common.core.helper.Vector3;
import vazkii.botania.common.entity.EntitySpark;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lib.LibMisc;
import vazkii.botania.common.network.PacketBotaniaEffect;
import vazkii.botania.common.network.PacketHandler;

import javax.annotation.Nonnull;
import java.awt.*;
import java.lang.reflect.Field;

public class BotaniaHelper {

    private static final String TAG_BOUND_TILE_X = "botaniaX";
    private static final String TAG_BOUND_TILE_Y = "botaniaY";
    private static final String TAG_BOUND_TILE_Z = "botaniaZ";
    private static final BlockPos UNBOUND_POS = new BlockPos(0, -1, 0);

    @Nonnull
    public static EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, ItemStack stack, Block block) {
        BlockPos boundPos = getBoundTile(stack);
        TileEntity boundTile = world.getTileEntity(boundPos);

        if (player.isSneaking()) {
            // Try to complete a binding
            if (boundPos.getY() != -1 && !pos.equals(boundPos)) {
                if (isWandBindable(boundTile)) {
                    if (((IWandBindable) boundTile).bindTo(player, stack, pos, side)) {
                        Vector3 orig = new Vector3(boundPos.getX() + 0.5, boundPos.getY() + 0.5, boundPos.getZ() + 0.5);
                        Vector3 end = new Vector3(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
                        doParticleBeam(world, orig, end);

                        VanillaPacketDispatcher.dispatchTEToNearbyPlayers(world, boundPos);
                        setBoundTile(stack, UNBOUND_POS);
                    }

                    return EnumActionResult.SUCCESS;
                } else {
                    setBoundTile(stack, UNBOUND_POS);
                }
            }

            if (player.canPlayerEdit(pos, side, stack)
                    && (!(block instanceof BlockCommandBlock) || player.canUseCommandBlock())
                    && block.rotateBlock(world, pos, side)) {
                player.swingArm(hand);
                return EnumActionResult.SUCCESS;
            }
        }

        if (block == Blocks.LAPIS_BLOCK && ConfigHandler.enchanterEnabled) {
            EnumFacing.Axis axis = null;
            if (TileEnchanter.canEnchanterExist(world, pos, EnumFacing.Axis.X))
                axis = EnumFacing.Axis.X;
            else if (TileEnchanter.canEnchanterExist(world, pos, EnumFacing.Axis.Z))
                axis = EnumFacing.Axis.Z;

            if (axis != null) {
                if (!world.isRemote) {
                    world.setBlockState(pos, ModBlocks.enchanter.getDefaultState().withProperty(BotaniaStateProps.ENCHANTER_DIRECTION, axis), 1 | 2);
                    world.playSound(null, pos, ModSounds.enchanterForm, SoundCategory.BLOCKS, 0.5F, 0.6F);
                    PlayerHelper.grantCriterion((EntityPlayerMP) player, new ResourceLocation(LibMisc.MOD_ID, "main/enchanter_make"), "code_triggered");
                } else {
                    for (int i = 0; i < 50; i++) {
                        float red = (float) Math.random();
                        float green = (float) Math.random();
                        float blue = (float) Math.random();

                        double x = (Math.random() - 0.5) * 6;
                        double y = (Math.random() - 0.5) * 6;
                        double z = (Math.random() - 0.5) * 6;

                        float velMul = 0.07F;

                        Botania.proxy.wispFX(pos.getX() + 0.5 + x, pos.getY() + 0.5 + y, pos.getZ() + 0.5 + z, red, green, blue, (float) Math.random() * 0.15F + 0.15F, (float) -x * velMul, (float) -y * velMul, (float) -z * velMul);
                    }
                }

                return EnumActionResult.SUCCESS;
            }

            return EnumActionResult.PASS;
        }

        if (isWandable(block)) {
            TileEntity tile = world.getTileEntity(pos);
            boolean bindable = isWandBindable(tile);

            boolean wanded;
            if (bindable && player.isSneaking() && ((IWandBindable) tile).canSelect(player, stack, pos, side)) {
                if (boundPos.equals(pos))
                    setBoundTile(stack, UNBOUND_POS);
                else setBoundTile(stack, pos);

                if (world.isRemote) {
                    player.swingArm(hand);
                    player.playSound(ModSounds.ding, 0.11F, 1F);
                }

                wanded = true;
            } else {
                wanded = ((IWandable) block).onUsedByWand(player, stack, world, pos, side);
                if (wanded && world.isRemote)
                    player.swingArm(hand);
            }

            return wanded ? EnumActionResult.SUCCESS : EnumActionResult.FAIL;
        }

        if (!world.isRemote && ((BlockPistonRelay) ModBlocks.pistonRelay).playerPositions.containsKey(player.getUniqueID())) {
            BlockPistonRelay.DimWithPos bindPos = ((BlockPistonRelay) ModBlocks.pistonRelay).playerPositions.get(player.getUniqueID());
            BlockPistonRelay.DimWithPos currentPos = new BlockPistonRelay.DimWithPos(world.provider.getDimension(), pos);

            ((BlockPistonRelay) ModBlocks.pistonRelay).playerPositions.remove(player.getUniqueID());
            ((BlockPistonRelay) ModBlocks.pistonRelay).mappedPositions.put(bindPos, currentPos);
            BlockPistonRelay.WorldData.get(world).markDirty();

            PacketHandler.sendToNearby(world, pos,
                    new PacketBotaniaEffect(PacketBotaniaEffect.EffectType.PARTICLE_BEAM,
                            bindPos.blockPos.getX() + 0.5, bindPos.blockPos.getY() + 0.5, bindPos.blockPos.getZ() + 0.5,
                            pos.getX(), pos.getY(), pos.getZ()));
            world.playSound(null, player.posX, player.posY, player.posZ, ModSounds.ding, SoundCategory.PLAYERS, 1F, 1F);
            return EnumActionResult.SUCCESS;
        }

        return EnumActionResult.PASS;
    }

    @SubscribeEvent
    public static void playerInteract(PlayerInteractEvent.EntityInteract event) {
        Item item = event.getEntityPlayer().getHeldItem(event.getHand()).getItem();
        EntityPlayer player = event.getEntityPlayer();
        if (player.isSneaking() && event.getTarget() instanceof EntitySpark && item instanceof ItemSuperWrench) {
            EntitySpark entity = (EntitySpark) event.getTarget();
            CoolGadgets.logger.error("sneaking, entityspark, superwrench");
            EnumHand hand = event.getHand();
            World world = event.getWorld();
            ItemStack stack = player.getHeldItem(hand);
            if (!entity.isDead && !stack.isEmpty()) {
                if (world.isRemote) {
                    boolean valid = stack.getItem() == ModItems.twigWand || stack.getItem() == ModItems.sparkUpgrade
                            || stack.getItem() == ModItems.phantomInk;
                    if (valid)
                        player.swingArm(hand);
                    return;
                }

                SparkUpgradeType upgrade = entity.getUpgrade();
                if (player.isSneaking()) {
                    if (upgrade != SparkUpgradeType.NONE) {
                        entity.entityDropItem(new ItemStack(ModItems.sparkUpgrade, 1, upgrade.ordinal() - 1), 0F);
                        entity.setUpgrade(SparkUpgradeType.NONE);

                        //transfers.clear();
                        //removeTransferants = 2;
                    } else dropAndKill(entity);
                    return;
                } else {
                    for (ISparkEntity spark : SparkHelper.getSparksAround(world, entity.posX, entity.posY + (entity.height / 2.0), entity.posZ))
                        entity.particleBeam(player, entity, (Entity) spark);
                    return;
                }
            }
        }
    }

    private static void dropAndKill(EntitySpark entity) {
        SparkUpgradeType upgrade = entity.getUpgrade();
        entity.entityDropItem(new ItemStack(ModItems.spark), 0F);
        if(upgrade !=  SparkUpgradeType.NONE)
            entity.entityDropItem(new ItemStack(ModItems.sparkUpgrade, 1, upgrade.ordinal() - 1), 0F);
        entity.setDead();
    }


    public static void onUpdate(ItemStack par1ItemStack, World world) {
        BlockPos coords = getBoundTile(par1ItemStack);
        TileEntity tile = world.getTileEntity(coords);
        if (tile == null || !(isWandBindable(tile)))
            setBoundTile(par1ItemStack, UNBOUND_POS);
    }

    private static void doParticleBeam(World world, Vector3 orig, Vector3 end) {
        if (!world.isRemote)
            return;

        Vector3 diff = end.subtract(orig);
        Vector3 movement = diff.normalize().multiply(0.05);
        int iters = (int) (diff.mag() / movement.mag());
        float huePer = 1F / iters;
        float hueSum = (float) Math.random();

        Vector3 currentPos = orig;
        for (int i = 0; i < iters; i++) {
            float hue = i * huePer + hueSum;
            Color color = Color.getHSBColor(hue, 1F, 1F);
            float r = color.getRed() / 255F;
            float g = color.getGreen() / 255F;
            float b = color.getBlue() / 255F;

            Botania.proxy.setSparkleFXNoClip(true);
            Botania.proxy.sparkleFX(currentPos.x, currentPos.y, currentPos.z, r, g, b, 0.5F, 4);
            Botania.proxy.setSparkleFXNoClip(false);
            currentPos = currentPos.add(movement);
        }
    }

    private static void setBoundTile(ItemStack stack, BlockPos pos) {
        ItemNBTHelper.setInt(stack, TAG_BOUND_TILE_X, pos.getX());
        ItemNBTHelper.setInt(stack, TAG_BOUND_TILE_Y, pos.getY());
        ItemNBTHelper.setInt(stack, TAG_BOUND_TILE_Z, pos.getZ());
    }

    private static BlockPos getBoundTile(ItemStack stack) {
        int x = ItemNBTHelper.getInt(stack, TAG_BOUND_TILE_X, 0);
        int y = ItemNBTHelper.getInt(stack, TAG_BOUND_TILE_Y, -1);
        int z = ItemNBTHelper.getInt(stack, TAG_BOUND_TILE_Z, 0);
        return new BlockPos(x, y, z);
    }

    public static BlockPos getBinding(ItemStack stack) {
        BlockPos bound = getBoundTile(stack);
        if (bound.getY() != -1)
            return bound;

        RayTraceResult pos = Minecraft.getMinecraft().objectMouseOver;
        if (pos != null && pos.typeOfHit == RayTraceResult.Type.BLOCK) {
            TileEntity tile = Minecraft.getMinecraft().world.getTileEntity(pos.getBlockPos());
            if (tile != null && tile instanceof ITileBound) {
                BlockPos coords = ((ITileBound) tile).getBinding();
                return coords;
            }
        }

        return null;
    }

    public static boolean isWandBindable(TileEntity obj) {
        return obj instanceof IWandBindable;
    }

    public static boolean isWandable(Block obj) {
        return obj instanceof IWandable;
    }
}
