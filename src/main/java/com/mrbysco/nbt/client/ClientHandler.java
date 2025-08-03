package com.mrbysco.nbt.client;

import com.google.common.reflect.TypeToken;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mrbysco.nbt.NotableBubbleText;
import com.mrbysco.nbt.client.util.BubbleRenderer;
import com.mrbysco.nbt.command.BubbleText;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.entity.state.PlayerRenderState;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.context.ContextKey;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ClientChatReceivedEvent;
import net.neoforged.neoforge.client.event.RenderLivingEvent;
import net.neoforged.neoforge.client.event.RenderPlayerEvent;
import net.neoforged.neoforge.client.renderstate.RegisterRenderStateModifiersEvent;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class ClientHandler {
	public static final ContextKey<UUID> UUID = new ContextKey<>(ResourceLocation.fromNamespaceAndPath(NotableBubbleText.MOD_ID, "uuid"));
	public static final ContextKey<EntityDimensions> DIMENSIONS = new ContextKey<>(ResourceLocation.fromNamespaceAndPath(NotableBubbleText.MOD_ID, "dimensions"));
	public static final ContextKey<Boolean> SHOW_NAME = new ContextKey<>(ResourceLocation.fromNamespaceAndPath(NotableBubbleText.MOD_ID, "show_name"));
	public static final ContextKey<Boolean> INVISIBLE = new ContextKey<>(ResourceLocation.fromNamespaceAndPath(NotableBubbleText.MOD_ID, "invisible"));

	public static void registerCustomRenderData(RegisterRenderStateModifiersEvent event) {


		event.registerEntityModifier(new TypeToken<LivingEntityRenderer<?, ?, ?>>() {
		                             }, (living, state) -> {
					state.setRenderData(UUID, living.getUUID());
					state.setRenderData(SHOW_NAME, living.shouldShowName());
					state.setRenderData(DIMENSIONS, living.getDimensions(living.getPose()));

					Minecraft mc = Minecraft.getInstance();
					Player localPlayer = mc.player;
					if (localPlayer == null) return;
					state.setRenderData(INVISIBLE, living.isInvisibleTo(localPlayer));
				}

		);
	}

	@SubscribeEvent
	public <T extends LivingEntity, S extends LivingEntityRenderState> void onEntityRender(RenderLivingEvent.Post<T, S, ? extends EntityModel<S>> event) {
		final float partialTick = event.getPartialTick();
		final Minecraft mc = Minecraft.getInstance();
		final Player localPlayer = mc.player;
		if (localPlayer == null) return;

		final LivingEntityRenderState renderState = event.getRenderState();
		if (renderState.getRenderDataOrDefault(INVISIBLE, false)) return;
		final UUID uuid = renderState.getRenderDataOrDefault(UUID, Util.NIL_UUID);

		String author = BubbleHandler.getAuthor(uuid);
		if (!author.isEmpty()) {
			List<BubbleText> bubbles = BubbleHandler.getBubbles(author);
			if (bubbles.isEmpty()) return;
			BubbleText bubble = bubbles.getFirst();

			final Level level = mc.level;
			if (level == null) return;
			final long currentTime = level.getGameTime();

			long bubbleTime = bubbles.size() > 1 ? 50 : 200;
			long bubbleAge = bubble.getAge(currentTime);
			float bubbleAlpha = bubble.getAlpha(currentTime);

			final Font font = mc.font;
			final PoseStack poseStack = event.getPoseStack();
			final EntityDimensions dimensions = renderState.getRenderDataOrDefault(DIMENSIONS, EntityDimensions.fixed(0.0F, 0.0F));
			final MultiBufferSource multiBufferSource = event.getMultiBufferSource();
			final EntityRenderDispatcher renderDispatcher = mc.getEntityRenderDispatcher();
			final double nameOffset = getNameOffset(renderState);

			BubbleRenderer.renderBubbleText(bubble, poseStack, font, multiBufferSource, renderDispatcher,
					dimensions.height(), bubbleAlpha, event.getPackedLight(), nameOffset);

			if (bubbleAge > bubbleTime) {
				BubbleHandler.removeBubble(bubble);
			}
		}
	}

	public static double getNameOffset(LivingEntityRenderState livingEntityRenderState) {
		double nameOffset = 0.0D;
		if (!ConfigCache.nameOffset) {
			return nameOffset;
		}

		boolean shouldShow = livingEntityRenderState.getRenderDataOrDefault(SHOW_NAME, false);

		boolean flag = shouldShow || livingEntityRenderState.customName != null;
		if (!flag) return nameOffset;

		Vec3 vec3 = livingEntityRenderState.nameTagAttachment;
		if (flag && vec3 != null) {
			nameOffset += vec3.y * 0.3125D;
		}

		return nameOffset;
	}

	@SubscribeEvent
	public void onPlayerRender(RenderPlayerEvent.Post event) {
		if (!ConfigCache.renderPlayerBubbles) return;
		final Minecraft mc = Minecraft.getInstance();
		final Player localPlayer = mc.player;
		if (localPlayer == null) return;

		final PlayerRenderState renderState = event.getRenderState();
		if (renderState.getRenderDataOrDefault(INVISIBLE, false)) return;
		final float partialTick = event.getPartialTick();
		final UUID uuid = renderState.getRenderDataOrDefault(UUID, Util.NIL_UUID);

		List<BubbleText> bubbles = BubbleHandler.getPlayerBubbles(uuid);
		if (!bubbles.isEmpty()) {
			BubbleText bubble = bubbles.getFirst();

			final Level level = mc.level;
			if (level == null) return;
			final long currentTime = level.getGameTime();

			long bubbleTime = bubbles.size() > 1 ? 50 : 200;
			long bubbleAge = bubble.getAge(currentTime);
			float bubbleAlpha = bubble.getAlpha(currentTime);

			final Font font = mc.font;
			final PoseStack poseStack = event.getPoseStack();
			final EntityDimensions dimensions = renderState.getRenderDataOrDefault(DIMENSIONS, EntityDimensions.fixed(0.0F, 0.0F));
			final MultiBufferSource multiBufferSource = event.getMultiBufferSource();
			final EntityRenderDispatcher renderDispatcher = mc.getEntityRenderDispatcher();
			final double nameOffset = getNameOffset(renderState);

			BubbleRenderer.renderBubbleText(bubble, poseStack, font, multiBufferSource, renderDispatcher,
					dimensions.height(), bubbleAlpha, event.getPackedLight(), nameOffset);

			if (bubbleAge > bubbleTime) {
				BubbleHandler.removePlayerBubble(bubble);
			}
		}
	}


	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onPlayerReceiveChat(ClientChatReceivedEvent.Player event) {
		if (!ConfigCache.renderPlayerBubbles) return;

		final UUID sender = event.getSender();
		final Component message = event.getMessage();
		final Minecraft mc = Minecraft.getInstance();
		final Player player = mc.player;
		if (player == null) return;
		final Level level = mc.level;
		if (level == null) return;

		Player senderPlayer = level.getPlayerByUUID(sender);
		if (senderPlayer == null) return;

		if (player.blockPosition().distManhattan(senderPlayer.blockPosition()) < 2000 || player.level().dimension().location().equals(senderPlayer.level().dimension().location())) {
			String senderName = senderPlayer.getGameProfile().getName();
			String messageText = message.getString();
			List<Component> siblings = message.getSiblings();
			if (!siblings.isEmpty()) {
				String lastText = siblings.getLast().getString();
				if (!lastText.isEmpty())
					messageText = lastText;
			}

			boolean showUsername = ConfigCache.showUsername;
			if (!showUsername && message.getContents() instanceof TranslatableContents translatableContents) {
				final Object[] args = translatableContents.getArgs();
				if (args.length > 1) {
					final Object[] adjustedArgs = Arrays.copyOfRange(args, 1, args.length);
					StringBuilder justTheMessage = new StringBuilder();
					for (Object adjustedArg : adjustedArgs) {
						if (adjustedArg instanceof Component component) {
							justTheMessage.append(component.getString());
						}
						if (adjustedArg instanceof String str) {
							justTheMessage.append(str);
						}
					}
					messageText = justTheMessage.toString();
				}
			}
			if (!BubbleHandler.addPlayerBubble(sender, new BubbleText(senderName, messageText, sender, level.getGameTime()))) {
				NotableBubbleText.LOGGER.error("Failed to add player bubble for {}", senderName);
			}
		}
	}
}
