package com.tracker.habittracker.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Frequency {
	DAILY("daily"),
	WEEKLY("weekly"),
	MONTHLY("monthly");

	private final String jsonValue;

	Frequency(String jsonValue) {
		this.jsonValue = jsonValue;
	}

	@JsonValue
	public String getJsonValue() {
		return jsonValue;
	}

	@JsonCreator(mode = JsonCreator.Mode.DELEGATING)
	public static Frequency fromString(String value) {
		if (value == null) {
			throw new IllegalArgumentException("Frequency cannot be null");
		}
		String normalized = value.trim().toLowerCase();
		switch (normalized) {
			case "daily":
				return DAILY;
			case "weekly":
				return WEEKLY;
			case "monthly":
				return MONTHLY;
			default:
				throw new IllegalArgumentException("Unknown frequency: " + value);
		}
	}
}


