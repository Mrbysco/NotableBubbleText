package com.mrbysco.nbt.network;

import com.mrbysco.nbt.NotableBubbleText;
import com.mrbysco.nbt.network.handler.ClientPayloadHandler;
import com.mrbysco.nbt.network.message.AddBubblePayload;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlerEvent;
import net.neoforged.neoforge.network.registration.IPayloadRegistrar;

public class PacketHandler {

	public static void setupPackets(final RegisterPayloadHandlerEvent event) {
		final IPayloadRegistrar registrar = event.registrar(NotableBubbleText.MOD_ID).optional();

		registrar.play(AddBubblePayload.ID, AddBubblePayload::new, handler -> handler
				.client(ClientPayloadHandler.getInstance()::handleData));
	}
}
