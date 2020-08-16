package io.github.coolmineman.portaltags;

import net.minecraft.block.Blocks;
import net.minecraft.block.NetherPortalBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Matrix4f;

public class NetherPortalBlockEntityRenderer extends BlockEntityRenderer<NetherPortalBlockEntity> {

    public NetherPortalBlockEntityRenderer(BlockEntityRenderDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void render(NetherPortalBlockEntity entity, float tickDelta, MatrixStack matrices,
            VertexConsumerProvider vertexConsumers, int light, int overlay) {
        if (!entity.hasTag) return;
        BlockPos.Mutable b = new BlockPos.Mutable(entity.getPos().getX(), entity.getPos().getY(), entity.getPos().getZ());
        while (true) {
            b.move(0, 1, 0);
            if (entity.getWorld().getBlockState(b).getBlock() != Blocks.NETHER_PORTAL) {
                b.move(0, -1, 0);
                break;
            }
        }

        if (entity.getPos().getY() != b.getY()) {
            return;
        }

        double xOffset = 0;
        double yOffset = 0;
        double zOffset = 0;

        switch(entity.getWorld().getBlockState(b).get(NetherPortalBlock.AXIS)) {
        case Z:
            while (true) {
                b.move(0, 0, 1);
                if (entity.getWorld().getBlockState(b).getBlock() != Blocks.NETHER_PORTAL) {
                    b.move(0, 0, -1);
                    break;
                }
            }

            if (entity.getPos().getZ() != b.getZ()) {
                return;
            }

            while (true) {
                b.move(0, 0, -1);
                if (entity.getWorld().getBlockState(b).getBlock() == Blocks.NETHER_PORTAL) {
                    zOffset += -0.5;
                } else {
                    b.move(0, 0, 1);
                    break;
                }
            }

            break;
        case X:
        default:
            while (true) {
                b.move(1, 0, 0);
                if (entity.getWorld().getBlockState(b).getBlock() != Blocks.NETHER_PORTAL) {
                    b.move(-1, 0, 0);
                    break;
                }
            }

            if (entity.getPos().getX() != b.getX()) {
                return;
            }

            while (true) {
                b.move(-1, 0, 0);
                if (entity.getWorld().getBlockState(b).getBlock() == Blocks.NETHER_PORTAL) {
                    xOffset += -0.5;
                } else {
                    b.move(1, 0, 0);
                    break;
                }
            }
        }
        while (true) {
            b.move(0, -1, 0);
            if (entity.getWorld().getBlockState(b).getBlock() == Blocks.NETHER_PORTAL) {
                yOffset += -0.5;
            } else {
                break;
            }
        }
        renderLabelIfPresent(entity, new LiteralText(entity.tagName), matrices, vertexConsumers, light, xOffset, yOffset, zOffset);
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