package com.robertx22.mine_and_slash.blocks.beacon;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.tileentity.BeaconTileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

import java.util.List;

public class CosmeticBeaconRenderer extends TileEntityRenderer<CosmeticBeaconTile> {
    public static final ResourceLocation TEXTURE_BEACON_BEAM = new ResourceLocation("textures/entity/beacon_beam.png");

    public CosmeticBeaconRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(CosmeticBeaconTile tileEntityIn, float partialTicks, MatrixStack matrixStackIn,
                       IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        try {
            long i = tileEntityIn.getWorld().getGameTime();
            List<BeaconTileEntity.BeamSegment> list = tileEntityIn.getBeams();
            int j = 0;

            for (int k = 0; k < list.size(); ++k) {
                BeaconTileEntity.BeamSegment segment = list.get(k);
                renderBeamSegment(matrixStackIn, bufferIn, partialTicks, i, j,
                                  k == list.size() - 1 ? 1024 : segment.getHeight(), segment.getColors()
                );
                j += segment.getHeight();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static void renderBeamSegment(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, float partialTicks,
                                          long totalWorldTime, int yOffset, int height, float[] colors) {
        renderBeamSegment(matrixStackIn, bufferIn, TEXTURE_BEACON_BEAM, partialTicks, 1.0F, totalWorldTime, yOffset,
                          height, colors, 0.2F, 0.25F
        );
    }

    public static void renderBeamSegment(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn,
                                         ResourceLocation textureLocation, float partialTicks, float textureScale,
                                         long totalWorldTime, int yOffset, int height, float[] colors, float beamRadius,
                                         float glowRadius) {
        int i = yOffset + height;
        matrixStackIn.push();
        matrixStackIn.translate(0.5D, 0.0D, 0.5D);
        float f = (float) Math.floorMod(totalWorldTime, 40L) + partialTicks;
        float f1 = height < 0 ? f : -f;
        float f2 = MathHelper.frac(f1 * 0.2F - (float) MathHelper.floor(f1 * 0.1F));
        float f3 = colors[0];
        float f4 = colors[1];
        float f5 = colors[2];
        matrixStackIn.push();
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(f * 2.25F - 45.0F));
        float f6 = 0.0F;
        float f8 = 0.0F;
        float f9 = -beamRadius;
        float f10 = 0.0F;
        float f11 = 0.0F;
        float f12 = -beamRadius;
        float f13 = 0.0F;
        float f14 = 1.0F;
        float f15 = -1.0F + f2;
        float f16 = (float) height * textureScale * (0.5F / beamRadius) + f15;
        renderPart(matrixStackIn, bufferIn.getBuffer(RenderType.beaconBeam(textureLocation, false)), f3, f4, f5, 1.0F,
                   yOffset, i, 0.0F, beamRadius, beamRadius, 0.0F, f9, 0.0F, 0.0F, f12, 0.0F, 1.0F, f16, f15
        );
        matrixStackIn.pop();
        f6 = -glowRadius;
        float f7 = -glowRadius;
        f8 = -glowRadius;
        f9 = -glowRadius;
        f13 = 0.0F;
        f14 = 1.0F;
        f15 = -1.0F + f2;
        f16 = (float) height * textureScale + f15;
        renderPart(matrixStackIn, bufferIn.getBuffer(RenderType.beaconBeam(textureLocation, true)), f3, f4, f5, 0.125F,
                   yOffset, i, f6, f7, glowRadius, f8, f9, glowRadius, glowRadius, glowRadius, 0.0F, 1.0F, f16, f15
        );
        matrixStackIn.pop();
    }

    private static void renderPart(MatrixStack matrixStackIn, IVertexBuilder bufferIn, float red, float green,
                                   float blue, float alpha, int yMin, int yMax, float p_228840_8_, float p_228840_9_,
                                   float p_228840_10_, float p_228840_11_, float p_228840_12_, float p_228840_13_,
                                   float p_228840_14_, float p_228840_15_, float u1, float u2, float v1, float v2) {
        MatrixStack.Entry matrixstack$entry = matrixStackIn.getLast();
        Matrix4f matrix4f = matrixstack$entry.getPositionMatrix();
        Matrix3f matrix3f = matrixstack$entry.getNormalMatrix();
        addQuad(matrix4f, matrix3f, bufferIn, red, green, blue, alpha, yMin, yMax, p_228840_8_, p_228840_9_,
                p_228840_10_, p_228840_11_, u1, u2, v1, v2
        );
        addQuad(matrix4f, matrix3f, bufferIn, red, green, blue, alpha, yMin, yMax, p_228840_14_, p_228840_15_,
                p_228840_12_, p_228840_13_, u1, u2, v1, v2
        );
        addQuad(matrix4f, matrix3f, bufferIn, red, green, blue, alpha, yMin, yMax, p_228840_10_, p_228840_11_,
                p_228840_14_, p_228840_15_, u1, u2, v1, v2
        );
        addQuad(matrix4f, matrix3f, bufferIn, red, green, blue, alpha, yMin, yMax, p_228840_12_, p_228840_13_,
                p_228840_8_, p_228840_9_, u1, u2, v1, v2
        );
    }

    private static void addQuad(Matrix4f matrixPos, Matrix3f matrixNormal, IVertexBuilder bufferIn, float red,
                                float green, float blue, float alpha, int yMin, int yMax, float x1, float z1, float x2,
                                float z2, float u1, float u2, float v1, float v2) {
        addVertex(matrixPos, matrixNormal, bufferIn, red, green, blue, alpha, yMax, x1, z1, u2, v1);
        addVertex(matrixPos, matrixNormal, bufferIn, red, green, blue, alpha, yMin, x1, z1, u2, v2);
        addVertex(matrixPos, matrixNormal, bufferIn, red, green, blue, alpha, yMin, x2, z2, u1, v2);
        addVertex(matrixPos, matrixNormal, bufferIn, red, green, blue, alpha, yMax, x2, z2, u1, v1);
    }

    private static void addVertex(Matrix4f matrixPos, Matrix3f matrixNormal, IVertexBuilder bufferIn, float red,
                                  float green, float blue, float alpha, int y, float x, float z, float texU,
                                  float texV) {
        bufferIn.pos(matrixPos, x, (float) y, z)
                .color(red, green, blue, alpha)
                .tex(texU, texV)
                .overlay(OverlayTexture.DEFAULT_LIGHT)
                .lightmap(15728880)
                .normal(matrixNormal, 0.0F, 1.0F, 0.0F)
                .endVertex();
    }

    @Override
    public boolean isGlobalRenderer(CosmeticBeaconTile te) {
        return true;
    }
}