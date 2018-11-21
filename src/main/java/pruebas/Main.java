package pruebas;

import java.util.LinkedList;

import lógica.Grafo;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Grafo G=new Grafo(31);
		G.AdyacenciafromXML();
		G.display();
		//G.toXML();
		//G.fromXML();
		//G.AsignarAleatorios();
		LinkedList<Integer> Amigos=new LinkedList<Integer>();
		Amigos.add(4);
		Amigos.add(29);
		Amigos.add(7);
		int yo=2;
		int tec=5;
		G.ConsultarCamino(1, 29);
		G.ConsultarCaminoAmigos(yo, tec, Amigos);
		
		Cliente C=new Cliente();
		C.CaminoFromJson(G.CaminoToJson());
		/*G.Floyd();
		G.toXML(0);
		G.toXML(1);
		G.toXML(2);
		G.display();
		G.ConsultarCamino(1, 4);*/
	}

}
