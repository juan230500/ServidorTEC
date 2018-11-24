package pruebas;

import java.util.Random;

import lógica.Grafo;

public class CerrarRuta {
	public static void main(String[] args) {
		Grafo G=new Grafo(31);
		G.AdyacenciafromXML();
		Random rn = new Random();
		int a=0;
		int b=0;
		while(a==b) {
			 a=rn.nextInt(30 - 0 + 1) + 0;
			 b=rn.nextInt(30 - 0 + 1) + 0;
		}
		G.EliminarCamino(a, b);
		System.out.println("Se cierra: "+a+"=>"+b);
		}
}
