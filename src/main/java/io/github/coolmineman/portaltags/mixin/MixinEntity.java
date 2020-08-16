package io.github.coolmineman.portaltags.mixin;

import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import io.github.coolmineman.portaltags.NamedPortalManager;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.TeleportTarget;

@Mixin(Entity.class)
public class MixinEntity {
    // @Redirect(method = "changeDimension", at = @At(value = "INVOKE", target = "net/minecraft/world/PortalForcer.getPortal(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Direction;DDZ)Lnet/minecraft/block/pattern/BlockPattern$TeleportTarget;"))
    // private BlockPattern.TeleportTarget getThePortal(PortalForcer no, BlockPos pos, Vec3d vec3d, Direction direction, double x, double y, boolean canActivate) {
    //     return NamedPortalManager.getPortal(pos, vec3d, direction, x, y, canActivate, (Entity)(Object)this, no);
    // }
    @Inject(method = "getTeleportTarget", at = @At("HEAD"), cancellable = true)
    protected void getTeleportTarget(ServerWorld destination, CallbackInfoReturnable<TeleportTarget> cb) {
        Optional<TeleportTarget> o = NamedPortalManager.getPortal(((Entity)(Object)this), destination);
        if (o.isPresent()) {
            cb.setReturnValue(o.get());
        }
    }
}