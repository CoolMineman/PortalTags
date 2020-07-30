package io.github.coolmineman.portaltags;

import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.NetherPortalBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;

public class NetherPortalBlockEntity extends BlockEntity implements BlockEntityClientSerializable {

    boolean hasTag;
    String tagName;

    public NetherPortalBlockEntity() {
        super(PortalTags.PORTAL_BLOCK_ENTITY);
    }

    public String getName() {
        return tagName;
    }

    public void setName(String name) {
        this.hasTag = true;
        tagName = name;
        sync();
    }

    public BlockPos getLowCorner(Direction.Axis axis, BlockPos pos) {
        BlockPos.Mutable p = new BlockPos.Mutable(pos.getX(), pos.getY(), pos.getZ());
        while (world.getBlockState(p).isOf(Blocks.NETHER_PORTAL)) {
            p.setY(p.getY() - 1);
        }
        p.setY(p.getY() + 1);
        if (axis == Direction.Axis.X) {
            while (world.getBlockState(p).isOf(Blocks.NETHER_PORTAL)) {
                p.setX(p.getX() - 1);
            }
            p.setX(p.getX() + 1);
        } else { //Z
            while (world.getBlockState(p).isOf(Blocks.NETHER_PORTAL)) {
                p.setZ(p.getZ() - 1);
            }
            p.setZ(p.getZ() + 1);
        }
        return p;
    }

    private void setNameBlockEntity(String name, BlockPos yes) {
        BlockEntity e = world.getBlockEntity(yes);
        if (e instanceof NetherPortalBlockEntity) {
            ((NetherPortalBlockEntity)e).setName(name);
        }
    }

    private void startProbeX(String name, BlockPos pos) {
        if (!world.getBlockState(pos).isOf(Blocks.NETHER_PORTAL)) return;
        setNameBlockEntity(name, pos);
        startProbeX(name, pos.up());
        startProbeX(name, pos.east()); //increase x
    }

    private void startProbeZ(String name, BlockPos pos) {
        if (!world.getBlockState(pos).isOf(Blocks.NETHER_PORTAL)) return;
        setNameBlockEntity(name, pos); 
        startProbeX(name, pos.up());
        startProbeZ(name, pos.south()); //increase z
    }

    public void setAllNames(String name, Direction.Axis direction, BlockPos pos) {
        BlockPos lowCorner = getLowCorner(direction, pos);
        if (world.getRegistryKey() == World.OVERWORLD) {
            NamedPortalManager.addOverworldPortal(name, lowCorner);
        } else if (world.getRegistryKey() == World.NETHER) {
            NamedPortalManager.addNetherPortal(name, lowCorner);
        }
        
        if (direction == Direction.Axis.X) {
            startProbeX(name, lowCorner);
        } else {
            startProbeZ(name, lowCorner);
        }
    }

    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ItemStack itemStack = player.getStackInHand(hand);
        if (itemStack.getItem().equals(Items.NAME_TAG)) {
            if (!itemStack.hasCustomName()) {
                return ActionResult.PASS;
            }
            if (!world.isClient) {
                setAllNames(itemStack.getName().asString(), state.get(NetherPortalBlock.AXIS), pos);
            }
            itemStack.decrement(1);
            return ActionResult.SUCCESS;
        }
        return ActionResult.PASS;
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        super.toTag(tag);
        tag.putBoolean("hasTag", hasTag);
        if (hasTag) {
            tag.putString("tagName", tagName);
        }
        return tag;
    }

    @Override
    public void fromTag(BlockState state, CompoundTag tag) {
        super.fromTag(state, tag);
        hasTag = tag.getBoolean("hasTag");
        if (hasTag) {
            tagName = tag.getString("tagName");
        }
    }

    @Override
    public void fromClientTag(CompoundTag tag) {
        this.fromTag(null, tag);
    }

    @Override
    public CompoundTag toClientTag(CompoundTag tag) {
        return this.toTag(tag);
    }
    
}