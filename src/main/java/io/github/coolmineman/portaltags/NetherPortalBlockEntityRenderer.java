package io.github.coolmineman.portaltags;

import net.minecraft.block.Blocks;
import net.minecraft.block.NetherPortalBlock;
import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Matrix4f;

public class NetherPortalBlockEntityRenderer extends BlockEntityRenderer<NetherPortalBlockEntity> {

    public NetherPortalBlockEntityRenderer(BlockEntityRenderDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void render(NetherPortalBlockEntity entity, float tickDelta, MatrixStack matrices,
            VertexConsumerProvider vertexConsumers, int light, int overlay) {
        BlockPattern.Result r = NetherPortalBlock.findPortal(entity.getWorld(), entity.getPos());
        if (entity.hasTag) {
            if (r.getForwards().getAxis() == Direction.Axis.X) {
                if ( !( r.getFrontTopLeft().getX() == (entity.getPos().getX()) && r.getFrontTopLeft().getY() == (entity.getPos().getY()) && r.getFrontTopLeft().getZ() == (entity.getPos().getZ() - r.getWidth() + 1) ) ) return; 
                renderLabelIfPresent(entity, new LiteralText(entity.tagName), matrices, vertexConsumers, light, 0d, (r.getHeight() * -0.5d) + 0.5d, (r.getWidth() * -0.5d) + 0.5d);
            } else {
                if ( !( r.getFrontTopLeft().getZ() == (entity.getPos().getZ()) && r.getFrontTopLeft().getY() == (entity.getPos().getY()) && r.getFrontTopLeft().getX() == (entity.getPos().getX()) ) ) return; 
                renderLabelIfPresent(entity, new LiteralText(entity.tagName), matrices, vertexConsumers, light, (r.getWidth() * -0.5d) + 0.5d, (r.getHeight() * -0.5d) + 0.5d, 0d);
            }
        }
    }
    
    protected void renderLabelIfPresent(NetherPortalBlockEntity entity, Text text, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, double x, double y, double z) {
        double d = this.dispatcher.camera.getBlockPos().getSquaredDistance(entity.getPos());
        if (d <= 4096.0D) {
            float offset = 0.5F;
            int yOffset = 0;
            matrices.push();

            matrices.translate(x, y, z);

            matrices.translate(offset, offset, offset);
            matrices.multiply(this.dispatcher.camera.getRotation());
            matrices.scale(-0.025F, -0.025F, 0.025F);
            Matrix4f matrix4f = matrices.peek().getModel();
            float g = MinecraftClient.getInstance().options.getTextBackgroundOpacity(0.25F);
            int j = (int)(g * 255.0F) << 24;
            TextRenderer textRenderer = this.dispatcher.getTextRenderer();
            float h = (float)(-textRenderer.getWidth(text) / 2);
            textRenderer.draw(text, h, (float)yOffset, 553648127, false, matrix4f, vertexConsumers, true, j, light);
            textRenderer.draw(text, h, (float)yOffset, -1, false, matrix4f, vertexConsumers, false, 0, light);

            matrices.pop();
        }
    }
}