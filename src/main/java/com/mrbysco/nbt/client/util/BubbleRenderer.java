package com.mrbysco.nbt.client.util;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix4f;
import com.mrbysco.nbt.NotableBubbleText;
import com.mrbysco.nbt.client.ConfigCache;
import com.mrbysco.nbt.command.BubbleText;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;

import java.util.List;

public class BubbleRenderer {
	private static final ResourceLocation BUBBLE_TEXTURE = new ResourceLocation(NotableBubbleText.MOD_ID, "textures/block/bubble.png");

	public static void renderBubbleText(BubbleText bubble, PoseStack poseStack, Font font, MultiBufferSource buffer,
										EntityRenderDispatcher renderDispatcher, float entityHeight, float alpha, int light) {

		String text = bubble.text();
		MutableComponent component = Component.literal(text).withStyle(ChatFormatting.BLACK);
		List<FormattedCharSequence> sequences = font.split(component, ConfigCache.maxTextWidth);
		int textWidth = 0;
		int textHeight = font.lineHeight * sequences.size();
		for (FormattedCharSequence sequence : sequences) {
			textWidth = Math.max(textWidth, font.width(sequence));
		}

		poseStack.pushPose();
		Matrix4f pose = poseStack.last().pose();

		poseStack.translate(0.0D, entityHeight + (ConfigCache.bubbleOffset), 0.0D);
		if (sequences.size() > 1)
			poseStack.translate(0.0D, (0.1F * sequences.size()), 0.0D);
		poseStack.mulPose(renderDispatcher.cameraOrientation());
		poseStack.scale(-0.025F, -0.025F, -0.025F);

		RenderType bubbleType = BubbleRenderType.bubble(BUBBLE_TEXTURE);
		VertexConsumer bubbleBuffer = buffer.getBuffer(bubbleType);
		renderBubble(poseStack, pose, bubbleBuffer, textWidth, textHeight, alpha, light);
		if (buffer instanceof MultiBufferSource.BufferSource source) {
			source.endBatch(bubbleType);
		}

		for (int i = 0; i < sequences.size(); i++) {
			FormattedCharSequence sequence = sequences.get(i);
			float offset = 0;
			if (sequences.size() > 1) {
				offset += font.lineHeight * i + 2;
				offset += (-4F * sequences.size());
			}
			if (sequence != null) {
				font.drawInBatch(sequence, 0, offset, -1, false, pose,
						buffer, false, 0, 15728880);
			}
		}


		poseStack.popPose();
	}

	/**
	 * Render the bubble sized to the text
	 *
	 * @param poseStack    The pose stack
	 * @param pose         The matrix pose
	 * @param bubbleBuffer The buffer to render the bubble
	 * @param textWidth    The width of the text
	 * @param textHeight   The height of the text
	 * @param light        The light value
	 */
	public static void renderBubble(PoseStack poseStack, Matrix4f pose, VertexConsumer bubbleBuffer, int textWidth, int textHeight, float alpha, int light) {
		final int imageSize = 32;
		final float xOffset = -textWidth + 3;
		poseStack.pushPose();

		final float height = textHeight - 6;

		//Render the top center of the bubble
		drawTexture(pose, bubbleBuffer, -textWidth + 3 - xOffset, -textHeight / 2F, 7, 0, 1, 6, textWidth, 6, imageSize, imageSize, alpha, light);
		//Render the left middle of the bubble
		drawTexture(pose, bubbleBuffer, -textWidth - 3 - xOffset, -textHeight / 2F + 6, 0, 7, 6, 1, 6, textHeight - 6, imageSize, imageSize, alpha, light);
		//Render the center of the bubble
		drawTexture(pose, bubbleBuffer, -textWidth + 3 - xOffset, -textHeight / 2F + 6, 7, 7, 1, 1, textWidth, height, imageSize, imageSize, alpha, light);
		//Render the right middle of the bubble
		drawTexture(pose, bubbleBuffer, 3 - xOffset, -textHeight / 2F + 6, 9, 7, 6, 1, 6, height, imageSize, imageSize, alpha, light);
		//Render the bottom center of the bubble
		drawTexture(pose, bubbleBuffer, -textWidth + 3 - xOffset, textHeight / 2F, 7, 9, 1, 6, textWidth, 6, imageSize, imageSize, alpha, light);

		//Render Top left corner of bubble
		drawTexture(pose, bubbleBuffer, -textWidth - 3 - xOffset, -textHeight / 2F, 0, 0, 6, 6, 6, 6, imageSize, imageSize, alpha, light);
		//Render Top right corner of bubble
		drawTexture(pose, bubbleBuffer, 3 - xOffset, -textHeight / 2F, 9, 0, 6, 6, 6, 6, imageSize, imageSize, alpha, light);
		//Render Bottom left corner of bubble
		drawTexture(pose, bubbleBuffer, -textWidth - 3 - xOffset, textHeight / 2F, 0, 9, 6, 6, 6, 6, imageSize, imageSize, alpha, light);
		//Render Bottom right corner of bubble
		drawTexture(pose, bubbleBuffer, 3 - xOffset, textHeight / 2F, 9, 9, 6, 6, 6, 6, imageSize, imageSize, alpha, light);

		//Render the tail of the bubble based on the above uv positions offset using the poseStack
		drawTexture(pose, bubbleBuffer, 3, textHeight / 2F + 5, 0.1F, 0, 16, 5, 5, 5, 5, imageSize, imageSize, alpha, light);

		poseStack.popPose();
	}

