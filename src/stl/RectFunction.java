package stl;

import java.util.function.BiFunction;

/**
 * A RectFunction = a function f(x, y), five bounds and three numbers driving precision
 */
public class RectFunction {

	public BiFunction<Double, Double, Double> function;
	public Double xMin;
	public Double xMax;
	public Double yMin;
	public Double yMax;
	public Double zMin; // zMin must be provided smaller than the inf of all the f(x, y) on the [xMin, xMax] X [yMin, yMax] domain.
	public Integer nx;
	public Integer ny;
	public Integer nz;

	public RectFunction(
			BiFunction<Double, Double, Double> function,
			Double xMin, Double xMax,
			Double yMin, Double yMax,
			Double zMin,
			Integer nx, Integer ny, Integer nz) {
		super();
		this.function = function;
		this.xMin = xMin;
		this.xMax = xMax;
		this.yMin = yMin;
		this.yMax = yMax;
		this.zMin = zMin;
		this.nx = nx;
		this.ny = ny;
		this.nz = nz;
	}
}
