package com.mrbysco.nbt.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mrbysco.nbt.NotableBubbleText;
import com.mrbysco.nbt.config.BubbleConfig;
import com.mrbysco.nbt.network.PacketHandler;
import com.mrbysco.nbt.network.message.AddBubbleMessage;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.MessageArgument;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BubbleCommands {
	public static void initializeCommands(CommandDispatcher<CommandSourceStack> dispatcher) {
		final LiteralArgumentBuilder<CommandSourceStack> root = Commands.literal("bubbletext");

		root.requires((source) -> source.hasPermission(2))
				.then(Commands.argument("author", StringArgumentType.word())
						.then(Commands.argument("text", MessageArgument.message())
								.executes(BubbleCommands::addBubble)
						)
				);

		dispatcher.register(root);
	}

	private static int addBubble(CommandContext<CommandSourceStack> ctx) {
		String author = StringArgumentType.getString(ctx, "author");
		String text = "";
		try {
			text = MessageArgument.getMessage(ctx, "text").getString();
		} catch (Exception e) {
			//Ignore
		}
		String finalText = text;

		MinecraftServer server = ctx.getSource().getServer();
		String bubbleKey = BubbleConfig.COMMON.nameKey.get();
		server.getAllLevels().forEach(level -> {
			if (level.players().isEmpty()) return;
			List<ServerPlayer> playerList = level.players();

			List<Entity> entityList = new ArrayList<>();
			level.getAllEntities().forEach(entityList::add);
			entityList.removeIf(entity ->
					!entity.getPersistentData().contains(bubbleKey) || !entity.getPersistentData().getString(bubbleKey).equals(author));

			for (ServerPlayer player : playerList) {
				UUID uuid = null;
				for (Entity entity : entityList) {
					if (entity.blockPosition().distManhattan(player.blockPosition()) < 2000) {
						uuid = entity.getUUID();
						break;
					}
				}

				if (uuid == null) {
					NotableBubbleText.LOGGER.debug("Ignoring bubble for {} because no entity was found", player.getName().getString());
				} else {
					PacketHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new AddBubbleMessage(uuid, author, finalText));
				}
			}
		});

		return 0;
	}
}
