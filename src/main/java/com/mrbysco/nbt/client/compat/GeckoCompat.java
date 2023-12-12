package com.mrbysco.nbt.client.compat;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mrbysco.nbt.client.BubbleHandler;
import com.mrbysco.nbt.client.ClientHandler;
import com.mrbysco.nbt.client.util.BubbleRenderer;
import com.mrbysco.nbt.command.BubbleText;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import software.bernie.geckolib.event.GeoRenderEvent;

import java.util.List;

public class GeckoCompat {

	@SubscribeEvent
	public void onEntityRender(GeoRenderEvent.Entity.Post event) {
		final Minecraft mc = Minecraft.getInstance();
		final Player localPlayer = mc.player;
		if (localPlayer == null) return;

		if (event.getEntity() instanceof LivingEntity livingEntity) {
			if (livingEntity.isInvisibleTo(localPlayer)) return;

			String author = BubbleHandler.getAuthor(livingEntity.getUUID());
			if (!author.isEmpty()) {
				List<BubbleText> bubbles = BubbleHandler.getBubbles(author);
				if (bubbles.isEmpty()) return;
				BubbleText bubble = bubbles.get(0);

				final Level level = mc.level;
				if (level == null) return;
				final long currentTime = level.getGameTime();

				long bubbleTime = bubbles.size() > 1 ? 50 : 200;
				long bubbleAge = bubble.getAge(currentTime);
				float bubbleAlpha = bubble.getAlpha(currentTime);

				final Font font = mc.font;
				final PoseStack poseStack = event.getPoseStack();
				final EntityDimensions dimensions = livingEntity.getDimensions(livingEntity.getPose());
				final MultiBufferSource multiBufferSource = event.getBufferSource();
				final EntityRenderDispatcher renderDispatcher = mc.getEntityRenderDispatcher();
				final double nameOffset = ClientHandler.getNameOffset(renderDispatcher, livingEntity);

				BubbleRenderer.renderBubbleText(bubble, poseStack, font, multiBufferSource, renderDispatcher,
						dimensions.height, bubbleAlpha, event.getPackedLight(), nameOffset);

				if (bubbleAge > bubbleTime) {
					BubbleHandler.removeBubble(bubble);
				}
			}
		}
	}
}
