package dev.nolij.libnolij.util;

import org.jetbrains.annotations.Contract;

public final class MathUtil {
	
	private MathUtil() {}
	
	@Contract(pure = true)
	public static int sign(final int input) {
		return input >> (Integer.SIZE - 1) | 1;
	}
	
	@Contract(pure = true)
	public static long sign(final long input) {
		return input >> (Long.SIZE - 1) | 1;
	}
	
	@Contract(pure = true)
	public static float sign(final float input) {
		return Math.signum(input);
	}
	
	@Contract(pure = true)
	public static double sign(final double input) {
		return Math.signum(input);
	}
	
	@Contract(pure = true)
	public static double clamp(final double value, final double min, final double max) {
		return Math.max(Math.min(value, max), min);
	}
	
}
