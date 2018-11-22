package lógica;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.jdom2.output.Format;
import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import com.google.gson.Gson;


public class Almacenador {
	String RutaCarne="/home/juan/eclipse-workspace/ServidorTEC/src/main/java/carnes.xml";
	String RutaEspera="/home/juan/eclipse-workspace/ServidorTEC/src/main/java/espera.xml";
	
	/**
	 * Lee el array de carnés, obtiene los cinco conductores con más viajes y
	 * devuelve sus carnes en orden
	 * @return Array con los carnes en orden de mayor a menor
	 */
	public String Top5() {
		List<Element> L;
		LinkedList<Element> LE=new LinkedList<Element>();
		String[] Top=new String[5];
		String[] Viajes=new String[5];
		try {
			File inputFile = new File(RutaCarne);
            SAXBuilder saxBuilder = new SAXBuilder();
			Document doc = saxBuilder.build(inputFile);
			Element rootElement = doc.getRootElement();
			L=rootElement.getChildren();
			for (int k=0;k<L.size();k++) {
				LE.add(L.get(k));
    			}
	        	Element supercarElement;
	        	for (int i=0;i<5 && i<L.size();i++) {
	        		int mayor=-1;
	        		int index=0;
	        		for (int j=i;j<LE.size();j++) {
	        			supercarElement=LE.get(j);
	        			int tmp=Integer.parseInt(supercarElement.getAttributeValue("Viajes"));
	        			if(tmp>mayor){
	        				mayor=tmp;
	        				index=j;
	        			}
		        	}
	        		Element Etmp=LE.get(i);
	        		LE.set(i, LE.get(index));
	        		LE.set(index, Etmp);
	        		Top[i]=LE.get(i).getName();
	        		Viajes[i]=""+mayor;
	        	}
	        XMLOutputter xmlOutput = new XMLOutputter();
	        xmlOutput.setFormat(Format.getPrettyFormat());
	        xmlOutput.output(doc, new FileWriter(RutaCarne));
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String[][] S= {Top,Viajes};
		Gson gson=new Gson();
		return gson.toJson(S);
	}
	/**
	 * Suma un viaje más a algún carné
	 * @param Carne carné a sumar el viaje
	 * @return false,si no existe ese carne, true de lo contrario
	 */
	public String SumarViaje(String Carne) {
		try {
			File inputFile = new File(RutaCarne);
            SAXBuilder saxBuilder = new SAXBuilder();
			Document doc = saxBuilder.build(inputFile);
			Element rootElement = doc.getRootElement();
	        if (rootElement.getChild("E"+Carne)!=null) {
	        	Element supercarElement=rootElement.getChild("E"+Carne);
	        	supercarElement.setAttribute("Viajes", ""+(Integer.parseInt(supercarElement.getAttributeValue("Viajes"))+1));
		        XMLOutputter xmlOutput = new XMLOutputter();
		        xmlOutput.setFormat(Format.getPrettyFormat());
		        xmlOutput.output(doc, new FileWriter(RutaCarne));
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
	 * Guarda al estudiante en una lsita de espera [para que lo regoja cualquiera que le quede de camino
	 * si ya se habia guardado no se repite
	 * @param Carne
	 * @param Residencia
	 * @return
	 */
	public String PonerEnEspera(String Carne, String Residencia) {
		try {
        	File inputFile = new File(RutaEspera);
            SAXBuilder saxBuilder = new SAXBuilder();
			Document doc = saxBuilder.build(inputFile);
			Element rootElement = doc.getRootElement();
			if (rootElement.getChild("E"+Carne)==null) {
				Element supercarElement = new Element("E"+Carne);
				supercarElement.setAttribute("Residencia", Residencia);
		        doc.getRootElement().addContent(supercarElement);
		        XMLOutputter xmlOutput = new XMLOutputter();
		        xmlOutput.setFormat(Format.getPrettyFormat());
		        xmlOutput.output(doc, new FileWriter(RutaEspera));
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
        	File inputFile = new File(RutaCarne);
            SAXBuilder saxBuilder = new SAXBuilder();
			Document doc = saxBuilder.build(inputFile);
			Element rootElement = doc.getRootElement();
			if (rootElement.getChild("E"+Carne)!=null) {
				rootElement.removeChild("E"+Carne);
		        XMLOutputter xmlOutput = new XMLOutputter();
		        xmlOutput.setFormat(Format.getPrettyFormat());
		        xmlOutput.output(doc, new FileWriter(RutaCarne));
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
			File inputFile = new File(RutaCarne);
            SAXBuilder saxBuilder = new SAXBuilder();
			Document doc = saxBuilder.build(inputFile);
			Element rootElement = doc.getRootElement();
	        Element supercarElement = rootElement.getChild("E"+CarnePropio);
	        if (supercarElement.getChild("E"+CarneAmigo)==null) {
	        	Element nuevoamigo=new Element("E"+CarneAmigo);
		        supercarElement.addContent(nuevoamigo);
		        XMLOutputter xmlOutput = new XMLOutputter();
		        xmlOutput.setFormat(Format.getPrettyFormat());
		        xmlOutput.output(doc, new FileWriter(RutaCarne));
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
        	File inputFile = new File(RutaCarne);
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
        	File inputFile = new File(RutaCarne);
            SAXBuilder saxBuilder = new SAXBuilder();
			Document doc = saxBuilder.build(inputFile);
			Element rootElement = doc.getRootElement();
			if (rootElement.getChild("E"+Carne)==null) {
				Element supercarElement = new Element("E"+Carne);
				supercarElement.setAttribute("Viajes", "0");
		        doc.getRootElement().addContent(supercarElement);
		        XMLOutputter xmlOutput = new XMLOutputter();
		        xmlOutput.setFormat(Format.getPrettyFormat());
		        xmlOutput.output(doc, new FileWriter(RutaCarne));
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
			File inputFile = new File(RutaCarne);
            SAXBuilder saxBuilder = new SAXBuilder();
			Document doc = saxBuilder.build(inputFile);
			Element rootElement = doc.getRootElement();
	        Element supercarElement = rootElement.getChild("E"+Carne);
	        supercarElement.setAttribute("Residencia", Residencia);
	        XMLOutputter xmlOutput = new XMLOutputter();
	        xmlOutput.setFormat(Format.getPrettyFormat());
	        xmlOutput.output(doc, new FileWriter(RutaCarne));
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
