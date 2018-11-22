package lógica;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.concurrent.ThreadLocalRandom;

import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import com.google.gson.Gson;

/**
 * 
 * @author juan
 *
 */
public class Grafo {
	int[][] MatrizAdyancencia;
	int[][] MatrizFloydVértices;
	int[][] MatrizFloydDistancias;
	int maximo;
    SAXBuilder saxBuilder;
    LinkedList<Integer> MejorUltimaRuta;
    LinkedList<Integer> Tiempos;
    
    public LinkedList<Integer> getMejorUltimaRuta(){
    	return MejorUltimaRuta;
    }
    
    public LinkedList<Integer> getTiempos(){
    	return Tiempos;
    }
    
	public Grafo() {
		
	}
	
	public Grafo(int tamaño) {
		this.MatrizAdyancencia=new int[tamaño][tamaño];
		this.MatrizFloydVértices=new int[tamaño][tamaño];
		this.MatrizFloydDistancias=new int[tamaño][tamaño];
		this.maximo=101;
		this.saxBuilder = new SAXBuilder();
		this.MejorUltimaRuta=new LinkedList<Integer>();
		this.Tiempos=new LinkedList<Integer>();
	}
	
	/**
	 * Método que se encarga de recorrer cada casilla de la matriz
	 * y colocar un aleatorio en cada posición
	 */
	public void AsignarAleatorios() {
		for (int i=0;i<this.MatrizAdyancencia.length;i++) {
			for (int j=0;j<this.MatrizAdyancencia[0].length;j++) {
				this.MatrizAdyancencia[i][j]=ThreadLocalRandom.current().nextInt(0, this.maximo);
			}
		}
	}
	/**
	* Ejecuta el algoritmo de floyd conocido teoricamente por devolver una matriz
	 * con las rutas más cortas de cualquier a cualquier vértice y tambíen otra
	 * con las distancias que tomará esa ruta, estas dos matrices se almecan en el propio
	 * grafo y después pueden pasarse a un xml para consultarse solo cuando el cliente pida una mejor ruta
	 * 
	 */
	public void Floyd() {
		InciarFloyd();
		int k=0;
		int i=0;
		int j=0;
		int largo=this.MatrizAdyancencia.length;
		displayVertices();
		for (k=0;k<largo;k++) {
			System.out.print("iteración"+k+")");
			displayDistancias();
			for (i=0;i<this.MatrizAdyancencia.length;i++) {
				if (i==k) {
					continue;
				}
				for (j=0;j<this.MatrizAdyancencia[0].length;j++) {
					if (j==k || i==j) {
						continue;
					}
					int opcional=this.MatrizFloydDistancias[k][j]+this.MatrizFloydDistancias[i][k];
					if (opcional<this.MatrizFloydDistancias[i][j]) {
						this.MatrizFloydDistancias[i][j]=opcional;
						this.MatrizFloydVértices[i][j]=k;
					}
				}
			}
		}
		System.out.print("iteración"+k+")");
		displayDistancias();
		displayVertices();
	}
	 /**
	 * Incia las matrices de distancias y vértices
	 * según el estado incial de floyd
	 * para distancias iguales a cero se asigna un valor igual a la suma de dos máximos
	 */
	private void InciarFloyd() {
		for (int i=0;i<this.MatrizAdyancencia.length;i++) {
			for (int j=0;j<this.MatrizAdyancencia[0].length;j++) {
				this.MatrizFloydVértices[i][j]=i;
			}
		}
		int Distancia;
		for (int i=0;i<this.MatrizAdyancencia.length;i++) {
			for (int j=0;j<this.MatrizAdyancencia[0].length;j++) {
				if (i!=j) {
					Distancia=this.MatrizAdyancencia[i][j];
					if (Distancia==0) {
						Distancia=1000;
					}
					this.MatrizFloydDistancias[i][j]=Distancia;
				}
			}
		}
	}
	
