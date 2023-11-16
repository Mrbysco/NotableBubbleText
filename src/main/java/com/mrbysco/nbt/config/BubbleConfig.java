package com.mrbysco.nbt.config;

import com.mrbysco.nbt.NotableBubbleText;
import com.mrbysco.nbt.client.ConfigCache;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import org.apache.commons.lang3.tuple.Pair;

public class BubbleConfig {
	public static class Client {
		public final ForgeConfigSpec.IntValue maxTextWidth;
		public final ForgeConfigSpec.DoubleValue yOffset;
		public final ForgeConfigSpec.BooleanValue playerBubbles;

		Client(ForgeConfigSpec.Builder builder) {
			builder.comment("General settings")
					.push("General");

			maxTextWidth = builder
					.comment("The maximum width of the bubble text before it wraps to a new line")
					.defineInRange("maxTextWidth", 200, 10, 512);

			yOffset = builder
					.comment("The offset above the entity's head at which the bubble renders")
					.defineInRange("YOffset", 0.5D, 0d, 10d);

			playerBubbles = builder
					.comment("Render chat messages above players heads in a bubble")
					.define("playerBubbles", true);

			builder.pop();
		}
	}

	public static final ForgeConfigSpec clientSpec;
	public static final Client CLIENT;

	static {
		final Pair<Client, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Client::new);
		clientSpec = specPair.getRight();
		CLIENT = specPair.getLeft();
	}

	public static class Common {
		public final ForgeConfigSpec.ConfigValue<? extends String> nameKey;

		Common(ForgeConfigSpec.Builder builder) {
			builder.comment("General settings")
					.push("General");

			nameKey = builder
					.comment("The name for the key used to store the bubble owners' name in the entity's persistent data")
					.define("nameKey", "BubbleOwner");

			builder.pop();
		}
	}

	public static final ForgeConfigSpec commonSpec;
	public static final Common COMMON;

	static {
		final Pair<Common, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Common::new);
		commonSpec = specPair.getRight();
		COMMON = specPair.getLeft();
	}

	@SubscribeEvent
	public static void onLoad(final ModConfigEvent.Loading configEvent) {
		NotableBubbleText.LOGGER.debug("Loaded Notable Bubble Text's config file {}", configEvent.getConfig().getFileName());
	}

	@SubscribeEvent
	public static void onFileChange(final ModConfigEvent.Reloading configEvent) {
		NotableBubbleText.LOGGER.debug("MNotable Bubble Text's config just got changed on the file system!");
	}

	@SubscribeEvent
	public static void onFileChange(final ModConfigEvent configEvent) {
		ModConfig config = configEvent.getConfig();
		refreshCache(config.getType());
	}

	private static void refreshCache(ModConfig.Type type) {
		if (type == ModConfig.Type.CLIENT) {
			ConfigCache.setRenderPlayerBubbles(BubbleConfig.CLIENT.playerBubbles.get());
			ConfigCache.setMaxTextWidth(BubbleConfig.CLIENT.maxTextWidth.get());
			ConfigCache.setBubbleOffset(BubbleConfig.CLIENT.yOffset.get());
		}
	}
}
