/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serveur;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.QueryParam;
/**
 * REST Web Service
 *
 * @author PC_fixe_1
 */
@Path("Terminator")
public class TerminatorResource {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of TerminatorResource
     */
    public TerminatorResource() {
    }

    /**
     * Retrieves representation of an instance of serveur.TerminatorResource
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public String getXml() {
       return "dans getXml de RessourceCalculette";
    }

    /**
     * PUT method for updating or creating an instance of TerminatorResource
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_XML)
    public void putXml(String content) {
    }
    
    
    float[] vitesses = {4,3,2};
    
    
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/moteurs")
    public String moteursJSON(@QueryParam("t") float t){
        if (t<1000){
            return formatJSON2( 1,  1);
        }
        if (t==2000){
            return formatJSON2( 0,  1);
        }
        else{
            return formatJSON2(1, 1);
        }
    }
    
    
    
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/abscisse")
    public String abscisseJSON(@QueryParam("t") float t) {
       /* if (t<=10) {
		return 0;
        }
        if ((t>10) && (t<=15)) {
		return vitesses[1]*(t-10);
        }
        if ((t>15) && (t<=22)) {
		return vitesses[1]*5;
                     }
*/
        if ((t>22) && (t<=40)) {
		return formatJSON ("abcisse",vitesses[1]*5+vitesses[0]*(t-22));
        } /*
        float tf1=40+(vitesses[0] * 10+vitesses[2]*7)/vitesses[2];
	if ((t>40) && (t<=tf1)) {
		return vitesses[1]*5+vitesses[0]*(40-22);
        }
        float tf2=tf1+(vitesses[1]*5+vitesses[0]*(40-22))/vitesses[1];
	if ((t>tf1) && (t<=tf2)) {
		return vitesses[1]*5+vitesses[0]*(40-22)-vitesses[1]*(t-tf2);
        }*/
        else {
	System.out.println("erreur");
	
	return "bla";
        }
}
    
      @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/ordonnee")
    public float ordoneeJSON(@QueryParam("t") float t) {
        if (t<=10) {
		return  vitesses[0] * t;
        }
        if ((t>10) && (t<=15)) {
		return vitesses[0] * 10;
        }
        if ((t>15) && (t<=22)) {
		return vitesses[0] * 10+vitesses[2]*(t-15);
                        }
        if ((t>22) && (t<=40)) {
		return vitesses[0] * 10+vitesses[2]*7;
        }
        float tf1=40+(vitesses[0] * 10+vitesses[2]*7)/vitesses[2];
	if ((t>40) && (t<=tf1)) {
		return  vitesses[0] * 10+vitesses[2]*7-vitesses[2]*(t-40);
        }
        float tf2=tf1+(vitesses[1]*5+vitesses[0]*(40-22))/vitesses[1];
	if ((t>tf1) && (t<=tf2)) {
		return 0;
        }
        else {
	System.out.println("erreur");
	
	return 10100000;
        }
}
    protected String formatJSON(String nom,  float resultat) {
        String json = "{";
        json += "\"" + nom + "\":" + resultat + "}";
 
        return json;
    }
     protected String formatJSON2(int moteurdroit,   int moteurgauche) {
        String json = "{\n";
        json += "\t\t\"droit\": \"" + moteurdroit + "\",\n";
        json += "\t\t\"gauche\": \"" + moteurgauche + "\",\n";
        json += "}";
        return json;
    }
    

}
