package adt;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
	
	
	public Grafo(int tamaño) {
		this.MatrizAdyancencia=new int[tamaño][tamaño];
		this.MatrizFloydVértices=new int[tamaño][tamaño];
		this.MatrizFloydDistancias=new int[tamaño][tamaño];
		this.maximo=5;
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
		displayVertices();
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
	 * Crea un archivo xml con la matriz de adyacencia actual
	 * para fines de almacenamiento
	 */
	public void toXML()  {
		Element carsElement = new Element("matriz");
        Document doc = new Document(carsElement);
        
        Element columna;
        Element fila;
        //Ciclo para añadir cada elemento de forma ordenada
        for (int i=0;i<this.MatrizAdyancencia.length;i++) {
        	columna = new Element("C"+i);
        	doc.getRootElement().addContent(columna);
			for (int j=0;j<this.MatrizAdyancencia[0].length;j++) {
				fila = new Element("F"+j);
		        fila.setAttribute(new Attribute("peso",""+this.MatrizAdyancencia[i][j]));
		        columna.addContent(fila);
			}
		}
        
        XMLOutputter xmlOutput = new XMLOutputter();
       
        xmlOutput.setFormat(Format.getPrettyFormat());
		try {
			xmlOutput.output(doc, new FileWriter("src/main/java/matriz.xml"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * A partir de un archivo xml almacenado junto al mismo código del servidor,
	 * este método extrae la información en xml de una matriz de adyacencia en la actual
	 * con el fin de no necesitar guardrse en una variable.
	 */
	public void fromXML() {
		File inputFile = new File("src/main/java/matriz.xml");
        SAXBuilder saxBuilder = new SAXBuilder();
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
	 * en una string de formate json
	 * @return la string en json
	 */
	public String toJson() {
		Gson gson = new Gson();
		String json=gson.toJson(this.MatrizAdyancencia);
		return json;
	}
	/**
	 * A partir de una string en formato json se obtiene
	 * una nueva matriz de adyancencia
	 * @param json
	 */
	public void fromJson(String json){
		Gson gson = new Gson();
		this.MatrizAdyancencia=gson.fromJson(json, int[][].class);
	}
	
}
