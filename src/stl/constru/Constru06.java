package stl.constru;

import stl.BiparameterizedFunction;

public class Constru06 {

	public static final BiparameterizedFunction CONSTRU = new BiparameterizedFunction(
			"cylindre",
			(x, y) -> 1.0,
			(r) -> r,
			(θ) -> θ,
			0.0,
			2,
			6,
			10);
}
