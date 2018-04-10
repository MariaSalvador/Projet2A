/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serveur;

import coppelia.IntW;
import coppelia.IntWA;
import coppelia.FloatWA;
import coppelia.remoteApi;
import coppelia.BoolW;

import java.io.*;
import java.net.URL;
import java.net.HttpURLConnection;
import org.json.JSONObject;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.core.MediaType;

/**
 * REST Web Service
 *
 * @author User
 */
@Path("Robot")
public class RessourceRobot {
    

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of RessourceRobot
     */
    public RessourceRobot() {
    }

    /**
     * Retrieves representation of an instance of serveur.RessourceRobot
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public String getXml() {
        //TODO return proper representation object
        return "dans getXml de RessourceCalculette";
    }

    /**
     * PUT method for updating or creating an instance of RessourceRobot
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_XML)
    public void putXml(String content) {
        
    }
    
    @GET
    @Path("/initsimul")
    public int initsimul(){
       // protected remoteApi vrep;
       // protected int clientID;
        return connexionVREP();//on se connecte a vrep
    }
    
    private int connexionVREP() {
        			// Connexion a VREP
    remoteApi vrep = new remoteApi();
    vrep.simxFinish(-1);
    int answer = 0;
    int clientID = vrep.simxStart("127.0.0.1", 19999, true, true, 5000, 5);
    if (clientID != -1) {
    System.out.println("Connected to remote API server");
    answer = 1;     
    }
    return(answer);
	}
    
   
}