	/**
	 * Reune todos los pasos necesarios para tomar un punto de partida, algunos amigos y un final
	 * y devuelve una buena ruta y la distancia total.
	 * @param Inicio partida del conductor
	 * @param Fin TEC
	 * @param ListaAmigos Paradas a realizar
	 * @return
	 */
	public LinkedList<Integer> ConsultarCaminoAmigos(int Inicio,int Fin,LinkedList<Integer> ListaAmigos){
		if (ListaAmigos==null) {
			this.MejorUltimaRuta=ConsultarCamino(Inicio, Fin);
			GuardarTiempos();
			return this.MejorUltimaRuta;
		}
		int[] ArrayDirecciones=AmigosToArray(Inicio,Fin,ListaAmigos);
		this.OrdenarAmigos(ArrayDirecciones);
		LinkedList<Integer> ListaTemporal;
		LinkedList<Integer> ListaDireccionesFinal=new LinkedList<Integer>();
		int DistanciaTotal=0;
		ListaDireccionesFinal.add(Inicio);
		for (int i=0;i<ArrayDirecciones.length-1;i++) {
			DistanciaTotal+=this.DistanciafromXML(ArrayDirecciones[i], ArrayDirecciones[i+1]);
			ListaTemporal=ConsultarCamino(ArrayDirecciones[i],ArrayDirecciones[i+1]);
			ListaTemporal.removeFirst();
			ListaDireccionesFinal.addAll(ListaTemporal);
		}
		System.out.println("$$$"+ListaDireccionesFinal.toString()+"="+DistanciaTotal+"s");
		this.MejorUltimaRuta=ListaDireccionesFinal;
		GuardarTiempos();
		return ListaDireccionesFinal;
	}
	
	public void GuardarTiempos() {
		for (int i=0;i<MejorUltimaRuta.size()-1;i++) {
			Tiempos.add(this.DistanciafromXML(MejorUltimaRuta.get(i), MejorUltimaRuta.get(i+1)));
		}
	}
	
	/**
	 * Convierte un punto de partida, un final y una lista de amigos en un array
	 * donde el primer elemento es el inicio y el ultimo es el final, el resto son amigos en desorden
	 * esto facilitará ordenar el array con "OrdenarAmigos"
	 * @param Inicio
	 * @param Fin
	 * @param ListaAmigos
	 * @return array donde el primer elemento es el inicio y el ultimo es el final, el resto son amigos en desorden
	 */
	public int[] AmigosToArray(int Inicio,int Fin,LinkedList<Integer> ListaAmigos) {
		int l=ListaAmigos.size();
		int[] ArrayDirecciones=new int[l+2];
		ArrayDirecciones[0]=Inicio;
		for (int i=1;i<l+1;i++) {
			ArrayDirecciones[i]=ListaAmigos.get(i-1);
		}
		ArrayDirecciones[l+1]=Fin;
		return ArrayDirecciones;
	}
	/**
	 * Con la matriz de vértices del algorimo de floyd,
	 * este método devuelve la lista de vértices a visitar
	 * que optimicen los tiempos para llegar de un vértice a otro
	 * @param inicio vertice de inicio
	 * @param fin vvertice de destino
	 * @return lista con los vertices a recorrer
	 */
	public LinkedList<Integer> ConsultarCamino(int inicio,int fin) {
		LinkedList<Integer> L = new LinkedList<Integer>();
		L.add(inicio);
		int actual=inicio;
		while (actual!=fin) {
			actual=VerticefromXML(fin, actual);
			L.add(actual);
		}
		System.out.println("$"+L.toString()+"="+this.DistanciafromXML(inicio, fin)+"s");
		return L;
	}
	
	/**
	 * Método que devuelve un array con el mejor orden para recoger n amigos
	 * pensando en ir primero por el más cercano
	 * @param ArrayDirecciones posicoines de los amigos
	 * @return array con los lugares a pasar en orden
	 */
	public void OrdenarAmigos(int[] ArrayDirecciones){
		int l=ArrayDirecciones.length;
		int[] ArrayTemporalAmigos=new int[l-2];
		
		for (int i=1;i<ArrayDirecciones.length-1;i++) {
			ArrayTemporalAmigos[i-1]=ArrayDirecciones[i];
		}
		
		int InicioActual=ArrayDirecciones[0];
		int[] Distancias;
		int Menor;
		
		for (int j=0;j<l-2;j++) {
			Distancias=this.ArrayDistancias(InicioActual, ArrayTemporalAmigos);
			Menor= this.MenorIndex(Distancias);
			InicioActual=ArrayTemporalAmigos[Menor];
			ArrayDirecciones[j+1]=InicioActual;
			ArrayTemporalAmigos=this.IgnorarIndex(ArrayTemporalAmigos, Menor);
		}
	}
	
