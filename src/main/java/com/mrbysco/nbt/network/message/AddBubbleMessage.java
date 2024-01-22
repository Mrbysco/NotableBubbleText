package com.mrbysco.nbt.network.message;

import com.mrbysco.nbt.NotableBubbleText;
import com.mrbysco.nbt.client.BubbleHandler;
import com.mrbysco.nbt.command.BubbleText;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.NetworkEvent.Context;

import java.util.UUID;

public class AddBubbleMessage {

	private final UUID mobUUID;
	private final String author, message;

	public AddBubbleMessage(UUID mobUUID, String author, String message) {
		this.mobUUID = mobUUID;
		this.author = author;
		this.message = message;
	}

	public void encode(FriendlyByteBuf buffer) {
		buffer.writeUUID(mobUUID);
		buffer.writeUtf(author);
		buffer.writeUtf(message);
	}

	public static AddBubbleMessage decode(final FriendlyByteBuf buffer) {
		return new AddBubbleMessage(buffer.readUUID(), buffer.readUtf(), buffer.readUtf());
	}

	public void handle(Context ctx) {
		ctx.enqueueWork(() -> {
			if (ctx.getDirection().getReceptionSide().isClient()) {
				if (FMLEnvironment.dist.isClient()) {
					Level level = Minecraft.getInstance().level;
					if (level == null) return;

					if (!BubbleHandler.addBubble(author, new BubbleText(author, message, mobUUID, level.getGameTime()))) {
						NotableBubbleText.LOGGER.error("Failed to add bubble for {}", author);
					}
				}
			}
		});
		ctx.setPacketHandled(true);
	}
}
