/*
 * Ce programme écrit sur la sortie standard la description au format STL 
 * (destiné à l'impression 3D) d'une surface représentative d'une fonction
 * f(x, y), avec ajout de plancher et de murs verticaux pour fermer la surface.
 *
 * Fonction ici prise pour exemple :
 * f(x, y) = (x^2 - y^2) / (x^2 + y^2)
 * ... intéressante pour visualiser la singularité en (0, 0) 
 */
package stl;

import java.util.Locale;

public class MakeSTL {

	private static final double X_MIN = -3.0;
	private static final double X_MAX = +3.0;
	private static final double Y_MIN = -3.0;
	private static final double Y_MAX = +3.0;
	private static final double Z_MIN = -1.5; // à choisir plus petit que le plus petit f(x, y)
	private static final int NX = 100; // nombre de tranches selon l'axe des x.
	private static final int NY = 100; // nombre de tranches selon l'axe des y.
	private static final int NZ = 10; // nombre de tranches selon l'axe des z.
	
	// Calcule l'abscisse x de la coupe sagittale numérotée ix
	private static double fx(int ix) {
		return X_MIN + ix * (X_MAX - X_MIN) / NX;
	}
	
	// Calcule l'ordonnée y de la coupe frontale numérotée iy
	private static double fy(int iy) {
		return Y_MIN + iy * (Y_MAX - Y_MIN) / NY;
	}
	
	// La fonction f, ici f(x, y) = (x^2 - y^2) / (x^2 + y^2), avec f(0, 0) = 1 arbitraire.
	private static double f(double x, double y) {
		double xx = x * x;
		double yy = y * y;
		double rr = xx + yy;
		double ff = (rr <= 1E-10) ? 1.0 : (xx - yy) / rr;
		return ff; 
	}

	// Calcule l'altitude z d'un sommet équi-réparti entre Z_MIN (fixé) et zMax (variable)
	// en fonction de la finesse de découpage en z (NZ, fixé) et d'un indice nz entre 0 et NZ.
	private static double fz(int iz, double zMax) {
		return Z_MIN + iz * (zMax - Z_MIN) / NZ;
	}

