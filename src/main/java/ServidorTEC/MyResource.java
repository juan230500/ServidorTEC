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
	Almacenador A=new Almacenador();
	
	@GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getIt() {
    	return "Hola";
    }
	
    @GET
    @Path("Mapa")
    @Produces(MediaType.APPLICATION_JSON)
    public String Mapa() {
    	Grafo G=new Grafo(31);
    	G.AdyacenciafromXML();
    	return G.MatrizToJson();
    }

    @POST
    @Path("NuevoAmigo")
    @Produces(MediaType.APPLICATION_JSON)
    public String NuevoAmigo(@FormParam("Conductor") String CarneConductor,@FormParam("Amigo") String CarneAmigo) {
    	System.out.println("Se agrega a "+CarneAmigo+" como un amigo de "+CarneConductor);
    	return A.AgregarAmigo(CarneConductor, CarneAmigo);
    }
    
    @POST
    @Path("Residencia")
    @Produces(MediaType.APPLICATION_JSON)
    public void Residencia(@FormParam("Carne") String Carne,@FormParam("Residencia") String Residencia) {
    	System.out.println("++"+Residencia);
    	A.GuardarResidencia(Carne, Residencia);
    }
    
    @GET
    @Path("Top5")
    @Produces(MediaType.APPLICATION_JSON)
    public String Top5() {
    	return A.Top5();
    }
    
    @POST
    @Path("Carne")
    @Produces(MediaType.APPLICATION_JSON)
    public String Carne(@FormParam("Carne") String Carne) {
    	System.out.println("Se agrega el carne "+Carne);
    	Almacenador A=new Almacenador();
    	return A.GuardarCarne(Carne);
    }
    
    @POST
    @Path("RutaSolo")
    @Produces(MediaType.APPLICATION_JSON)
    public String Ruta(@FormParam("Residencia") String Residencia) {
    	System.out.println(Residencia);
    	Almacenador A=new Almacenador(); 
    	Grafo G=new Grafo(31);
    	G.ConsultarCaminoAmigos(Integer.parseInt(Residencia),0, null) ;
    	G.AdyacenciafromXML();
    	G.display();
    	return G.CaminoToJson();
    }
    
    @POST
    @Path("Espera")
    @Produces(MediaType.APPLICATION_JSON)
    public void Espera(@FormParam("Carne") String Carne) {
    	
    }
    
    @POST
    @Path("IniciarViaje")
    @Produces(MediaType.APPLICATION_JSON)
    public String IniciarViaje(@FormParam("Carne") String Carne) {
    	A.AgregarAmigo(Carne, "2018131313");
    	return A.RegistrarViaje(Carne, A.ConsultarAmigos(Carne));
    }
    
    
    
}
