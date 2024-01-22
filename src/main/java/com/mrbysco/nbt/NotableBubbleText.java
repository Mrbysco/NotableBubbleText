package com.mrbysco.nbt;

import com.mojang.logging.LogUtils;
import com.mrbysco.nbt.client.ClientHandler;
import com.mrbysco.nbt.command.BubbleCommands;
import com.mrbysco.nbt.config.BubbleConfig;
import com.mrbysco.nbt.network.PacketHandler;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import org.slf4j.Logger;

@Mod(NotableBubbleText.MOD_ID)
public class NotableBubbleText {
	public static final String MOD_ID = "nbt";
	public static final Logger LOGGER = LogUtils.getLogger();

	public NotableBubbleText(IEventBus eventBus) {
		ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, BubbleConfig.clientSpec, "notablebubbletext-client.toml");
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, BubbleConfig.commonSpec, "notablebubbletext-common.toml");
		eventBus.register(BubbleConfig.class);

		eventBus.addListener(PacketHandler::setupPackets);
		NeoForge.EVENT_BUS.addListener(this::onCommandRegister);

		if (FMLEnvironment.dist.isClient()) {
			NeoForge.EVENT_BUS.register(new ClientHandler());
			if (ModList.get().isLoaded("geckolib")) {
				NeoForge.EVENT_BUS.register(new com.mrbysco.nbt.client.compat.GeckoCompat());
			}
		}
	}

	public void onCommandRegister(RegisterCommandsEvent event) {
		BubbleCommands.initializeCommands(event.getDispatcher());
	}
}
