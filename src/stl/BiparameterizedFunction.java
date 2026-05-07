package stl;

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * A BiparameterizedFunction = the information needed to build a solid (almost a right-prism). More precisely, the solid has:
 * - a directrix = the closed 2D curve of the projection of that solid on the (x, y) plane
 * - a flat, horizontal floor
 * - generatrices = vertical walls along the directrix   
 * - a function f(x, y) that closes the solid (the ceiling) (not necessary flat)
 * - two functions that parametrize the interior of the directrix:
 *     - the R(r) function:
 *         r between 0 and 1
 *         R(r) real
 *         R continuous 
 *         R(0) = 0
 *     - the G(θ) function:
 *         θ between 0 and 2π
 *         G(θ) real 
 *         G continuous
 * - Let Φ(r, θ) be the (x, y) such that x = R(r)cos(G(θ)) and y = R(r)sin(G(θ))
 *   then Φ should be injective except at r = 0.
 * The idea is to easily get the vertices of the triangles that will constitute the floor and the ceiling,
 * by subdividing the intervals of r and θ evenly, while coercing (x, y) to stay in the interior of the directrix.  
 * See r as a "proto-radius" and θ as a "proto-angle".
 * See R and G as a way to bend the radial lines stemming out of the origin, if necessary (if the directrix is not convex).
 * The directrix should be given by Φ(1, [0, 2π]).
 */
public class BiparameterizedFunction {

	public BiFunction<Double, Double, Double> function;
	public Function<Double, Double> R;
	public Function<Double, Double> G;
	public Double zMin; // zMin must be provided smaller than the inf of all the f(x, y) on the domain delimited by the directrix.
	public Integer nr; // number of subdivisions according to r: r will take successively the (nr + 1) values (ir / nr), ir from 0 to nr.
	public Integer nθ; // number of subdivisions according to θ: θ will take successively the (nθ + 1) values (iθ / nθ) * 2π, iθ from 0 to nθ. 
	public Integer nz; // number of subdivisions according to z: z will take successively the (nz + 1) values from zMin to the relevant f(x, y).

	public BiparameterizedFunction(
			BiFunction<Double, Double, Double> function,
			Function<Double, Double> R,
			Function<Double, Double> G,
			Double zMin,
			Integer nr, Integer nθ, Integer nz) {
		super();
		this.function = function;
		this.R = R;
		this.G = G;
		this.zMin = zMin;
		this.nr = nr;
		this.nθ = nθ;
		this.nz = nz;
	}
}
