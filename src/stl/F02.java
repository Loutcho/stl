package stl;

import java.util.function.BiFunction;

public class F02 implements BiFunction<Double, Double, Double> {

	@Override
	public Double apply(Double x, Double y) {
		double rr = x * x + y * y;
		rr -= 1.0;
		rr *= rr;
		return 1.0 / (1.0 + rr);
	}
}
