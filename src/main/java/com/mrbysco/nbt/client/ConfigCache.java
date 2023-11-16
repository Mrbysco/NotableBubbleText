package com.mrbysco.nbt.client;

public class ConfigCache {

	public static boolean renderPlayerBubbles = false;
	public static double bubbleOffset = 0.5D;
	public static int maxTextWidth = 200;

	public static void setRenderPlayerBubbles(boolean renderPlayerBubbles) {
		ConfigCache.renderPlayerBubbles = renderPlayerBubbles;
	}

	public static void setBubbleOffset(double bubbleOffset) {
		ConfigCache.bubbleOffset = bubbleOffset;
	}

	public static void setMaxTextWidth(int maxWidth) {
		ConfigCache.maxTextWidth = maxWidth;
	}
}
