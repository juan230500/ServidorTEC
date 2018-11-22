package lógica;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.jdom2.output.Format;
import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;


public class Almacenador {
	/**
	 * Lee el archivo de carnes y busca la residencia de alguno
	 * @param Carne del cual consultar residencia
	 * @return int con la residencia
	 */
	public int ConsultarResidencia(String Carne) {
		try {
        	File inputFile = new File("C:/Users/Dell/eclipse-workspace/ServidorTEC/src/main/java/carnes.xml");
            SAXBuilder saxBuilder = new SAXBuilder();
			Document doc = saxBuilder.build(inputFile);
			Element rootElement = doc.getRootElement();
	        Element supercarElement = rootElement.getChild("E"+Carne);
	        return Integer.parseInt(supercarElement.getText());
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
	/**
	 * Almacena un nuevo carne y si ya existia entonces retorna un false
	 * @param Carne carne a agregar
	 * @return true si es nuevo carne
	 */
	public boolean GuardarCarne(String Carne) {
		try {
        	File inputFile = new File("C:/Users/Dell/eclipse-workspace/ServidorTEC/src/main/java/carnes.xml");
            SAXBuilder saxBuilder = new SAXBuilder();
			Document doc = saxBuilder.build(inputFile);
			Element rootElement = doc.getRootElement();
			if (rootElement.getChild("E"+Carne)==null) {
				Element supercarElement = new Element("E"+Carne);
		        doc.getRootElement().addContent(supercarElement);
		        XMLOutputter xmlOutput = new XMLOutputter();
		        xmlOutput.setFormat(Format.getPrettyFormat());
		        xmlOutput.output(doc, new FileWriter("C:/Users/Dell/eclipse-workspace/ServidorTEC/src/main/java/carnes.xml"));
		        return true;
			}
			else {
				return false;
			}
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
		
	}
	/**
	 * Asigna una residencia a un carne registrado
	 * @param Carne
	 * @param Residencia
	 */
	public void GuardarResidencia(String Carne,String Residencia) {
		try {
			File inputFile = new File("C:/Users/Dell/eclipse-workspace/ServidorTEC/src/main/java/carnes.xml");
            SAXBuilder saxBuilder = new SAXBuilder();
			Document doc = saxBuilder.build(inputFile);
			Element rootElement = doc.getRootElement();
	        Element supercarElement = rootElement.getChild("E"+Carne);
	        supercarElement.setText(Residencia);
	        XMLOutputter xmlOutput = new XMLOutputter();
	        xmlOutput.setFormat(Format.getPrettyFormat());
	        xmlOutput.output(doc, new FileWriter("C:/Users/Dell/eclipse-workspace/ServidorTEC/src/main/java/carnes.xml"));
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) {
		
		

	         //root element
	         /*Element carsElement = new Element("Estudiantes");
	         Document doc = new Document(carsElement);

	         XMLOutputter xmlOutput = new XMLOutputter();
	         xmlOutput.setFormat(Format.getPrettyFormat());
	 		try {
	 			xmlOutput.output(doc, new FileWriter("src/main/java/carnes.xml"));
	 		} catch (IOException e) {
	 			e.printStackTrace();
	 		}*/
	 		
	
	   
}
	}
