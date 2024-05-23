package com.mrbysco.nbt.network;

import com.mrbysco.nbt.NotableBubbleText;
import com.mrbysco.nbt.network.message.AddBubbleMessage;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class PacketHandler {
	private static final String PROTOCOL_VERSION = "1";
	public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
			new ResourceLocation(NotableBubbleText.MOD_ID, "main"),
			() -> PROTOCOL_VERSION,
			 NetworkRegistry.acceptMissingOr(PROTOCOL_VERSION),
			 NetworkRegistry.acceptMissingOr(PROTOCOL_VERSION)
	);

	private static int id = 0;

	public static void init() {
		CHANNEL.registerMessage(id++, AddBubbleMessage.class, AddBubbleMessage::encode, AddBubbleMessage::decode, AddBubbleMessage::handle);
	}
}
