package com.mrbysco.nbt.client.util;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat.Mode;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public class BubbleRenderType extends RenderType {
	public BubbleRenderType(String nameIn, VertexFormat formatIn, Mode drawMode, int bufferSizeIn, boolean useDelegateIn, boolean needsSortingIn, Runnable setupTaskIn, Runnable clearTaskIn) {
		super(nameIn, formatIn, drawMode, bufferSizeIn, useDelegateIn, needsSortingIn, setupTaskIn, clearTaskIn);
	}

	public static RenderType bubble(ResourceLocation texture) {
		return create("nbt:bubble",
				DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, VertexFormat.Mode.QUADS, 256,
				false, false,
				CompositeState.builder()
						.setTextureState(new RenderStateShard.TextureStateShard(texture, false, false))
						.setShaderState(POSITION_COLOR_TEX_LIGHTMAP_SHADER)
						.setTransparencyState(TRANSLUCENT_TRANSPARENCY)
						.setWriteMaskState(COLOR_WRITE)
						.createCompositeState(false));
	}
}
