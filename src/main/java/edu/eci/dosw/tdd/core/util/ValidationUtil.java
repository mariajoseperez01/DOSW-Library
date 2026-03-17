package edu.eci.dosw.tdd.core.util;

public final class ValidationUtil {

	private ValidationUtil() {
	}

	public static void requireNotNull(Object value, String message) {
		if (value == null) {
			throw new IllegalArgumentException(message);
		}
	}

	public static void requireNonBlank(String value, String message) {
		if (value == null || value.isBlank()) {
			throw new IllegalArgumentException(message);
		}
	}

	public static void requireTrue(boolean condition, String message) {
		if (!condition) {
			throw new IllegalArgumentException(message);
		}
	}
}