	/**
	 * Draws a textured rectangle with the specified position and size.e
	 *
	 * @param pose           The matrix pose
	 * @param vertexConsumer The vertex consumer
	 * @param x              The x position
	 * @param y              The y position
	 * @param u              The u position
	 * @param v              The v position
	 * @param uWidth         The u width
	 * @param vHeight        The v height
	 * @param renderWidth    The render width
	 * @param renderHeight   The render height
	 * @param textureWidth   The texture width
	 * @param textureHeight  The texture height
	 * @param light          The light value
	 */
	public static void drawTexture(Matrix4f pose, VertexConsumer vertexConsumer, float x, float y, float u, float v,
								   float uWidth, float vHeight, float renderWidth, float renderHeight,
								   float textureWidth, float textureHeight, float alpha, int light) {
		drawTexture(pose, vertexConsumer, x, y, 0.0F, u, v, uWidth, vHeight, renderWidth, renderHeight, textureWidth, textureHeight, alpha, light);
	}


	/**
	 * Draws a textured rectangle with the specified position and size.e
	 *
	 * @param pose           The matrix pose
	 * @param vertexConsumer The vertex consumer
	 * @param x              The x position
	 * @param y              The y position
	 * @param z              The z position
	 * @param u              The u position
	 * @param v              The v position
	 * @param uWidth         The u width
	 * @param vHeight        The v height
	 * @param renderWidth    The render width
	 * @param renderHeight   The render height
	 * @param textureWidth   The texture width
	 * @param textureHeight  The texture height
	 * @param light          The light value
	 */
	public static void drawTexture(Matrix4f pose, VertexConsumer vertexConsumer, float x, float y, float z, float u, float v,
								   float uWidth, float vHeight, float renderWidth, float renderHeight,
								   float textureWidth, float textureHeight, float alpha, int light) {
		float wRatio = 1 / textureWidth;
		float hRatio = 1 / textureHeight;

		vertexConsumer.vertex(pose, x, y + renderHeight, z)
				.color(1.0F, 1.0F, 1.0F, alpha)
				.uv(u * wRatio, (v + vHeight) * hRatio)
				.uv2(light)
				.overlayCoords(OverlayTexture.NO_OVERLAY)
				.endVertex();
		vertexConsumer.vertex(pose, x + renderWidth, y + renderHeight, z)
				.color(1.0F, 1.0F, 1.0F, alpha)
				.uv((u + uWidth) * wRatio, (v + vHeight) * hRatio)
				.uv2(light)
				.overlayCoords(OverlayTexture.NO_OVERLAY)
				.endVertex();
		vertexConsumer.vertex(pose, x + renderWidth, y, z)
				.color(1.0F, 1.0F, 1.0F, alpha)
				.uv((u + uWidth) * wRatio, v * hRatio)
				.uv2(light)
				.overlayCoords(OverlayTexture.NO_OVERLAY)
				.endVertex();
		vertexConsumer.vertex(pose, x, y, z)
				.color(1.0F, 1.0F, 1.0F, alpha)
				.uv(u * wRatio, v * hRatio)
				.uv2(light)
				.overlayCoords(OverlayTexture.NO_OVERLAY)
				.endVertex();
	}
}
