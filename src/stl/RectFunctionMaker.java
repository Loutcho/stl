package stl;

import java.util.Locale;

public class RectFunctionMaker {

	private RectFunction rf;
	
	public void make(RectFunction rf) {
		this.rf = rf;
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
		return rf.xMin + ix * (rf.xMax - rf.xMin) / rf.nx;
	}
	
	// Calcule l'ordonnée y de la coupe frontale numérotée iy
	private double fy(int iy) {
		return rf.yMin + iy * (rf.yMax - rf.yMin) / rf.ny;
	}
	
	private double f(double x, double y) {
		return rf.function.apply(x, y);
	}

	// Calcule l'altitude z d'un sommet équi-réparti entre Z_MIN (fixé) et zMax (variable)
	// en fonction de la finesse de découpage en z (NZ, fixé) et d'un indice nz entre 0 et NZ.
	private double fz(int iz, double zMax) {
		return rf.zMin + iz * (zMax - rf.zMin) / rf.nz;
	}

	// Génère la description des faces triangulaires représentant le plancher
	private void plancher() {
		for (int ix = 0; ix < rf.nx; ix ++) {
			for (int iy = 0; iy < rf.ny; iy ++) {
				Sommet a = new Sommet();
				a.x = fx(ix);
				a.y = fy(iy);
				a.z = rf.zMin;
				Sommet b = new Sommet();
				b.x = fx(ix + 1);
				b.y = fx(iy + 1);
				b.z = rf.zMin;
				Sommet c = new Sommet();
				c.x = a.x;
				c.y = b.y;
				c.z = rf.zMin;
				Sommet d = new Sommet();
				d.x = b.x;
				d.y = a.y;
				d.z = rf.zMin;
				triangle(a, b, d);
				triangle(b, a, c);
			}
		}
	}
	
	// Génère la description des faces triangulaires de la surface z = f(x, y)
	private void surface() {
		for (int ix = 0; ix < rf.nx; ix ++) {
			for (int iy = 0; iy < rf.ny; iy ++) {
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
		for (int iy = rf.ny; iy > 0; iy --) {
			double x = rf.xMin;
			double y1 = fy(iy);
			double y2 = fy(iy - 1);
			double z1 = f(x, y1);
			double z2 = f(x, y2);
			
			for (int iz = 0; iz < rf.nz; iz ++) {
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
		for (int iy = 0; iy < rf.ny; iy ++) {
			double x = rf.xMax;
			double y1 = fy(iy);
			double y2 = fy(iy + 1);
			double z1 = f(x, y1);
			double z2 = f(x, y2);
			
			for (int iz = 0; iz < rf.nz; iz ++) {
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
		for (int ix = 0; ix < rf.nx; ix ++) {
			double x1 = fx(ix);
			double x2 = fx(ix + 1);
			double y = rf.yMin;
			double z1 = f(x1, y);
			double z2 = f(x2, y);
			
			for (int iz = 0; iz < rf.nz; iz ++) {
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
		for (int ix = rf.nx; ix > 0; ix --) {
			double x1 = fx(ix);
			double x2 = fx(ix - 1);
			double y = rf.yMax;
			double z1 = f(x1, y);
			double z2 = f(x2, y);
			
			for (int iz = 0; iz < rf.nz; iz ++) {
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
