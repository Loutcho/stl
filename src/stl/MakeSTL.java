/*
 * Ce programme écrit sur la sortie standard la description au format STL 
 * (destiné à l'impression 3D) d'une surface représentative d'une fonction
 * f(x, y), avec ajout de plancher et de murs verticaux pour fermer la surface.
 *
 */
package stl;

import java.io.File;
import java.io.PrintStream;
import java.util.Locale;
import java.util.function.BiFunction;

public class MakeSTL {

	public static void main(String[] args) throws Exception {
		System.setOut(new PrintStream(new File("C:\\Users\\Luc\\Desktop\\fichier.stl")));
		
		new MakeSTL(new F01(), -3.0, +3.0, -3.0, +3.0, -1.5, 100, 100, 10).run();
		// new MakeSTL(new F02(), -3.0, +3.0, -3.0, +3.0, -0.50, 300, 300, 100).run();
	}
	
	private BiFunction<Double, Double, Double> function;
	private Double xMin, xMax, yMin, yMax, zMin;
	private Integer nx, ny, nz;
	
	public MakeSTL(
			BiFunction<Double, Double, Double> function,
			Double xMin,
			Double xMax,
			Double yMin,
			Double yMax,
			Double zMin,
			Integer nx,
			Integer ny,
			Integer nz
	) {
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
	
	public void run() {
		Locale.setDefault(new Locale("en", "US")); // Appel servant à ce que le séparateur décimal soit "." dans les nombres flottants 
		
		String name = "toto";
		System.out.printf("solid \"%s\"\n", name);
		plancher();
		murXmin();
		murXmax();
		murYmin();
		murYmax();
		surface();
		System.out.printf("endsolid \"%s\"\n", name);
	}
	
	// Calcule l'abscisse x de la coupe sagittale numérotée ix
	private double fx(int ix) {
		return xMin + ix * (xMax - xMin) / nx;
	}
	
	// Calcule l'ordonnée y de la coupe frontale numérotée iy
	private double fy(int iy) {
		return yMin + iy * (yMax - yMin) / ny;
	}
	
	private double f(double x, double y) {
		return function.apply(x, y);
	}

	// Calcule l'altitude z d'un sommet équi-réparti entre Z_MIN (fixé) et zMax (variable)
	// en fonction de la finesse de découpage en z (NZ, fixé) et d'un indice nz entre 0 et NZ.
	private double fz(int iz, double zMax) {
		return zMin + iz * (zMax - zMin) / nz;
	}

	// Génère la description des faces triangulaires représentant le plancher
	private void plancher() {
		for (int ix = 0; ix < nx; ix ++) {
			for (int iy = 0; iy < ny; iy ++) {
				Sommet a = new Sommet();
				a.x = fx(ix);
				a.y = fy(iy);
				a.z = zMin;
				Sommet b = new Sommet();
				b.x = fx(ix + 1);
				b.y = fx(iy + 1);
				b.z = zMin;
				Sommet c = new Sommet();
				c.x = a.x;
				c.y = b.y;
				c.z = zMin;
				Sommet d = new Sommet();
				d.x = b.x;
				d.y = a.y;
				d.z = zMin;
				triangle(a, b, d);
				triangle(b, a, c);
			}
		}
	}
	
	// Génère la description des faces triangulaires de la surface z = f(x, y)
	private void surface() {
		for (int ix = 0; ix < nx; ix ++) {
			for (int iy = 0; iy < ny; iy ++) {
				double ax = fx(ix);
				double ay = fy(iy);
				double bx = fx(ix + 1);
				double by = fx(iy + 1); // TODO: paraît louche ; ça serait-y pas plutôt fy(iy + 1) ?
				Sommet a = new Sommet(ax, ay, f(ax, ay));
				Sommet b = new Sommet(bx, by, f(bx, by));
				Sommet c = new Sommet(ax, by, f(ax, by));
				Sommet d = new Sommet(bx, ay, f(bx, ay));
				triangle(a, b, c);
				triangle(b, a, d);
			}
		}
	}
	
	// Génère la description des faces triangulaires du mur en x = X_MIN
	private void murXmin() {
		for (int iy = ny; iy > 0; iy --) {
			double x = xMin;
			double y1 = fy(iy);
			double y2 = fy(iy - 1);
			double z1 = f(x, y1);
			double z2 = f(x, y2);
			
			for (int iz = 0; iz < nz; iz ++) {
				Sommet a = new Sommet(x, y1, fz(iz, z1));
				Sommet b = new Sommet(x, y2, fz(iz + 1, z2));
				Sommet c = new Sommet(x, y1, fz(iz + 1, z1));
				Sommet d = new Sommet(x, y2, fz(iz, z2));
				triangle(a, b, c);
				triangle(b, a, d);
			}
		}
	}
	
	// Génère la description des faces triangulaires du mur en x = X_MAX
	private void murXmax() {
		for (int iy = 0; iy < ny; iy ++) {
			double x = xMax;
			double y1 = fy(iy);
			double y2 = fy(iy + 1);
			double z1 = f(x, y1);
			double z2 = f(x, y2);
			
			for (int iz = 0; iz < nz; iz ++) {
				Sommet a = new Sommet(x, y1, fz(iz, z1));
				Sommet b = new Sommet(x, y2, fz(iz + 1, z2));
				Sommet c = new Sommet(x, y1, fz(iz + 1, z1));
				Sommet d = new Sommet(x, y2, fz(iz, z2));
				triangle(a, b, c);
				triangle(b, a, d);
			}
		}
	}

	// Génère la description des faces triangulaires du mur en y = Y_MIN
	private void murYmin() {
		for (int ix = 0; ix < nx; ix ++) {
			double x1 = fx(ix);
			double x2 = fx(ix + 1);
			double y = yMin;
			double z1 = f(x1, y);
			double z2 = f(x2, y);
			
			for (int iz = 0; iz < nz; iz ++) {
				Sommet a = new Sommet(x1, y, fz(iz, z1));
				Sommet b = new Sommet(x2, y, fz(iz + 1, z2));
				Sommet c = new Sommet(x1, y, fz(iz + 1, z1));
				Sommet d = new Sommet(x2, y, fz(iz, z2));
				triangle(a, b, c);
				triangle(b, a, d);
			}
		}
	}
	
	// Génère la description des faces triangulaires du mur en y = Y_MAX
	private void murYmax() {
		for (int ix = nx; ix > 0; ix --) {
			double x1 = fx(ix);
			double x2 = fx(ix - 1);
			double y = yMax;
			double z1 = f(x1, y);
			double z2 = f(x2, y);
			
			for (int iz = 0; iz < nz; iz ++) {
				Sommet a = new Sommet(x1, y, fz(iz, z1));
				Sommet b = new Sommet(x2, y, fz(iz + 1, z2));
				Sommet c = new Sommet(x1, y, fz(iz + 1, z1));
				Sommet d = new Sommet(x2, y, fz(iz, z2));
				triangle(a, b, c);
				triangle(b, a, d);
			}
		}
	}
	
	// Génère la description de la face triangulaire de sommets a, b, c fournis
	private void triangle(Sommet a, Sommet b, Sommet c) {
		double x = 0.0, y = 0.0, z = 0.0; // Peut être amélioré en calculant la normale, mais l'outil que j'utilise pour imprimer n'en a pas besoin
		System.out.printf("  facet normal %f %f %f\n", x, y, z);
		System.out.printf("    outer loop\n");
		sommet(a);
		sommet(b);
		sommet(c);
		System.out.printf("    endloop\n");
		System.out.printf("  endfacet\n");
	}
	
	// Génère la description d'un sommet
	private void sommet(Sommet s) {
		System.out.printf("      vertex %f %f %f\n", s.x, s.y, s.z);
	}
}