	/**
	 * Devuelve el índice del menor elemento de un array
	 * @param arr array dentro del cual se busca el menor
	 * @return el índice de ese menor
	 */
	public int MenorIndex(int[] arr) {
        int index = 0;  
        for (int j = 1; j < arr.length; j++){  
            if (arr[j] < arr[index]){  
                index = j;//searching for lowest index  
            }  
        }  
        return index;
	}
	/**
	 * Métodos que devuelve en nuevo array de la distancia 
	 * desde un mismo inicio hasta varios lugares del array (enteros) 
	 * para saber hacia cual es más coveniente ir en un principio
	 * @param arr lugares posibles
	 * @param inicio punto de partida
	 * @return array con las distancias desde al inicio a cada punto del array
	 */
	public int[] ArrayDistancias(int inicio,int arr[]) {
		int [] Distancias=new int[arr.length];
		for (int i=0;i<arr.length;i++) {
			Distancias[i]=this.DistanciafromXML(inicio,arr[i]);
		}
		return Distancias;
	}
	/**
	 * Metodo que devuelve todo un array pero sin un índice
	 * @param i inidece a omitir
	 * @return el nuevo array
	 */
	public int[] IgnorarIndex(int[] arr,int index) {
		int[] NuevoArr=new int[arr.length-1];
		int j=0;
		for(int i=0;i<arr.length;i++) {
			if (i!=index) {
				NuevoArr[j]=arr[i];
				j++;
			}
		}
		return NuevoArr;
	}
	
	/**
	 * Método que hace un print de cada casilla de la matriz de vertices para floyd
	 * con el fin de verificar sus valores a lo largo del desarrollo
	 */
	public void displayVertices() {
		System.out.println("=====Vertices=====");
		for (int i=0;i<this.MatrizFloydVértices[0].length;i++) {
			System.out.print(i+"=>\t");
			for (int j=0;j<this.MatrizFloydVértices.length;j++) {
				System.out.print(this.MatrizFloydVértices[j][i]+"\t");
			}
			System.out.println("");
		}
		System.out.println("==========");
	}
	
	/**
	 * Método que hace un print de cada casilla de la matriz de distancias para floyd
	 * con el fin de verificar sus valores a lo largo del desarrollo
	 */
	public void displayDistancias() {
		System.out.println("=====Distancias=====");
		for (int i=0;i<this.MatrizFloydDistancias[0].length;i++) {
			for (int j=0;j<this.MatrizFloydDistancias.length;j++) {
				System.out.print(this.MatrizFloydDistancias[j][i]+"\t");
			}
			System.out.println("");
		}
		System.out.println("==========");
	}
	
	/**
	 * Método que hace un print de cada casilla de la matriz
	 * con el fin de verificar sus valores a lo largo del desarrollo
	 */
	public void display() {
		System.out.println("=====Adyancencia=====");
		for (int i=0;i<this.MatrizAdyancencia[0].length;i++) {
			System.out.print(i+"=>\t");
			for (int j=0;j<this.MatrizAdyancencia.length;j++) {
				System.out.print(this.MatrizAdyancencia[j][i]+"\t");
			}
			System.out.println("");
		}
		System.out.println("==========");
	}
	
