package ServidorTEC;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
	DateFormat hourdateFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
	
	@GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getIt() {
    	return "Hola";
    }
	
    @GET
    @Path("Mapa")//
    @Produces(MediaType.APPLICATION_JSON)
    public String Mapa() {
    	Grafo G=new Grafo(31);
    	G.AdyacenciafromXML();
    	Date date = new Date();
    	System.out.print(hourdateFormat.format(date)+": ");
    	System.out.println("Se envia el mapa");
    	return G.MatrizToJson();
    }

    @POST
    @Path("NuevoAmigo")//
    @Produces(MediaType.APPLICATION_JSON)
    public String NuevoAmigo(@FormParam("Conductor") String CarneConductor,@FormParam("Amigo") String CarneAmigo) {
    	Date date = new Date();
    	System.out.print(hourdateFormat.format(date)+": ");
    	System.out.println("Se agrega a "+CarneAmigo+" como un amigo de "+CarneConductor);
    	return A.AgregarAmigo(CarneConductor, CarneAmigo);
    }
    
    @POST
    @Path("Residencia")//
    @Produces(MediaType.APPLICATION_JSON)
    public void Residencia(@FormParam("Carne") String Carne,@FormParam("Residencia") String Residencia) {
    	Date date = new Date();
    	System.out.print(hourdateFormat.format(date)+": ");
    	System.out.println("Residencia "+Residencia+" de "+Carne);
    	A.GuardarResidencia(Carne, Residencia);
    }
    
    @GET
    @Path("Top5")//
    @Produces(MediaType.APPLICATION_JSON)
    public String Top5() {
    	Date date = new Date();
    	System.out.print(hourdateFormat.format(date)+": ");
    	System.out.println("Se envia el top");
    	return A.Top5();
    }
    
    @POST
    @Path("Carne")//
    @Produces(MediaType.APPLICATION_JSON)
    public String Carne(@FormParam("Carne") String Carne) {
    	Date date = new Date();
    	System.out.print(hourdateFormat.format(date)+": ");
    	System.out.println("Se agrega el carne "+Carne);
    	Almacenador A=new Almacenador();
    	return A.GuardarCarne(Carne);
    }
    
    @POST
    @Path("RutaSolo")//
    @Produces(MediaType.APPLICATION_JSON)
    public String Ruta(
    		@FormParam("Residencia") String Residencia,
    		@FormParam("Carne") String Carne,
    		@FormParam("Asientos") String Asientos) {
    	
    	Grafo G=new Grafo(31);
    	G.ConsultarCaminoAmigos(Integer.parseInt(Residencia),0, null) ;
    	LinkedList<String> ListaAmigos=A.ConsultarEnEspera(G.getMejorUltimaRuta(), "", "0",Asientos);
    	LinkedList<Integer> ListaPos=A.getPosGenteEnEspera();
    	A.RegistrarViaje(Carne, ListaAmigos,ListaPos, ""+G.getMejorUltimaRuta().get(0),"1");
    	A.SumarViaje(Carne);
    	Gson gson=new Gson();
    	Date date = new Date();
    	System.out.print(hourdateFormat.format(date)+": ");
    	System.out.println("Se inicia el viaje solo de "+Carne+" junto a "+ListaAmigos.toString()+" por la ruta "+ListaPos.toString());
    	String json="{\"Tiempos\":"+gson.toJson(G.getTiempos())+
    			", \"Ruta\":"+gson.toJson(G.getMejorUltimaRuta())+
    			", \"Pasajeros\":"+gson.toJson(ListaAmigos)+
    			", \"PosicionesPasajeros\":"+gson.toJson(ListaPos)+
    			", \"Conductor\":"+gson.toJson(Carne)+
    			"}";
    	for (int i=0;i<ListaAmigos.size();i++) {
    		A.SacarDeEspera(json,ListaAmigos.get(i).substring(1), "0");
    	}
    	return json;
    }
    
    @POST
    @Path("RutaAmigo")//
    @Produces(MediaType.APPLICATION_JSON)
    public String RutaAmigo(
    		@FormParam("Residencia") String Residencia,
    		@FormParam("Carne") String Carne,
    		@FormParam("Asientos") String Asientos) {
    	System.out.println(Residencia);
    	Almacenador A=new Almacenador(); 
    	LinkedList<String> ListaAmigos=A.ConsultarEnEspera(null, Carne, "1",Asientos);
    	LinkedList<Integer> ListaPos=A.getPosGenteEnEspera();
    	
    	Grafo G=new Grafo(31);
    	G.ConsultarCaminoAmigos(Integer.parseInt(Residencia),0, ListaPos) ;
    	A.RegistrarViaje(Carne, ListaAmigos,ListaPos, ""+G.getMejorUltimaRuta().get(0),"1");
    	A.SumarViaje(Carne);
    	Gson gson=new Gson();
    	Date date = new Date();
    	System.out.print(hourdateFormat.format(date)+": ");
    	System.out.println("Se inicia el viaje con amigos de "+Carne+" junto a "+ListaAmigos.toString()+" por la ruta "+ListaPos.toString());
    	String json="{\"Tiempos\":"+gson.toJson(G.getTiempos())+
    			", \"Ruta\":"+gson.toJson(G.getMejorUltimaRuta())+
    			", \"Pasajeros\":"+gson.toJson(ListaAmigos)+
    			", \"PosicionesPasajeros\":"+gson.toJson(ListaPos)+
    			", \"Conductor\":"+gson.toJson(Carne)+
    			"}";
    	for (int i=0;i<ListaAmigos.size();i++) {
    		A.SacarDeEspera(json,ListaAmigos.get(i).substring(1), "1");
    	}
    	return json;
    }
    
    @POST
    @Path("SeguirEsperando")//
    @Produces(MediaType.APPLICATION_JSON)
    public String SeguirEsperando(@FormParam("SoloAmigos") String IsAmigo,@FormParam("Carne") String Carne) {
    	Date date = new Date();
    	System.out.print(hourdateFormat.format(date)+": ");
    	System.out.println("Se consulta por la espera de "+Carne);
    	return A.SeguirEsperando(Carne, IsAmigo);
    }
    
    @POST
    @Path("CerrarViaje")//
    @Produces(MediaType.APPLICATION_JSON)
    public String CerrarViaje(@FormParam("Carne") String Carne) {
    	Date date = new Date();
    	System.out.print(hourdateFormat.format(date)+": ");
    	System.out.println("Se cierra el viaje del conductor "+Carne);
    	return A.CerraViaje(Carne);
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
    	Date date = new Date();
    	System.out.print(hourdateFormat.format(date)+": ");
    	System.out.println("El viaje de "+Carne+"Se actualiza en "+Pos);
    	return json;
    }
    
    @POST
    @Path("Calificar")//
    @Produces(MediaType.APPLICATION_JSON)
    public String Calificar(@FormParam("Calificacion") String Calificacion,@FormParam("Carne") String Carne) {
    	Date date = new Date();
    	System.out.print(hourdateFormat.format(date)+": ");
    	System.out.println("Se califica a "+Carne+" con "+Calificacion);
    	return A.SumarCalificacion(Carne, Calificacion.substring(0,1));
    }
    
    @POST
    @Path("CalificacionPropia")//
    @Produces(MediaType.APPLICATION_JSON)
    public String Calificar(@FormParam("Carne") String Carne) {
    	Date date = new Date();
    	System.out.print(hourdateFormat.format(date)+": ");
    	System.out.println("Se consulta la calificacion de "+Carne);
    	if (Carne.equals("null")) {
    		System.out.println("null?");
    		return "0";
    	}
    	return ""+A.ConsultarCalificacionPromedio(Carne);
    }
    
    @POST
    @Path("Amigos")//
    @Produces(MediaType.APPLICATION_JSON)
    public String Amigos(@FormParam("Carne") String Carne) {
    	Date date = new Date();
    	System.out.print(hourdateFormat.format(date)+": ");
    	System.out.println("Se consultan los amigos de "+Carne);
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
    @Path("Espera")//
    @Produces(MediaType.APPLICATION_JSON)
    public String Espera(@FormParam("Carne") String Carne,
    		@FormParam("Residencia") String Residencia,
    		@FormParam("SoloAmigos") String IsAmigos) {
    	Date date = new Date();
    	System.out.print(hourdateFormat.format(date)+": ");
    	System.out.println("Se pone en espera a "+Carne+" en la residencia "+Residencia+",Quiero amigo="+IsAmigos);
    	return A.PonerEnEspera(Carne, Residencia, IsAmigos);
    }
    
    @POST
    @Path("SalirEspera")
    @Produces(MediaType.APPLICATION_JSON)
    public String SalirEspera(@FormParam("Carne") String Carne,
    		@FormParam("SoloAmigos") String IsAmigos) {
    	Date date = new Date();
    	System.out.print(hourdateFormat.format(date)+": ");
    	System.out.println("Se se saca de espera a "+Carne);
    	return A.SacarDeEspera("1",Carne, IsAmigos);
    }
    
}
