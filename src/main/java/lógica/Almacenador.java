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


/**
 * @author juan
 *
 */
/**
 * @author juan
 *
 */
public class Almacenador {
	String RutaWorkSpace="/home/juan/eclipse-workspace";
	String RutaCarne=RutaWorkSpace+"/ServidorTEC/src/main/java/carnes.xml";
	String RutaEspera=RutaWorkSpace+"/ServidorTEC/src/main/java/espera.xml";
	String RutaViajes=RutaWorkSpace+"/ServidorTEC/src/main/java/viajes.xml";
	LinkedList<Integer> PosGenteEnEspera=new LinkedList<Integer>();
	

	public LinkedList<Integer> getPosGenteEnEspera() {
		return PosGenteEnEspera;
	}

	public void setPosGenteEnEspera(LinkedList<Integer> posGenteEnEspera) {
		PosGenteEnEspera = posGenteEnEspera;
	}
	
	public String SeguirEsperando(String Carne, String IsAmigo) {
		try {
        	File inputFile = new File(RutaEspera);
            SAXBuilder saxBuilder = new SAXBuilder();
			Document doc = saxBuilder.build(inputFile);
			Element rootElement = doc.getRootElement();
			Element supercarElement;
			if (IsAmigo.equals("1")) {
				supercarElement=rootElement.getChild("Amigos");
			}
			else {
				supercarElement=rootElement.getChild("Cualquiera");
			}
			if (supercarElement.getChild("E"+Carne)!=null) {
				String txt=supercarElement.getChild("E"+Carne).getText();
				if (txt!=null) {
					supercarElement.removeChild("E"+Carne);
			        XMLOutputter xmlOutput = new XMLOutputter();
			        xmlOutput.setFormat(Format.getPrettyFormat());
			        xmlOutput.output(doc, new FileWriter(RutaEspera));
					return txt;
				}
				else {
					return "1";
				}
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
	 * Busca el tiempo de un viaje por carne
	 * @param Carne
	 * @return
	 */
	public String Consultarviaje(String Carne) {
		try {
        	File inputFile = new File(RutaViajes);
            SAXBuilder saxBuilder = new SAXBuilder();
			Document doc = saxBuilder.build(inputFile);
			Element rootElement = doc.getRootElement();
	        Element supercarElement = rootElement.getChild("E"+Carne);
	        return supercarElement.getAttributeValue("tiempo");
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
	 * Metodo que registra el inicio del viaje y el tiempo en el que inicia
	 * @param CarneConductorC
	 * @param CarnesResto
	 * @return
	 */
	public String RegistrarViaje(String CarneConductor,LinkedList<String> CarnesResto) {
		try {
        	File inputFile = new File(RutaViajes);
            SAXBuilder saxBuilder = new SAXBuilder();
			Document doc = saxBuilder.build(inputFile);
			Element rootElement = doc.getRootElement();
			if (rootElement.getChild("E"+CarneConductor)==null) {
				Element supercarElement = new Element("E"+CarneConductor);
				supercarElement.setAttribute("tiempo", ""+(int)System.currentTimeMillis());
				for (int i=0;i<CarnesResto.size();i++) {
					Element pasajero = new Element("E"+CarnesResto.get(i));
					supercarElement.addContent(pasajero);
				}
		        doc.getRootElement().addContent(supercarElement);
		        XMLOutputter xmlOutput = new XMLOutputter();
		        xmlOutput.setFormat(Format.getPrettyFormat());
		        xmlOutput.output(doc, new FileWriter(RutaViajes));
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
	 * Suma una calificacion al promedio existente
	 * @param Carne carné a sumar 
	 * @return false si no existe ese carne, true de lo contrario
	 */
	public String SumarCalificacion(String Carne,String Calificacion) {
		try {
			File inputFile = new File(RutaCarne);
            SAXBuilder saxBuilder = new SAXBuilder();
			Document doc = saxBuilder.build(inputFile);
			Element rootElement = doc.getRootElement();
	        if (rootElement.getChild("E"+Carne)!=null) {
	        	Element supercarElement=rootElement.getChild("E"+Carne);
	        	int cantidad=Integer.parseInt(supercarElement.getAttributeValue("NCalificaciones"));
	        	int actual=Integer.parseInt(supercarElement.getAttributeValue("Calificacion"));
	        	int nueva=Integer.parseInt(Calificacion);
	        	System.out.println(nueva);
	        	supercarElement.setAttribute("Calificacion", ""+(actual+nueva));
	        	supercarElement.setAttribute("NCalificaciones", ""+(cantidad+1));
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
	 * Obtiene el promedio de calificaciones de un carne
	 * @param Carne carné a sumar 
	 * @return false si no existe ese carne, true de lo contrario
	 */
	public int ConsultarCalificacionPromedio(String Carne) {
		try {
			File inputFile = new File(RutaCarne);
            SAXBuilder saxBuilder = new SAXBuilder();
			Document doc = saxBuilder.build(inputFile);
			Element rootElement = doc.getRootElement();
	        if (rootElement.getChild("E"+Carne)!=null) {
	        	Element supercarElement=rootElement.getChild("E"+Carne);
	        	int cantidad=Integer.parseInt(supercarElement.getAttributeValue("NCalificaciones"));
	        	int actual=Integer.parseInt(supercarElement.getAttributeValue("Calificacion"));
		        return actual*100/cantidad;
	        }
	        else {
	        	return 0;
	        }
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
	 * Suma un viaje más a algún carné
	 * @param Carne carné a sumar el viaje
	 * @return false si no existe ese carne, true de lo contrario
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
	 * Tiene dos flujos:
	 * 1)Busca gente que quiera ser recogida por cualquiera y que esté en la ruta propia
	 * 2)Busca gente que esté esperando ser recogida por un amigo y que sea amigo del conductor
	 * @param Ruta camino fijo dentro de la cual buscar gente
	 * @param Carne carne del conductor para consulta por amigos
	 * @param IsAmigos true para el flujo 2), false para el 1)
	 * @return Un lista con las strings de los carnés
	 */
	public LinkedList<String> ConsultarEnEspera(LinkedList<Integer> Ruta,String Carne,String IsAmigos, String max) {
		try {
			LinkedList<String> L=new LinkedList<String>();
        	File inputFile = new File(RutaEspera);
            SAXBuilder saxBuilder = new SAXBuilder();
			Document doc = saxBuilder.build(inputFile);
			Element rootElement = doc.getRootElement();
			Element supercarElement;
			int maximo=Integer.parseInt(max);
			if (IsAmigos.equals("1")) {
				supercarElement=rootElement.getChild("Amigos");
				LinkedList<String> Laux=this.ConsultarAmigos(Carne);
				for (int i=0;i<Laux.size() && i<maximo;i++) {
					Element tmp=supercarElement.getChild(Laux.get(i));
					if (tmp!=null) {
						L.add(tmp.getName());
						PosGenteEnEspera.add(Integer.parseInt(tmp.getAttributeValue("Residencia")));
					}
				}
			}
			else {
				supercarElement=rootElement.getChild("Cualquiera");
				for (int i=0;i<Ruta.size() && i<maximo;i++) {
					List<Element> ListaEspera=supercarElement.getChildren();
					for (int j=0;j<ListaEspera.size();j++) {
						Element tmp=ListaEspera.get(j);
						if (tmp.getAttributeValue("Residencia").equals(""+Ruta.get(i))) {
							L.add(tmp.getName());
							PosGenteEnEspera.add(Integer.parseInt(tmp.getAttributeValue("Residencia")));
						}
					}
				}
			}
			System.out.println(PosGenteEnEspera.toString());
			return L;
			
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Guarda al estudiante en una lsita de espera [para que lo regoja cualquiera que le quede de camino
	 * si ya se habia guardado no se repite
	 * @param Carne
	 * @param Residencia
	 * @return
	 */
	public String PonerEnEspera(String Carne, String Residencia,String IsAmigos) {
		try {
        	File inputFile = new File(RutaEspera);
            SAXBuilder saxBuilder = new SAXBuilder();
			Document doc = saxBuilder.build(inputFile);
			Element rootElement = doc.getRootElement();
			Element supercarElement;
			if (IsAmigos.equals("1")) {
				supercarElement=rootElement.getChild("Amigos");
			}
			else {
				supercarElement=rootElement.getChild("Cualquiera");
			}
			if (supercarElement.getChild("E"+Carne)==null) {
				Element Pasajero = new Element("E"+Carne);
				Pasajero.setAttribute("Residencia", Residencia);
				supercarElement.addContent(Pasajero);
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
	 * Una vez que el estudiante recibe un conductor, se señala en la lista de espera
	 * para dar campo a otros estudiantes
	 * @param Carne Carne del estudiante
	 * @param Residencia residencia del estudiante
	 * @return falso si ni siquera estaba en la lista, true si no
	 */
	public String SacarDeEspera(String Conductor,String Carne,String IsAmigos) {
		try {
			File inputFile = new File(RutaEspera);
            SAXBuilder saxBuilder = new SAXBuilder();
			Document doc = saxBuilder.build(inputFile);
			Element rootElement = doc.getRootElement();
			Element supercarElement;
			if (IsAmigos.equals("1")) {
				supercarElement=rootElement.getChild("Amigos");
			}
			else {
				supercarElement=rootElement.getChild("Cualquiera");
			}
			if (supercarElement.getChild("E"+Carne)!=null) {
				supercarElement.getChild("E"+Carne).setText(Conductor);
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
	        	Element amigo = rootElement.getChild("E"+CarneAmigo);
		        supercarElement.addContent(nuevoamigo);
		        nuevoamigo=new Element("E"+CarnePropio);
		        amigo.setContent(nuevoamigo);
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
	 * Busca los amigos de un carne registrados como tal
	 * @param Carne
	 * @return
	 */
	public LinkedList<String> ConsultarAmigos(String Carne) {
		try {
        	File inputFile = new File(RutaCarne);
            SAXBuilder saxBuilder = new SAXBuilder();
			Document doc = saxBuilder.build(inputFile);
			Element rootElement = doc.getRootElement();
	        Element supercarElement = rootElement.getChild("E"+Carne);
	        List<Element> L=supercarElement.getChildren();
	        LinkedList<String> LS=new LinkedList<String>();
	        for (int i=0;i<L.size();i++) {
	        	LS.add(L.get(i).getName());
	        }
	        return LS;
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
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
				supercarElement.setAttribute("NCalificaciones", "0");
				supercarElement.setAttribute("Calificacion", "0");
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
