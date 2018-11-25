
package pruebas;

import lógica.Grafo;

public class NuevoMapa {

	public static void main(String[] args) {
		Grafo G=new Grafo(31);
		G.AsignarAleatorios();
		G.Floyd();
		G.toXML(0);
		G.toXML(1);
		G.toXML(2);
	}

}
