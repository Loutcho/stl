package stl;

public class BiparameterizedFunctionMaker {

	private BiparameterizedFunction bpf;
	
	public BiparameterizedFunctionMaker(BiparameterizedFunction bpf) {
		this.bpf = bpf;
	}

	public void make() {
		System.out.printf("solid \"%s\"\n", bpf.name);
		System.err.printf("# DEBUG: plancher\n");
		plancher();
		System.err.printf("# DEBUG: surface\n");
		surface();
		System.err.printf("# DEBUG: mur\n");
		mur();
		System.out.printf("endsolid \"%s\"\n", bpf.name);
	}

	private void mur() {
		int nsubθ = bpf.nθ * bpf.nr;
		for (int isubθa = 0; isubθa < nsubθ; isubθa ++) {
			System.err.printf("# DEBUG: isubθa = %d\n", isubθa);
			/*
			 * D---C <-- surface
			 * |  /|
			 * | / |
			 * |/  |
			 * A---B <-- floor
			 */
			int isubθb = (isubθa + 1) % nsubθ;
			double θa = (2.0 * Math.PI) / nsubθ * isubθa;
			double θb = (2.0 * Math.PI) / nsubθ * isubθb;
			Sommet a = mkSommet(true, 1.0, θa);
			Sommet b = mkSommet(true, 1.0, θb);
			Sommet c = mkSommet(false, 1.0, θb);
			Sommet d = mkSommet(false, 1.0, θa);
			StlUtils.triangle(a, b, c);
			StlUtils.triangle(c, d, a);
		}
	}
	
	private void plancher() {
		plancher_ou_surface(true);
	}
	
	private void surface() {
		plancher_ou_surface(false);
	}

	private void plancher_ou_surface(boolean floor) {
		for (int ir = 0; ir < bpf.nr; ir ++) {
			System.err.printf("# DEBUG: ir = %d\n", ir);
			couronne(floor, ir);
		}
	}
	
	private void couronne(boolean floor, int ir) {
		for (int iθ = 0; iθ < bpf.nθ; iθ ++) {
			rangee_interne(floor, ir, iθ);
			rangee_externe(floor, ir, iθ);
		}
	}
	
	private void rangee_interne(boolean floor, int ir, int iθ) { // rangée verte sur le schéma
		int nΔ = ir;
		for (int iΔ = 0; iΔ < nΔ; iΔ ++) {
			triangle_interne(floor, ir, iθ, iΔ);
		}
	}
	
	private void rangee_externe(boolean floor, int ir, int iθ) { // rangée jaune sur le schéma
		int nΔ = ir + 1;
		for (int iΔ = 0; iΔ < nΔ; iΔ ++ ) {
			triangle_externe(floor, ir, iθ, iΔ);
		}
	}
	
	private Sommet mkSommet(boolean floor, double r, double θ) {
		double rr = bpf.R.apply(r, θ);
		double gθ = bpf.G.apply(r, θ);
		double x = rr * Math.cos(gθ);
		double y = rr * Math.sin(gθ);
		double z = floor ? bpf.zMin : bpf.function.apply(x, y);
		return new Sommet(x, y, z);
	}
	
	private void triangle_interne(boolean floor, int ir, int iθ, int iΔ) { // triangle vert sur le schéma
		// 2 points sur la ligne "basse" numérotée ir
		// 1 point sur la ligne "haute" numérotée ir + 1
		// Normale vers le bas (z décroissant)
		
		int nsubθ_lo = bpf.nθ * ir; // nombre total de subdivisions sur la ligne basse
		int nsubθ_hi = bpf.nθ * (ir + 1); // nombre total de subdivisions sur la ligne haute
		
		double ra = (1.0 / bpf.nr) * ir;
		int isubθa = (iθ * ir + iΔ + 1) % nsubθ_lo;
		double θa = (2.0 * Math.PI) / nsubθ_lo * isubθa;
		Sommet a = mkSommet(floor, ra, θa);
		
		double rb = (1.0 / bpf.nr) * ir;
		int isubθb = (iθ * ir + iΔ + 0) % nsubθ_lo;
		double θb = (2.0 * Math.PI) / nsubθ_lo * isubθb;
		Sommet b = mkSommet(floor, rb, θb);
		
		double rc = (1.0 / bpf.nr) * (ir + 1);
		int isubθc = (iθ * (ir + 1) + iΔ + 1) % nsubθ_hi;
		double θc = (2.0 * Math.PI) / nsubθ_hi * isubθc;
		Sommet c = mkSommet(floor, rc, θc);
		
		if (floor) {
			StlUtils.triangle(c, b, a);
		} else {
			StlUtils.triangle(a, b, c);
		}
	}
	
	private void triangle_externe(boolean floor, int ir, int iθ, int iΔ) { // triangle jaune sur le schéma
		int nsubθ_lo = bpf.nθ * ir; // nombre total de subdivisions sur la ligne basse
		int nsubθ_hi = bpf.nθ * (ir + 1); // nombre total de subdivisions sur la ligne haute
		
		Sommet a;
		if (nsubθ_lo == 0) {
			a = mkSommet(floor, 0.0, 0.0);
		} else {
			double ra = (1.0 / bpf.nr) * ir;
			int isubθa = (iθ * ir + iΔ) % nsubθ_lo;
			double θa = (2.0 * Math.PI) / nsubθ_lo * isubθa;
			a = mkSommet(floor, ra, θa);
		}
		
		double rb = (1.0 / bpf.nr) * (ir + 1);
		int isubθb = (iθ * (ir + 1) + iΔ + 0) % nsubθ_hi;
		double θb = (2.0 * Math.PI) / nsubθ_hi * isubθb;
		Sommet b = mkSommet(floor, rb, θb);
		
		double rc = (1.0 / bpf.nr) * (ir + 1);
		int isubθc = (iθ * (ir + 1) + iΔ + 1) % nsubθ_hi;
		double θc = (2.0 * Math.PI) / nsubθ_hi * isubθc;
		Sommet c = mkSommet(floor, rc, θc);
		
		if (floor) {
			StlUtils.triangle(c, b, a);
		} else {
			StlUtils.triangle(a, b, c);
		}
	}
}
