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
    	Gson gson=new Gson();
    	String json="{\"Tiempos\":"+gson.toJson(G.getTiempos())+
    			", \"Ruta\":"+gson.toJson(G.getMejorUltimaRuta())+
    			", \"Pasajeros\":"+gson.toJson(A.ConsultarEnEspera(G.getMejorUltimaRuta(), "", "0"))+
    			", \"PosicionesPasajeros\":"+gson.toJson(A.getPosGenteEnEspera())+
    			"}";
    	return json;
    }
    
    @POST
    @Path("RutaAmigo")
    @Produces(MediaType.APPLICATION_JSON)
    public String RutaAmigo(@FormParam("Residencia") String Residencia,@FormParam("Carne") String Carne) {
    	System.out.println(Residencia);
    	Almacenador A=new Almacenador(); 
    	LinkedList<String> ListaAmigos=A.ConsultarEnEspera(null, Carne, "1");
    	LinkedList<Integer> ListaPos=A.getPosGenteEnEspera();
    	Grafo G=new Grafo(31);
    	G.ConsultarCaminoAmigos(Integer.parseInt(Residencia),0, ListaPos) ;
    	Gson gson=new Gson();
    	String json="{\"Tiempos\":"+gson.toJson(G.getTiempos())+
    			", \"Ruta\":"+gson.toJson(G.getMejorUltimaRuta())+
    			", \"Pasajeros\":"+gson.toJson(ListaAmigos)+
    			", \"PosicionesPasajeros\":"+gson.toJson(ListaPos)+
    			"}";
    	return json;
    }
    
    @POST
    @Path("Calificar")
    @Produces(MediaType.APPLICATION_JSON)
    public String Calificar(@FormParam("Calificacion") String Calificacion,@FormParam("Carne") String Carne) {
    	return A.SumarCalificacion(Carne, Calificacion);
    }
    
    @POST
    @Path("CalificacionPropia")
    @Produces(MediaType.APPLICATION_JSON)
    public String Calificar(@FormParam("Carne") String Carne) {
    	return A.ConsultarCalificacionPromedio(Carne);
    }
    
    @POST
    @Path("Espera")
    @Produces(MediaType.APPLICATION_JSON)
    public String Espera(@FormParam("Carne") String Carne,
    		@FormParam("Residencia") String Residencia,
    		@FormParam("SoloAmigos") String IsAmigos) {
    	System.out.println("Se pone en espera a "+Carne+",Quiero amigo="+IsAmigos);
    	return A.PonerEnEspera(Carne, Residencia, IsAmigos);
    }
    
    @POST
    @Path("SalirEspera")
    @Produces(MediaType.APPLICATION_JSON)
    public String SalirEspera(@FormParam("Carne") String Carne,
    		@FormParam("SoloAmigos") String IsAmigos) {
    	System.out.println("Se se saca de espera a "+Carne);
    	Grafo G=new Grafo(31);
    	A.ConsultarEnEspera(G.ConsultarCaminoAmigos(0, 2, null), Carne, IsAmigos);
    	return A.SacarDeEspera(Carne, IsAmigos);
    }
    
    @POST
    @Path("IniciarViaje")
    @Produces(MediaType.APPLICATION_JSON)
    public String IniciarViaje(@FormParam("Carne") String Carne) {
    	A.AgregarAmigo(Carne, "2018131313");
    	return A.RegistrarViaje(Carne, A.ConsultarAmigos(Carne));
    }
    
    
    
}
