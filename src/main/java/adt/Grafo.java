package adt;

import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

public class Grafo {
	int[][] MatrizAdyancencia;
	
	public Grafo(int tamaño) {
		this.MatrizAdyancencia=new int[tamaño][tamaño];
		AsignarAleatorios();
	}
	
	public void display() {
		for (int i=0;i<this.MatrizAdyancencia.length;i++) {
			for (int j=0;j<this.MatrizAdyancencia[0].length;j++) {
				System.out.print(+this.MatrizAdyancencia[i][j]+"\t");
			}
			System.out.println("");
		}
	}
	
	public void AsignarAleatorios() {
		for (int i=0;i<this.MatrizAdyancencia.length;i++) {
			for (int j=0;j<this.MatrizAdyancencia[0].length;j++) {
				this.MatrizAdyancencia[i][j]=ThreadLocalRandom.current().nextInt(0, 5);
			}
		}
	}
	
	public void toXML()  {
		Element carsElement = new Element("matriz");
        Document doc = new Document(carsElement);

        //supercars element
        for (int i=0;i<this.MatrizAdyancencia.length;i++) {
        	Element columna = new Element("C"+i);
        	doc.getRootElement().addContent(columna);
        	
			for (int j=0;j<this.MatrizAdyancencia[0].length;j++) {
				Element supercarElement = new Element("F"+j);
		        supercarElement.setAttribute(new Attribute("peso",""+this.MatrizAdyancencia[i][j]));
		        columna.addContent(supercarElement);
			}
			
		}
        
        XMLOutputter xmlOutput = new XMLOutputter();
        
        xmlOutput.setFormat(Format.getPrettyFormat());
        
        try {
			xmlOutput.output(doc, System.out);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
		try {
			xmlOutput.output(doc, new FileWriter("/home/juan/eclipse-workspace/ProyectoServidor/matriz.xml"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
