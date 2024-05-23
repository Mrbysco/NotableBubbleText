package com.mrbysco.nbt.network.message;

import com.mrbysco.nbt.NotableBubbleText;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import java.util.UUID;

public record AddBubblePayload(UUID mobUUID, String author, String message) implements CustomPacketPayload {
	public static final StreamCodec<FriendlyByteBuf, AddBubblePayload> CODEC = CustomPacketPayload.codec(
			AddBubblePayload::write,
			AddBubblePayload::new);
	public static final Type<AddBubblePayload> ID = CustomPacketPayload.createType(new ResourceLocation(NotableBubbleText.MOD_ID, "add_bubble").toString());

	public AddBubblePayload(final FriendlyByteBuf buffer) {
		this(buffer.readUUID(), buffer.readUtf(), buffer.readUtf());
	}

	public void write(FriendlyByteBuf buffer) {
		buffer.writeUUID(mobUUID);
		buffer.writeUtf(author);
		buffer.writeUtf(message);
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return ID;
	}
}
