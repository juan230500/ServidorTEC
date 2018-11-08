package ServidorTEC;

import adt.Grafo;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Grafo G=new Grafo(4);
		Grafo G2=new Grafo(4);
		G.display();
		//G.toXML();
		G.fromXML();
		String json=G.toJson();
		G2.fromJson(json);
		G2.display();
		G.display();
	}

}
