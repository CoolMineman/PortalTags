package io.github.coolmineman.portaltags;

import net.fabricmc.api.ModInitializer;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.registry.Registry;

public class PortalTags implements ModInitializer {
	public static BlockEntityType<NetherPortalBlockEntity> PORTAL_BLOCK_ENTITY;

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		PORTAL_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, "portaltags:portal_entity", BlockEntityType.Builder.create(NetherPortalBlockEntity::new, Blocks.NETHER_PORTAL).build(null));
		System.out.println("Hello Fabric world!");
	}
}
