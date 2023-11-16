package com.mrbysco.nbt.network.message;

import com.mrbysco.nbt.NotableBubbleText;
import com.mrbysco.nbt.client.BubbleHandler;
import com.mrbysco.nbt.command.BubbleText;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.DistExecutor.SafeRunnable;
import net.minecraftforge.network.NetworkEvent.Context;

import java.io.Serial;
import java.util.UUID;
import java.util.function.Supplier;

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

	public void handle(Supplier<Context> context) {
		Context ctx = context.get();
		ctx.enqueueWork(() -> {
			if (ctx.getDirection().getReceptionSide().isClient()) {
				AddBubble.update(this.mobUUID, this.author, this.message).run();
			}
		});
		ctx.setPacketHandled(true);
	}

	private static class AddBubble {
		private static SafeRunnable update(UUID mobUUID, String author, String message) {
			return new SafeRunnable() {
				@Serial
				private static final long serialVersionUID = 1L;

				@Override
				public void run() {
					Level level = Minecraft.getInstance().level;
					if (level == null) return;

					if (!BubbleHandler.addBubble(author, new BubbleText(author, message, mobUUID, level.getGameTime()))) {
						NotableBubbleText.LOGGER.error("Failed to add bubble for {}", author);
					}
				}
			};
		}
	}
}
