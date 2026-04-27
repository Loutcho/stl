package stl.function;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

import java.util.function.BiFunction;

public class F05 implements BiFunction<Double, Double, Double> {

	private static final double π = Math.PI;
	private static final double X = 8.0;
	private static final double Y = 4.0;
	private static final double ZCOEF = 8.0;
	
	@Override
	public Double apply(Double x, Double y) {
		double fx = (sin(π * cos((2 * π / 3) * (x / X))) + 1) / 2;
		double gy = (cos(π * (y / Y)) + 1) / 2;
		return ZCOEF * fx * gy;
	}
}
