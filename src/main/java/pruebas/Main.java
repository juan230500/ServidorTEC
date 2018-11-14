package pruebas;

import java.util.LinkedList;

import adt.Grafo;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Grafo G=new Grafo(30);
		G.fromXML();
		G.display();
		//G.toXML();
		//G.fromXML();
		//G.AsignarAleatorios();
		int[] L= {1,11,23,3,2};
		G.ConsultarOrdenAmigos(L);
		G.ConsultarCamino(3, 11);
		/*G.Floyd();
		G.toXML(0);
		G.toXML(1);
		G.toXML(2);
		G.display();
		G.ConsultarCamino(1, 4);*/
	}

}
