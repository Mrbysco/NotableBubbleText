package com.mrbysco.nbt.network;

import com.mrbysco.nbt.NotableBubbleText;
import com.mrbysco.nbt.network.handler.ClientPayloadHandler;
import com.mrbysco.nbt.network.message.AddBubblePayload;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class PacketHandler {

	public static void setupPackets(final RegisterPayloadHandlersEvent event) {
		final PayloadRegistrar registrar = event.registrar(NotableBubbleText.MOD_ID).optional();

		registrar.playToClient(AddBubblePayload.ID, AddBubblePayload.CODEC, ClientPayloadHandler.getInstance()::handleData);
	}
}
