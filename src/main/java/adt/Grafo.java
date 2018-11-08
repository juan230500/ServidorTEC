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

/**
 * 
 * @author juan
 *
 */
public class Grafo {
	int[][] MatrizAdyancencia;
	
	public Grafo(int tamaño) {
		this.MatrizAdyancencia=new int[tamaño][tamaño];
		AsignarAleatorios();
	}
	/**
	 * Método que hace un print de cada casilla de la matriz
	 * con el fin de verificar sus valores a lo largo del desarrollo
	 */
	public void display() {
		for (int i=0;i<this.MatrizAdyancencia[0].length;i++) {
			for (int j=0;j<this.MatrizAdyancencia.length;j++) {
				System.out.print(+this.MatrizAdyancencia[j][i]+"\t");
			}
			System.out.println("");
		}
	}
	/**
	 * Método que se encarga de recorrer cada casilla de la matriz
	 * y colocar un aleatorio en cada posición
	 */
	public void AsignarAleatorios() {
		for (int i=0;i<this.MatrizAdyancencia.length;i++) {
			for (int j=0;j<this.MatrizAdyancencia[0].length;j++) {
				this.MatrizAdyancencia[i][j]=ThreadLocalRandom.current().nextInt(0, 5);
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
			xmlOutput.output(doc, new FileWriter("/home/juan/Documentos/matriz.xml"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void inXML() {
		File inputFile = new File("/home/juan/Documentos/matriz.xml");
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
	
}
