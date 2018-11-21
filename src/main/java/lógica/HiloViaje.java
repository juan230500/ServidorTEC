package lógica;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilderFactory;

import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

public class HiloViaje implements Runnable {
	long tInicial;
	long tFinal;
	int period;
	Document doc;

	
	public HiloViaje(long Duracion) {
		tInicial=System.currentTimeMillis();
		tFinal=Duracion*1000;
		period=1000;
		
        try {
        	File inputFile = new File("src/main/java/viajes.xml");
            SAXBuilder saxBuilder = new SAXBuilder();
			doc = saxBuilder.build(inputFile);
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		run();
	}
	
	@Override
	public void run() {
		Element rootElement = doc.getRootElement();
        Element supercarElement = new Element("E2018135361");
        supercarElement.setAttribute(new Attribute("tiempo",""));
        doc.getRootElement().addContent(supercarElement);
        
        XMLOutputter xmlOutput = new XMLOutputter();
        xmlOutput.setFormat(Format.getPrettyFormat());
        
		while (System.currentTimeMillis()-tInicial<tFinal) {
			System.out.println(System.currentTimeMillis()-tInicial);
			try {
				Attribute attribute = supercarElement.getAttribute("tiempo");
		        attribute.setValue(""+(int)(System.currentTimeMillis()-tInicial)/1000);
				xmlOutput.output(doc, new FileWriter("src/main/java/viajes.xml"));
				Thread.sleep(period);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
	 			// TODO Auto-generated catch block
	 			e.printStackTrace();
	 		}
		}
	}
	
	public static void main(String[] args) {
		HiloViaje H=new HiloViaje(50);
	}

}
