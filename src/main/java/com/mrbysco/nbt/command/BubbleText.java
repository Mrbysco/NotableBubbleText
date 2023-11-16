package com.mrbysco.nbt.command;

import java.util.UUID;

public record BubbleText(String author, String text, UUID uuid, long timeCreated) {

	public long getAge(long currentTime) {
		return currentTime - timeCreated;
	}

	public float getAlpha(long currentTime) {
		long age = getAge(currentTime);
		if (age < 5) {
			return age / 5F;
		} else if (age > 295) {
			return 1F - ((age - 295) / 5F);
		}
		return 1F;
	}
}
