package pruebas;

import java.util.Arrays;
import java.util.LinkedList;

import com.google.gson.Gson;
/**
 * 
 * @author juan
 *
 */
public class Cliente {
	int[][] MatrizAdyancencia;
	
	public Cliente() {
		
	}
	
	/**
	 * A partir de una string en formato json se obtiene
	 * una nueva matriz de adyancencia
	 * @param json
	 */
	public void MatrizFromJson(String json){
		Gson gson = new Gson();
		this.MatrizAdyancencia=gson.fromJson(json, int[][].class);
	}
	
	/**
	 * A partir de una string en formato json se obtiene
	 * un camino a seguir para optimizar el recorrido con o sin amigos
	 * @param json
	 * @return un array generado en el servidor con el camino
	 */
	public int[] CaminoFromJson(String json){
		Gson gson = new Gson();
		return gson.fromJson(json,int[].class);
	}
}
