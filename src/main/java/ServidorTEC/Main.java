package ServidorTEC;

import adt.Grafo;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Grafo G=new Grafo(4);
		G.display();
		//G.toXML();
		G.inXML();
		System.out.println("$");
		G.display();
		
	}

}
