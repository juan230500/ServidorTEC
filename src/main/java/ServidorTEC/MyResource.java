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
    public String Ruta(
    		@FormParam("Residencia") String Residencia,
    		@FormParam("Carne") String Carne,
    		@FormParam("Asientos") String Asientos) {
    	System.out.println(Residencia);
    	Grafo G=new Grafo(31);
    	G.ConsultarCaminoAmigos(Integer.parseInt(Residencia),0, null) ;
    	LinkedList<String> ListaAmigos=A.ConsultarEnEspera(G.getMejorUltimaRuta(), "", "0",Asientos);
    	for (int i=0;i<ListaAmigos.size();i++) {
    		A.SacarDeEspera(Carne,ListaAmigos.get(i).substring(1), "0");
    	}
    	LinkedList<Integer> ListaPos=A.getPosGenteEnEspera();
    	A.RegistrarViaje(Carne, ListaAmigos,ListaPos, ""+G.getMejorUltimaRuta().get(0),"1");
    	Gson gson=new Gson();
    	String json="{\"Tiempos\":"+gson.toJson(G.getTiempos())+
    			", \"Ruta\":"+gson.toJson(G.getMejorUltimaRuta())+
    			", \"Pasajeros\":"+gson.toJson(ListaAmigos)+
    			", \"PosicionesPasajeros\":"+gson.toJson(ListaPos)+
    			"}";
    	return json;
    }
    
    @POST
    @Path("RutaAmigo")
    @Produces(MediaType.APPLICATION_JSON)
    public String RutaAmigo(
    		@FormParam("Residencia") String Residencia,
    		@FormParam("Carne") String Carne,
    		@FormParam("Asientos") String Asientos) {
    	System.out.println(Residencia);
    	Almacenador A=new Almacenador(); 
    	LinkedList<String> ListaAmigos=A.ConsultarEnEspera(null, Carne, "1",Asientos);
    	LinkedList<Integer> ListaPos=A.getPosGenteEnEspera();
    	for (int i=0;i<ListaAmigos.size();i++) {
    		A.SacarDeEspera(Carne,ListaAmigos.get(i).substring(1), "1");
    	}
    	Grafo G=new Grafo(31);
    	G.ConsultarCaminoAmigos(Integer.parseInt(Residencia),0, ListaPos) ;
    	A.RegistrarViaje(Carne, ListaAmigos,ListaPos, ""+G.getMejorUltimaRuta().get(0),"1");
    	Gson gson=new Gson();
    	String json="{\"Tiempos\":"+gson.toJson(G.getTiempos())+
    			", \"Ruta\":"+gson.toJson(G.getMejorUltimaRuta())+
    			", \"Pasajeros\":"+gson.toJson(ListaAmigos)+
    			", \"PosicionesPasajeros\":"+gson.toJson(ListaPos)+
    			"}";
    	return json;
    }
    
    @POST
    @Path("SeguirEsperando")
    @Produces(MediaType.APPLICATION_JSON)
    public String SeguirEsperando(@FormParam("SoloAmigos") String IsAmigo,@FormParam("Carne") String Carne) {
    	return A.SeguirEsperando(Carne, IsAmigo);
    }
    
    @POST
    @Path("ActualizarViaje")
    @Produces(MediaType.APPLICATION_JSON)
    public String ActualizarViaje(
    		@FormParam("Carne") String Carne,
    		@FormParam("Pos") String Pos) {
    	LinkedList<Integer> L=A.ActualizarViaje(Carne, Pos);
    	String json;
    	if (L==null) {
    		return "0";
    	}
    	if (L.size()==0) {
    		json="1";
    	}
    	else if (L.size()==1){
    		int NuevoInicio=L.get(0);;
        	Grafo G=new Grafo(31);
        	G.ConsultarCaminoAmigos(NuevoInicio,0, null) ;
        	Gson gson=new Gson();
        	json="{\"Tiempos\":"+gson.toJson(G.getTiempos())+
        			", \"Ruta\":"+gson.toJson(G.getMejorUltimaRuta())+
        			"}";;
    	}
    	else {
    		int NuevoInicio=L.get(0);
        	LinkedList<Integer> NuevosAmigos=new LinkedList<Integer>();
        	L.removeFirst();
        	for (int i=0;i<L.size();i++) {
        		NuevosAmigos.add(L.removeFirst());
        	}
        	Grafo G=new Grafo(31);
        	G.ConsultarCaminoAmigos(NuevoInicio,0, NuevosAmigos) ;
        	Gson gson=new Gson();
        	json="{\"Tiempos\":"+gson.toJson(G.getTiempos())+
        			", \"Ruta\":"+gson.toJson(G.getMejorUltimaRuta())+
        			"}";
    	}
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
    	return ""+A.ConsultarCalificacionPromedio(Carne);
    }
    
    @POST
    @Path("Amigos")
    @Produces(MediaType.APPLICATION_JSON)
    public String Amigos(@FormParam("Carne") String Carne) {
    	LinkedList<String> ListaAmigos=A.ConsultarAmigos(Carne);
    	LinkedList<Integer> ListaCalificacion=new LinkedList<Integer>();
    	for (int i=0;i<ListaAmigos.size();i++) {
    		ListaCalificacion.add(A.ConsultarCalificacionPromedio(ListaAmigos.get(i).substring(1)));
    	}
    	Gson gson=new Gson();
    	String json="{\"Amigos\":"+gson.toJson(ListaAmigos)+
    			", \"Calificaciones\":"+gson.toJson(ListaCalificacion)+
    			"}";
    	return json;
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
    	return A.SacarDeEspera("1",Carne, IsAmigos);
    }
}
