package pruebas;

import java.util.LinkedList;

import adt.Grafo;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Grafo G=new Grafo(30);
		G.AdyacenciafromXML();
		G.display();
		//G.toXML();
		//G.fromXML();
		//G.AsignarAleatorios();
		LinkedList<Integer> Amigos=new LinkedList<Integer>();
		Amigos.add(4);
		Amigos.add(6);
		Amigos.add(7);
		int yo=2;
		int tec=5;
		G.ConsultarCamino(yo, tec);
		G.ConsultarCaminoAmigos(yo, tec, Amigos);
		/*G.Floyd();
		G.toXML(0);
		G.toXML(1);
		G.toXML(2);
		G.display();
		G.ConsultarCamino(1, 4);*/
	}

}
