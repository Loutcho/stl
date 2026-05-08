package stl;

public class StlUtils {

	// Génère la description de la face triangulaire de sommets a, b, c fournis
	public static void triangle(Sommet a, Sommet b, Sommet c) {
		Sommet v = vecteur_normal(a, b, c);
		System.out.printf("  facet normal %f %f %f\n", v.x, v.y, v.z);
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
	
	private static Sommet vecteur_normal(Sommet a, Sommet b, Sommet c) {
		Sommet ab = new Sommet(b.x - a.x, b.y - a.y, b.z - a.z);
		Sommet ac = new Sommet(c.x - a.x, c.y - a.y, a.z - a.z);
		Sommet pv = new Sommet(
				ab.y * ac.z - ab.z * ac.y,
				ab.z * ac.x - ab.x * ac.z,
				ab.x * ac.y - ab.y * ac.x
		);
		double norme = Math.sqrt(pv.x * pv.x + pv.y * pv.y + pv.z * pv.z);
		return new Sommet(pv.x / norme, pv.y / norme, pv.z / norme);
	}
}
