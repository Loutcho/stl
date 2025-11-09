package stl.function;

import java.util.function.BiFunction;

public class F04 implements BiFunction<Double, Double, Double> {

	@Override
	public Double apply(Double x, Double y) {
		double rr = Math.sqrt(x * x + y * y);
		double aa = Math.abs(rr - 2.0);
		double bb = 0.25 - aa * aa;
		if (bb < 0.0) { return 0.0; } else { return Math.sqrt(bb); }
	}
}
