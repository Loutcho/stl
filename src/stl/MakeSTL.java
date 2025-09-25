/*
 * Ce programme crée un fichier STL 
 * (destiné à l'impression 3D) d'une surface représentative d'une fonction
 * f(x, y), avec ajout de plancher et de murs verticaux pour fermer la surface.
 *
 */
package stl;

import java.io.File;
import java.io.PrintStream;

public class MakeSTL {

	public static void main(String[] args) throws Exception {
		System.setOut(new PrintStream(new File("C:\\Users\\Luc\\Desktop\\fichier.stl")));
		new MakeSTL().make(stl.constru.Constru01.CONSTRU);
		// new MakeSTL().make(stl.constru.Constru02.CONSTRU);
	}
	
	public void make(RectFunction rectFunction) {
		new RectFunctionMaker().make(rectFunction);
	}
}
