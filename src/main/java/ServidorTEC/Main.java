package ServidorTEC;

import adt.Grafo;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Grafo G=new Grafo(30);
		G.display();
		//G.toXML();
		//G.fromXML();
		G.AsignarAleatorios();
		G.Floyd();
		G.toXML(0);
		G.toXML(1);
		G.toXML(2);
		G.display();
		G.ConsultarCamino(1, 4);
	}

}
