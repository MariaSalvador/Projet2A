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
        system.out.println("je suis la")
    }

    /**
     * Retrieves representation of an instance of serveur.TerminatorResource
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public String getXml() {
       return "dans getXml de TerminatorRessource";
    }

    /**
     * PUT method for updating or creating an instance of TerminatorResource
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_XML)
    public void putXml(String content) {
    }
    

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/orientation")
    public String orientationJSON(){

            return formatJSON("orientation", -1);
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
