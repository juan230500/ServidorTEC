package pruebas;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;

public class MainCliente {

	public static void main(String[] args) {
		String REST_URI  = "http://192.168.100.4:8080/ServidorTEC/webapi/myresource/Mapa";
	  
	    Client client = ClientBuilder.newClient();
	 
	    String RequestMapa=client
	          .target(REST_URI)
	          .request(MediaType.APPLICATION_JSON)
	          .get(String.class);
	    
	    
	    System.out.println(RequestMapa);
	    
	    String REST_URI1  = "http://192.168.100.4:8080/ServidorTEC/webapi/myresource/Mapa1";
	    
	    String RequestMapa1=client
		          .target(REST_URI)
		          .request(MediaType.APPLICATION_JSON)
		          .get(String.class);
	    
	    System.out.println(RequestMapa);
	}

}
