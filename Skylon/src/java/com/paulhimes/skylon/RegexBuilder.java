package com.paulhimes.skylon;

public final class RegexBuilder {

	private RegexBuilder() {

	}

	public static String anything() {
		return "[\\s\\S]*";
	}

	public static String startsWith(String target) {
		return target + anything();
	}

	public static String endsWith(String target) {
		return anything() + target;
	}

	public static String contains(String target) {
		return anything() + target + anything();
	}
}
