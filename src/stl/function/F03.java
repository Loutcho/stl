package stl.function;

import java.util.function.BiFunction;

public class F03 implements BiFunction<Double, Double, Double> {

	@Override
	public Double apply(Double x, Double y) {
		return x * x - y * y;
	}
}
