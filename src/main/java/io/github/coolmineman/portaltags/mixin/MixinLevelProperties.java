package io.github.coolmineman.portaltags.mixin;

import java.util.Optional;

import com.mojang.datafixers.DataFixer;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.Lifecycle;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import io.github.coolmineman.portaltags.NamedPortalManager;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.world.gen.GeneratorOptions;
import net.minecraft.world.level.LevelInfo;
import net.minecraft.world.level.LevelProperties;
import net.minecraft.world.level.storage.SaveVersionInfo;

@Mixin(LevelProperties.class)
public class MixinLevelProperties {

    @Inject(method = "method_29029", at = @At("HEAD"))
    private static void method_29029(Dynamic<Tag> dynamic, DataFixer dataFixer, int i,
            /* @Nullable */ CompoundTag compoundTag, LevelInfo levelInfo, SaveVersionInfo saveVersionInfo,
            GeneratorOptions generatorOptions, Lifecycle lifecycle, CallbackInfoReturnable<LevelProperties> yes) {
        Optional<Dynamic<Tag>> oTag = dynamic.get("portalTags").result();
        if (oTag.isPresent()) {
            NamedPortalManager.fromTag((CompoundTag) oTag.get().getValue());
        }
    }

    @Inject(method = "updateProperties", at = @At("HEAD"))
    private void updateProperties(DynamicRegistryManager registryTracker, CompoundTag compoundTag,
            CompoundTag compoundTag2, CallbackInfo b) {
        compoundTag.put("portalTags", NamedPortalManager.toTag());
    }
}