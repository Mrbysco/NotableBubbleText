package com.mrbysco.nbt.client;

import com.mrbysco.nbt.command.BubbleText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * This class handles the tracking of the bubbles above the mobs head and how long they should be displayed
 */
public class BubbleHandler {
	/* Map of all the bubbles that are currently active */
	private static final Map<String, List<BubbleText>> bubbleMap = new HashMap<>();

	/* Map of all the bubbles that are currently active for players */
	private static final Map<UUID, List<BubbleText>> playerBubbleMap = new HashMap<>();

	/**
	 * Add a bubble to the map
	 *
	 * @param author     The author of the bubble
	 * @param bubbleText The bubble to add
	 * @return false if the bubble is null or already exists
	 */
	public static boolean addBubble(String author, BubbleText bubbleText) {
		if (bubbleText == null) return false;
		List<BubbleText> bubbleList = bubbleMap.getOrDefault(author, new ArrayList<>());
		if (bubbleList.contains(bubbleText)) return false;
		updateCache(bubbleText.uuid(), author);
		bubbleList.add(bubbleText);
		bubbleMap.put(author, bubbleList);
		return true;
	}

	/**
	 * Remove a bubble to the map
	 *
	 * @param bubbleText The bubble to add
	 * @return false if the bubble is null or doesn't exist
	 */
	public static boolean removeBubble(BubbleText bubbleText) {
		if (bubbleText == null) return false;
		String author = bubbleText.author();
		List<BubbleText> bubbleList = bubbleMap.getOrDefault(author, new ArrayList<>());
		if (!bubbleList.contains(bubbleText)) return false;
		bubbleList.remove(bubbleText);
		if (bubbleMap.isEmpty()) {
			bubbleMap.remove(author);
		} else {
			bubbleMap.put(author, bubbleList);
		}
		return true;
	}

	/**
	 * Get all the bubbles for a specific author
	 *
	 * @param author The author to get the bubbles from
	 * @return A list of bubbles
	 */
	public static List<BubbleText> getBubbles(String author) {
		return bubbleMap.getOrDefault(author, new ArrayList<>());
	}


	/**
	 * Add a bubble to the map
	 *
	 * @param uuid       The uuid of the player
	 * @param bubbleText The bubble to add
	 * @return false if the bubble is null or already exists
	 */
	public static boolean addPlayerBubble(UUID uuid, BubbleText bubbleText) {
		if (bubbleText == null) return false;
		List<BubbleText> bubbleList = playerBubbleMap.getOrDefault(uuid, new ArrayList<>());
		if (bubbleList.contains(bubbleText)) return false;
		bubbleList.add(bubbleText);
		playerBubbleMap.put(uuid, bubbleList);
		return true;
	}

	/**
	 * Remove a bubble to the map
	 *
	 * @param bubbleText The bubble to add
	 * @return false if the bubble is null or doesn't exist
	 */
	public static boolean removePlayerBubble(BubbleText bubbleText) {
		if (bubbleText == null) return false;
		UUID author = bubbleText.uuid();
		List<BubbleText> bubbleList = playerBubbleMap.getOrDefault(author, new ArrayList<>());
		if (!bubbleList.contains(bubbleText)) return false;
		bubbleList.remove(bubbleText);
		if (bubbleList.isEmpty()) {
			playerBubbleMap.remove(author);
		} else {
			playerBubbleMap.put(author, bubbleList);
		}
		return true;
	}

	/**
	 * Get all the bubbles for a specific author
	 *
	 * @param author The author to get the bubbles from
	 * @return A list of bubbles
	 */
	public static List<BubbleText> getPlayerBubbles(UUID author) {
		return playerBubbleMap.getOrDefault(author, new ArrayList<>());
	}

	public static final Map<UUID, String> uuidLookupCache = new HashMap<>();

	/**
	 * Get the UUID that matches the author
	 *
	 * @param uuid The UUID to get the author from
	 * @return The author that matches the UUID or an empty string if it doesn't exist
	 */
	public static String getAuthor(UUID uuid) {
		return uuidLookupCache.computeIfAbsent(uuid, (value) -> {
			for (Map.Entry<String, List<BubbleText>> entry : bubbleMap.entrySet()) {
				for (BubbleText bubble : entry.getValue()) {
					if (bubble.uuid() == value) {
						return entry.getKey();
					}
				}
			}
			return "";
		});
	}

	/**
	 * Update the cache with a new author
	 *
	 * @param uuid   The UUID to update
	 * @param author The author to update
	 */
	public static void updateCache(UUID uuid, String author) {
		uuidLookupCache.put(uuid, author);
	}
}
