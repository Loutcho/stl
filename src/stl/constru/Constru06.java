package stl.constru;

import stl.BiparameterizedFunction;

public class Constru06 {

	private static final double R = 20.0; // radius in millimeters
	
	public static final BiparameterizedFunction CONSTRU = new BiparameterizedFunction(
			"hemisphere",
			(x, y) -> { double u = R * R - x * x - y * y; return (u >= 0.0) ? Math.sqrt(u) : 0.0; },
			(r) -> R * r,
			(θ) -> θ,
			0.0, 40, 40);
}
