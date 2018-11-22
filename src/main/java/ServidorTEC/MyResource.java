package ServidorTEC;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;

import lógica.Almacenador;
import lógica.Grafo;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("myresource")
public class MyResource {
	
	@GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getIt() {
    	return "Hola";
    }
    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */
    @GET
    @Path("Mapa")
    @Produces(MediaType.APPLICATION_JSON)
    public String Mapa() {
    	Grafo G=new Grafo(31);
    	G.AdyacenciafromXML();
    	return G.MatrizToJson();
    }
    @POST
    @Path("Residencia")
    @Produces(MediaType.APPLICATION_JSON)
    public void Residencia(@FormParam("Residencia") String name) {
    	System.out.println(name);
    }
    
    @POST
    @Path("Carne")
    @Produces(MediaType.APPLICATION_JSON)
    public void Carne(@FormParam("Carne") String Carne) {
    	System.out.println(Carne);
    	Almacenador A=new Almacenador();
    	A.GuardarCarne(Carne);
    }
    
    @POST
    @Path("Ruta")
    @Produces(MediaType.APPLICATION_JSON)
    public String Ruta(@FormParam("Carne") String Carne,@FormParam("Residencia") String Residencia) {
    	System.out.println(Carne);
    	Almacenador A=new Almacenador();
    	Grafo G=new Grafo(31);
    	G.ConsultarCaminoAmigos(Integer.parseInt(Residencia),0, null) ;
    	G.AdyacenciafromXML();
    	G.display();
    	return G.CaminoToJson();
    }
    
    
}
