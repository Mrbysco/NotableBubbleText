package com.mrbysco.nbt.config;

import com.mrbysco.nbt.NotableBubbleText;
import com.mrbysco.nbt.client.ConfigCache;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import org.apache.commons.lang3.tuple.Pair;

public class BubbleConfig {
	public static class Client {
		public final ModConfigSpec.IntValue maxTextWidth;
		public final ModConfigSpec.DoubleValue yOffset;
		public final ModConfigSpec.BooleanValue playerBubbles;
		public final ModConfigSpec.BooleanValue nameOffset;
		public final ModConfigSpec.BooleanValue showUsername;

		Client(ModConfigSpec.Builder builder) {
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

			nameOffset = builder
					.comment("Offset the bubble text if the entity's name is rendered above their head")
					.define("nameOffset", true);

			showUsername = builder
					.comment("Show the username of the player who sent the message in the bubble")
					.define("showUsername", true);

			builder.pop();
		}
	}

	public static final ModConfigSpec clientSpec;
	public static final Client CLIENT;

	static {
		final Pair<Client, ModConfigSpec> specPair = new ModConfigSpec.Builder().configure(Client::new);
		clientSpec = specPair.getRight();
		CLIENT = specPair.getLeft();
	}

	public static class Common {
		public final ModConfigSpec.ConfigValue<? extends String> nameKey;

		Common(ModConfigSpec.Builder builder) {
			builder.comment("General settings")
					.push("General");

			nameKey = builder
					.comment("The name for the key used to store the bubble owners' name in the entity's persistent data")
					.define("nameKey", "BubbleOwner");

			builder.pop();
		}
	}

	public static final ModConfigSpec commonSpec;
	public static final Common COMMON;

	static {
		final Pair<Common, ModConfigSpec> specPair = new ModConfigSpec.Builder().configure(Common::new);
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
			ConfigCache.setNameOffset(BubbleConfig.CLIENT.nameOffset.get());
			ConfigCache.setMaxTextWidth(BubbleConfig.CLIENT.maxTextWidth.get());
			ConfigCache.setBubbleOffset(BubbleConfig.CLIENT.yOffset.get());
			ConfigCache.setShowUsername(BubbleConfig.CLIENT.showUsername.get());
		}
	}
}
