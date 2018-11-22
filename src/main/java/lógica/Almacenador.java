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
	 * Guarda al estudiante en una lsita de espera [para que lo regoja cualquiera que le quede de camino
	 * si ya se habia guardado no se repite
	 * @param Carne
	 * @param Residencia
	 * @return
	 */
	public String PonerEnEspera(String Carne, String Residencia) {
		try {
        	File inputFile = new File("C:/Users/Dell/eclipse-workspace/ServidorTEC/src/main/java/espera.xml");
            SAXBuilder saxBuilder = new SAXBuilder();
			Document doc = saxBuilder.build(inputFile);
			Element rootElement = doc.getRootElement();
			if (rootElement.getChild("E"+Carne)==null) {
				Element supercarElement = new Element("E"+Carne);
				supercarElement.setAttribute("Residencia", Residencia);
		        doc.getRootElement().addContent(supercarElement);
		        XMLOutputter xmlOutput = new XMLOutputter();
		        xmlOutput.setFormat(Format.getPrettyFormat());
		        xmlOutput.output(doc, new FileWriter("C:/Users/Dell/eclipse-workspace/ServidorTEC/src/main/java/espera.xml"));
		        return "1";
			}
			else {
				return "0";
			}
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "0";
	}
	
	/**
	 * Una vez que el estudiante recibe un conductor, se saca de la lsita de espera
	 * para dar campo a otros estudiantes
	 * @param Carne Carne del estudiante
	 * @param Residencia residencia del estudiante
	 * @return falso si ni siquera estaba en la lista, true si no
	 */
	public String SacarDeEspera(String Carne) {
		try {
        	File inputFile = new File("C:/Users/Dell/eclipse-workspace/ServidorTEC/src/main/java/espera.xml");
            SAXBuilder saxBuilder = new SAXBuilder();
			Document doc = saxBuilder.build(inputFile);
			Element rootElement = doc.getRootElement();
			if (rootElement.getChild("E"+Carne)!=null) {
				rootElement.removeChild("E"+Carne);
		        XMLOutputter xmlOutput = new XMLOutputter();
		        xmlOutput.setFormat(Format.getPrettyFormat());
		        xmlOutput.output(doc, new FileWriter("C:/Users/Dell/eclipse-workspace/ServidorTEC/src/main/java/espera.xml"));
		        return "1";
			}
			else {
				return "0";
			}
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "0";
	}
	
	/**
	 * Agrega un amigo sobre un carne ya guardado en el xml
	 * @param CarnePropio carne del conductor
	 * @param CarneAmigo carne del nuevo amigo
	 * @return false si ya eran amigos, true si no
	 */
	public String AgregarAmigo(String CarnePropio,String CarneAmigo) {
		try {
			File inputFile = new File("C:/Users/Dell/eclipse-workspace/ServidorTEC/src/main/java/carnes.xml");
            SAXBuilder saxBuilder = new SAXBuilder();
			Document doc = saxBuilder.build(inputFile);
			Element rootElement = doc.getRootElement();
	        Element supercarElement = rootElement.getChild("E"+CarnePropio);
	        if (supercarElement.getChild("E"+CarneAmigo)==null) {
	        	Element nuevoamigo=new Element("E"+CarneAmigo);
		        supercarElement.addContent(nuevoamigo);
		        XMLOutputter xmlOutput = new XMLOutputter();
		        xmlOutput.setFormat(Format.getPrettyFormat());
		        xmlOutput.output(doc, new FileWriter("C:/Users/Dell/eclipse-workspace/ServidorTEC/src/main/java/carnes.xml"));
		        return "1";
	        }
	        else {
	        	return "0";
	        }
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "0";
	}
	
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
	        return Integer.parseInt(supercarElement.getAttributeValue("Residencia"));
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
	public String GuardarCarne(String Carne) {
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
		        return "1";
			}
			else {
				return "0";
			}
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "0";
		
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
	        supercarElement.setAttribute("Residencia", Residencia);
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
