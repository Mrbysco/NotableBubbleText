package com.mrbysco.nbt;

import com.mojang.logging.LogUtils;
import com.mrbysco.nbt.client.ClientHandler;
import com.mrbysco.nbt.command.BubbleCommands;
import com.mrbysco.nbt.config.BubbleConfig;
import com.mrbysco.nbt.network.PacketHandler;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.slf4j.Logger;

@Mod(NotableBubbleText.MOD_ID)
public class NotableBubbleText {
	public static final String MOD_ID = "nbt";
	public static final Logger LOGGER = LogUtils.getLogger();

	public NotableBubbleText() {
		IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
		ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, BubbleConfig.clientSpec, "notablebubbletext-client.toml");
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, BubbleConfig.commonSpec, "notablebubbletext-common.toml");
		eventBus.register(BubbleConfig.class);

		eventBus.addListener(this::commonSetup);
		MinecraftForge.EVENT_BUS.addListener(this::onCommandRegister);

		if (FMLEnvironment.dist == Dist.CLIENT) {
			MinecraftForge.EVENT_BUS.register(new ClientHandler());
			if (ModList.get().isLoaded("geckolib")) {
				MinecraftForge.EVENT_BUS.register(new com.mrbysco.nbt.client.compat.GeckoCompat());
			}
		}
	}

	public void onCommandRegister(RegisterCommandsEvent event) {
		BubbleCommands.initializeCommands(event.getDispatcher());
	}

	public void commonSetup(FMLCommonSetupEvent event) {
		PacketHandler.init();
	}
}
