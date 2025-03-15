package com.mrbysco.nbt.network.handler;

import com.mrbysco.nbt.NotableBubbleText;
import com.mrbysco.nbt.client.BubbleHandler;
import com.mrbysco.nbt.command.BubbleText;
import com.mrbysco.nbt.network.message.AddBubblePayload;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.UUID;

public class ClientPayloadHandler {
	private static final ClientPayloadHandler INSTANCE = new ClientPayloadHandler();

	public static ClientPayloadHandler getInstance() {
		return INSTANCE;
	}

	public void handleData(final AddBubblePayload data, final IPayloadContext context) {
		context.enqueueWork(() -> {
					Player player = context.player();
					if (player == null) return;
					Level level = player.level();
					if (level == null) return;

					String author = data.author();
					String message = data.message();
					UUID mobUUID = data.mobUUID();
					if (!BubbleHandler.addBubble(data.author(), new BubbleText(author, message, mobUUID, level.getGameTime()))) {
						NotableBubbleText.LOGGER.error("Failed to add bubble for {}", author);
					}
				})
				.exceptionally(e -> {
					// Handle exception
					context.disconnect(Component.translatable("nbt.networking.add_bubble.failed", e.getMessage()));
					return null;
				});
	}
}
