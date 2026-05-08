package stl.constru;

import stl.BiparameterizedFunction;

public class Constru07 {

	private static final double R = 20.0;
	
	public static final BiparameterizedFunction CONSTRU = new BiparameterizedFunction(
			"heptapode",
			(x, y) -> { double u = R * R - x * x - y * y; return (u >= 0.0) ? Math.sqrt(u) : 0.0; },
			(r, θ) -> { double u = Math.cos(7.0 * θ / 2.0); u *= u; return (R / 2) * (1.0 + u) * r; },
			(r, θ) -> θ,
			0.0, 40, 40);
}
