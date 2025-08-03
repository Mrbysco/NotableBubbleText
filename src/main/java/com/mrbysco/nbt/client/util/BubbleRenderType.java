package com.mrbysco.nbt.client.util;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat.Mode;
import com.mrbysco.nbt.NotableBubbleText;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.TriState;
import net.neoforged.neoforge.client.event.RegisterRenderBuffersEvent;

public class BubbleRenderType extends RenderType {
	public BubbleRenderType(String nameIn, VertexFormat formatIn, Mode drawMode, int bufferSizeIn, boolean useDelegateIn, boolean needsSortingIn, Runnable setupTaskIn, Runnable clearTaskIn) {
		super(nameIn, formatIn, drawMode, bufferSizeIn, useDelegateIn, needsSortingIn, setupTaskIn, clearTaskIn);
	}
	private static final ResourceLocation BUBBLE_TEXTURE = ResourceLocation.fromNamespaceAndPath(NotableBubbleText.MOD_ID, "textures/block/bubble.png");

	public static final RenderType BUBBLE = create("nbt:bubble",
			DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, VertexFormat.Mode.QUADS, 256,
			false, false,
			CompositeState.builder()
					.setTextureState(new RenderStateShard.TextureStateShard(BUBBLE_TEXTURE, TriState.FALSE, false))
					.setShaderState(POSITION_COLOR_TEX_LIGHTMAP_SHADER)
					.setTransparencyState(NO_TRANSPARENCY)
//						.setWriteMaskState(COLOR_WRITE)
					.setLightmapState(LIGHTMAP)
					.setOverlayState(OVERLAY)
					.createCompositeState(false));

	public static void onRegisterRenderTypes(final RegisterRenderBuffersEvent event) {
		event.registerRenderBuffer(BUBBLE);
	}
}
