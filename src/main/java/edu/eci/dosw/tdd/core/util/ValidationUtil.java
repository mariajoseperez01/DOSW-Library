package edu.eci.dosw.tdd.core.util;

public final class ValidationUtil {

	private ValidationUtil() {
	}

	public static void requireNonBlank(String value, String fieldName) {
		if (value == null || value.isBlank()) {
			throw new IllegalArgumentException(fieldName + " cannot be blank");
		}
	}

	public static void requireNonNegative(int value, String fieldName) {
		if (value < 0) {
			throw new IllegalArgumentException(fieldName + " cannot be negative");
		}
	}
}
