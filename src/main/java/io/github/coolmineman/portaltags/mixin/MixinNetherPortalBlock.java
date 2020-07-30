package io.github.coolmineman.portaltags.mixin;

import org.spongepowered.asm.mixin.Mixin;

import io.github.coolmineman.portaltags.NetherPortalBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.NetherPortalBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

@Mixin(NetherPortalBlock.class)
public class MixinNetherPortalBlock extends Block implements BlockEntityProvider {

    private MixinNetherPortalBlock(Settings settings) {
        super(settings);
    }

    @Override
    public BlockEntity createBlockEntity(BlockView world) {
        return new NetherPortalBlockEntity();
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand,
            BlockHitResult hit) {
        BlockEntity e = world.getBlockEntity(pos);
        if (e instanceof NetherPortalBlockEntity) {
            ActionResult result = ((NetherPortalBlockEntity)e).onUse(state, world, pos, player, hand, hit);
            if (result != ActionResult.PASS) return result;
        }
        return super.onUse(state, world, pos, player, hand, hit);
    }
    
}