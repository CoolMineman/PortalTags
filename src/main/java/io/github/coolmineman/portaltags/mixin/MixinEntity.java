package io.github.coolmineman.portaltags.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import io.github.coolmineman.portaltags.NamedPortalManager;
import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.PortalForcer;

@Mixin(Entity.class)
public class MixinEntity {
    @Redirect(method = "changeDimension", at = @At(value = "INVOKE", target = "net/minecraft/world/PortalForcer.getPortal(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Direction;DDZ)Lnet/minecraft/block/pattern/BlockPattern$TeleportTarget;"))
    private BlockPattern.TeleportTarget getThePortal(PortalForcer no, BlockPos pos, Vec3d vec3d, Direction direction, double x, double y, boolean canActivate) {
        return NamedPortalManager.getPortal(pos, vec3d, direction, x, y, canActivate, (Entity)(Object)this, no);
    }
}