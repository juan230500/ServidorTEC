package pruebas;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;

import lógica.Almacenador;

public class MainCliente {

	public static void main(String[] args) {
		Almacenador A=new Almacenador();
		A.PonerEnEspera("2018135361","5");
		/*A.GuardarCarne("2018135360");
		A.GuardarResidencia("2018135360","4");
		A.AgregarAmigo("2018135360", "2018135361");
		System.out.println(A.ConsultarResidencia("2018135360"));*/
		/*String REST_URI  = "http://192.168.100.4:8080/ServidorTEC/webapi/myresource/Mapa";
	  
	    Client client = ClientBuilder.newClient();
	 
	    String RequestMapa=client
	          .target(REST_URI)
	          .request(MediaType.APPLICATION_JSON)
	          .get(String.class);
	    
	    
	    System.out.println(RequestMapa);*/
	}

}
