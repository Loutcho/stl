package stl;

import java.util.function.BiFunction;

public class F01 implements BiFunction<Double, Double, Double> {

	@Override
	public Double apply(Double x, Double y) {
		double xx = x * x;
		double yy = y * y;
		double rr = xx + yy;
		double ff = (rr <= 1E-10) ? 1.0 : (xx - yy) / rr;
		return ff;
	}
}
