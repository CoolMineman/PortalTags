package io.github.coolmineman.portaltags;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;

public class PortalTagsClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        BlockEntityRendererRegistry.INSTANCE.register(PortalTags.PORTAL_BLOCK_ENTITY, NetherPortalBlockEntityRenderer::new);
    }
    
}