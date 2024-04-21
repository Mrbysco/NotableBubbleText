package com.mrbysco.nbt.client;

public class ConfigCache {

	public static boolean renderPlayerBubbles = false;
	public static boolean nameOffset = false;
	public static double bubbleOffset = 0.5D;
	public static int maxTextWidth = 200;
	public static boolean showUsername = false;

	public static void setRenderPlayerBubbles(boolean renderPlayerBubbles) {
		ConfigCache.renderPlayerBubbles = renderPlayerBubbles;
	}

	public static void setNameOffset(boolean nameOffset) {
		ConfigCache.nameOffset = nameOffset;
	}

	public static void setBubbleOffset(double bubbleOffset) {
		ConfigCache.bubbleOffset = bubbleOffset;
	}

	public static void setMaxTextWidth(int maxWidth) {
		ConfigCache.maxTextWidth = maxWidth;
	}

	public static void setShowUsername(boolean showUsername) {
		ConfigCache.showUsername = showUsername;
	}
}
