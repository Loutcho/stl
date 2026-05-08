package stl;

public class BiparameterizedFunctionMaker {

	private BiparameterizedFunction bpf;
	
	public BiparameterizedFunctionMaker(BiparameterizedFunction bpf) {
		this.bpf = bpf;
	}

	public void make() {
		System.out.printf("solid \"%s\"\n", bpf.name);
		plancher();
		System.out.printf("endsolid \"%s\"\n", bpf.name);
	}
	
	// Génère la description des faces triangulaires représentant le plancher
	private void plancher() {
		for (int ir = 0; ir < bpf.nr; ir ++) {
			System.err.printf("# DEBUG: ir = %d\n", ir);
			couronne(ir);
		}
	}
	
	private void couronne(int ir) {
		for (int iθ = 0; iθ < bpf.nθ; iθ ++) {
			System.err.printf("# DEBUG: iθ = %d\n", iθ);
			System.err.printf("# DEBUG: interne\n", iθ);
			rangee_interne(ir, iθ);
			System.err.printf("# DEBUG: externe\n", iθ);
			rangee_externe(ir, iθ);
		}
	}
	
	private void rangee_interne(int ir, int iθ) { // rangée verte sur le schéma
		int nΔ = ir;
		for (int iΔ = 0; iΔ < nΔ; iΔ ++) {
			triangle_interne(ir, iθ, iΔ);
		}
	}
	
	private void rangee_externe(int ir, int iθ) { // rangée jaune sur le schéma
		int nΔ = ir + 1;
		for (int iΔ = 0; iΔ < nΔ; iΔ ++ ) {
			System.err.printf("# DEBUG: iΔ = %d\n", iΔ);
			triangle_externe(ir, iθ, iΔ);
		}
	}
	
	private Sommet mkSommet(double r, double θ) {
		double rr = bpf.R.apply(r);
		double gθ = bpf.G.apply(θ);
		double x = rr * Math.cos(gθ);
		double y = rr * Math.sin(gθ);
		double z = bpf.zMin;
		return new Sommet(x, y, z);
	}
	
	private void triangle_interne(int ir, int iθ, int iΔ) { // triangle vert sur le schéma
		// 2 points sur la ligne "basse" numérotée ir
		// 1 point sur la ligne "haute" numérotée ir + 1
		// Normale vers le bas (z décroissant)
		
		int nsubθ_lo = bpf.nθ * ir; // nombre total de subdivisions sur la ligne basse
		int nsubθ_hi = bpf.nθ * (ir + 1); // nombre total de subdivisions sur la ligne haute
		
		double ra = (1.0 / bpf.nr) * ir;
		int isubθa = (iθ * ir + iΔ + 1) % nsubθ_lo;
		double θa = (2.0 * Math.PI) / nsubθ_lo * isubθa;
		Sommet a = mkSommet(ra, θa);
		
		double rb = (1.0 / bpf.nr) * ir;
		int isubθb = (iθ * ir + iΔ + 0) % nsubθ_lo;
		double θb = (2.0 * Math.PI) / nsubθ_lo * isubθb;
		Sommet b = mkSommet(rb, θb);
		
		double rc = (1.0 / bpf.nr) * (ir + 1);
		int isubθc = (iθ * (ir + 1) + iΔ + 1) % nsubθ_hi;
		double θc = (2.0 * Math.PI) / nsubθ_hi * isubθc;
		Sommet c = mkSommet(rc, θc);
		
		StlUtils.triangle(c, b, a);
	}
	
	private void triangle_externe(int ir, int iθ, int iΔ) { // triangle jaune sur le schéma
		int nsubθ_lo = bpf.nθ * ir; // nombre total de subdivisions sur la ligne basse
		int nsubθ_hi = bpf.nθ * (ir + 1); // nombre total de subdivisions sur la ligne haute
		
		Sommet a;
		if (nsubθ_lo == 0) {
			a = mkSommet(0.0, 0.0);
		} else {
			double ra = (1.0 / bpf.nr) * ir;
			int isubθa = (iθ * ir + iΔ) % nsubθ_lo;
			double θa = (2.0 * Math.PI) / nsubθ_lo * isubθa;
			a = mkSommet(ra, θa);
		}
		
		double rb = (1.0 / bpf.nr) * (ir + 1);
		int isubθb = (iθ * (ir + 1) + iΔ + 0) % nsubθ_hi;
		double θb = (2.0 * Math.PI) / nsubθ_hi * isubθb;
		Sommet b = mkSommet(rb, θb);
		
		double rc = (1.0 / bpf.nr) * (ir + 1);
		int isubθc = (iθ * (ir + 1) + iΔ + 1) % nsubθ_hi;
		double θc = (2.0 * Math.PI) / nsubθ_hi * isubθc;
		Sommet c = mkSommet(rc, θc);
		
		StlUtils.triangle(c, b, a);
	}
}