	/**
	 * Crea un archivo xml con la matriz de adyacencia actual
	 * para fines de almacenamiento, el criterio indica que se puede guardar
	 * tanto la matriz de adyacencia como la matriz de distancia o vértices
	 * @param criterio entero que indica o que se va a almacenar
	 */
	public void toXML(int criterio)  {
		String nombre;
		int[][] matriz;
		if (criterio==0) {
			nombre="matriz";
			matriz=this.MatrizAdyancencia;
		}
		else if (criterio==1) {
			nombre="vertices";
			matriz=this.MatrizFloydVértices;
		}
		else {
			nombre="distancias";
			matriz=this.MatrizFloydDistancias;
		}
		
		Element carsElement = new Element("nombre");
        Document doc = new Document(carsElement);
        
        Element columna;
        Element fila;
        //Ciclo para añadir cada elemento de forma ordenada
        for (int i=0;i<this.MatrizAdyancencia.length;i++) {
        	columna = new Element("C"+i);
        	doc.getRootElement().addContent(columna);
			for (int j=0;j<this.MatrizAdyancencia[0].length;j++) {
				fila = new Element("F"+j);
		        fila.setAttribute(new Attribute("peso",""+matriz[i][j]));
		        columna.addContent(fila);
			}
		}
        
        XMLOutputter xmlOutput = new XMLOutputter();
       
        xmlOutput.setFormat(Format.getPrettyFormat());
		try {
			xmlOutput.output(doc, new FileWriter("src/main/java/"+nombre+".xml"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/** 
	 * Devuelve la distancia que se necesita para ir de un punto a otro
	 *  obtenida con Floyd
	 * @param fin columna
	 * @param inicio fila
	 * @return entero con la distancia
	 */
	public int DistanciafromXML(int inicio,int fin) {
		File inputFile = new File("C:/Users/Dell/eclipse-workspace/ServidorTEC/src/main/java/distancias.xml");
        this.saxBuilder = new SAXBuilder();
        Document document = null;
        
		try {
			document = saxBuilder.build(inputFile);
		} catch (JDOMException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
        Element classElement = document.getRootElement();
        
        String ValorActual=classElement.getChild("C"+fin).getChild("F"+inicio).getAttributeValue("peso");
        
        return Integer.parseInt(ValorActual);
	}
	/**
	 * Devuelve un numero en la matriz de vertices generada por Floyd
	 * para consuktar el mejor camino
	 * @param i columna
	 * @param j fila
	 * @return entero en esa posición
	 */
	public int VerticefromXML(int i,int j) {
		File inputFile = new File("C:/Users/Dell/eclipse-workspace/ServidorTEC/src/main/java/vertices.xml");
        this.saxBuilder = new SAXBuilder();
        Document document = null;
        
		try {
			document = saxBuilder.build(inputFile);
		} catch (JDOMException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
        Element classElement = document.getRootElement();
        
        String ValorActual=classElement.getChild("C"+i).getChild("F"+j).getAttributeValue("peso");
        
        return Integer.parseInt(ValorActual);
	}
	/**
	 * A partir de un archivo xml almacenado junto al mismo código del servidor,
	 * este método extrae la información en xml de una matriz de adyacencia en la actual
	 * con el fin de no necesitar guardrse en una variable.
	 */
	public void AdyacenciafromXML() {
		File inputFile = new File("C:/Users/Dell/eclipse-workspace/ServidorTEC/src/main/java/matriz.xml");
        this.saxBuilder = new SAXBuilder();
        Document document = null;
        
		try {
			document = saxBuilder.build(inputFile);
		} catch (JDOMException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        Element classElement = document.getRootElement();
        
        Element ColumnaActual;
        Element FilaActual;
        String ValorActual;
        int n;
        for (int i=0;i<this.MatrizAdyancencia.length;i++) {
        	ColumnaActual=classElement.getChild("C"+i);
			for (int j=0;j<this.MatrizAdyancencia[0].length;j++) {
				FilaActual=ColumnaActual.getChild("F"+j);
				ValorActual=FilaActual.getAttributeValue("peso");
				n=Integer.parseInt(ValorActual);
				this.MatrizAdyancencia[i][j]=n;
				
			}
		}
	}
	/**
	 * Convierte la matriz de adyancencia del grafo
	 * en una string de formato json
	 * @return la string en json
	 */
	public String MatrizToJson() {
		Gson gson = new Gson();
		String json=gson.toJson(this.MatrizAdyancencia);
		return json;
	}
	/***
	 * Convierte el ultimo mejor camino del grafo
	 * en una string de formato json
	 * @return la string en json
	 */
	public String CaminoToJson() {
		Gson gson = new Gson();
		LinkedList<LinkedList<Integer>> L=new LinkedList<LinkedList<Integer>>();
		L.add(this.MejorUltimaRuta);
		L.add(this.Tiempos);
		System.out.println(L.toString());
		return gson.toJson(L);
	}
	
	
}