	// Point d'entrée du programme
	public static void main(String[] args) {
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
	
	// Génère la description des faces triangulaires représentant le plancher
	private static void plancher() {
		for (int ix = 0; ix < NX; ix ++) {
			for (int iy = 0; iy < NY; iy ++) {
				Sommet a = new Sommet();
				a.x = fx(ix);
				a.y = fy(iy);
				a.z = Z_MIN;
				Sommet b = new Sommet();
				b.x = fx(ix + 1);
				b.y = fx(iy + 1);
				b.z = Z_MIN;
				Sommet c = new Sommet();
				c.x = a.x;
				c.y = b.y;
				c.z = Z_MIN;
				Sommet d = new Sommet();
				d.x = b.x;
				d.y = a.y;
				d.z = Z_MIN;
				triangle(a, b, d);
				triangle(b, a, c);
			}
		}
	}
	
	// Génère la description des faces triangulaires de la surface z = f(x, y)
	private static void surface() {
		for (int ix = 0; ix < NX; ix ++) {
			for (int iy = 0; iy < NY; iy ++) {
				Sommet a = new Sommet();
				a.x = fx(ix);
				a.y = fy(iy);
				a.z = f(a.x, a.y);
				Sommet b = new Sommet();
				b.x = fx(ix + 1);
				b.y = fx(iy + 1);
				b.z = f(b.x, b.y);
				Sommet c = new Sommet();
				c.x = a.x;
				c.y = b.y;
				c.z = f(c.x, c.y);
				Sommet d = new Sommet();
				d.x = b.x;
				d.y = a.y;
				d.z = f(d.x, d.y);
				triangle(a, b, c);
				triangle(b, a, d);
			}
		}
	}
	
	// Génère la description des faces triangulaires du mur en x = X_MIN
	private static void murXmin() {
		for (int iy = NY; iy > 0; iy --) {
			double x = X_MIN;
			double y1 = fy(iy);
			double y2 = fy(iy - 1);
			double z1 = f(x, y1);
			double z2 = f(x, y2);
			
			for (int iz = 0; iz < NZ; iz ++) {
				Sommet a = new Sommet();
				a.x = x;
				a.y = y1;
				a.z = fz(iz, z1); 
				Sommet b = new Sommet();
				b.x = x;
				b.y = y2;
				b.z = fz(iz + 1, z2);
				Sommet c = new Sommet();
				c.x = x;
				c.y = y1;
				c.z = fz(iz + 1, z1);
				Sommet d = new Sommet();
				d.x = x;
				d.y = y2;
				d.z = fz(iz, z2);
				triangle(a, b, c);
				triangle(b, a, d);
			}
		}
	}
	
	// Génère la description des faces triangulaires du mur en x = X_MAX
	private static void murXmax() {
		for (int iy = 0; iy < NY; iy ++) {
			double x = X_MAX;
			double y1 = fy(iy);
			double y2 = fy(iy + 1);
			double z1 = f(x, y1);
			double z2 = f(x, y2);
			
			for (int iz = 0; iz < NZ; iz ++) {
				Sommet a = new Sommet();
				a.x = x;
				a.y = y1;
				a.z = fz(iz, z1); 
				Sommet b = new Sommet();
				b.x = x;
				b.y = y2;
				b.z = fz(iz + 1, z2);
				Sommet c = new Sommet();
				c.x = x;
				c.y = y1;
				c.z = fz(iz + 1, z1);
				Sommet d = new Sommet();
				d.x = x;
				d.y = y2;
				d.z = fz(iz, z2);
				triangle(a, b, c);
				triangle(b, a, d);
			}
		}
	}

	// Génère la description des faces triangulaires du mur en y = Y_MIN
	private static void murYmin() {
		for (int ix = 0; ix < NX; ix ++) {
			double x1 = fx(ix);
			double x2 = fx(ix + 1);
			double y = Y_MIN;
			double z1 = f(x1, y);
			double z2 = f(x2, y);
			
			for (int iz = 0; iz < NZ; iz ++) {
				Sommet a = new Sommet();
				a.x = x1;
				a.y = y;
				a.z = fz(iz, z1); 
				Sommet b = new Sommet();
				b.x = x2;
				b.y = y;
				b.z = fz(iz + 1, z2);
				Sommet c = new Sommet();
				c.x = x1;
				c.y = y;
				c.z = fz(iz + 1, z1);
				Sommet d = new Sommet();
				d.x = x2;
				d.y = y;
				d.z = fz(iz, z2);
				triangle(a, b, c);
				triangle(b, a, d);
			}
		}
	}
	
	// Génère la description des faces triangulaires du mur en y = Y_MAX
	private static void murYmax() {
		for (int ix = NX; ix > 0; ix --) {
			double x1 = fx(ix);
			double x2 = fx(ix - 1);
			double y = Y_MAX;
			double z1 = f(x1, y);
			double z2 = f(x2, y);
			
			for (int iz = 0; iz < NZ; iz ++) {
				Sommet a = new Sommet();
				a.x = x1;
				a.y = y;
				a.z = fz(iz, z1); 
				Sommet b = new Sommet();
				b.x = x2;
				b.y = y;
				b.z = fz(iz + 1, z2);
				Sommet c = new Sommet();
				c.x = x1;
				c.y = y;
				c.z = fz(iz + 1, z1);
				Sommet d = new Sommet();
				d.x = x2;
				d.y = y;
				d.z = fz(iz, z2);
				triangle(a, b, c);
				triangle(b, a, d);
			}
		}
	}
	
	// Génère la description de la face triangulaire de sommets a, b, c fournis
	private static void triangle(Sommet a, Sommet b, Sommet c) {
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
	private static void sommet(Sommet s) {
		System.out.printf("      vertex %f %f %f\n", s.x, s.y, s.z);
	}
}
