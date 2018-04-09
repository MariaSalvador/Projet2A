package serveur;

import coppelia.BoolW;
import coppelia.FloatWA;
import coppelia.IntW;
import coppelia.IntWA;
import coppelia.remoteApi;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.core.MediaType;
import org.json.JSONObject;
/**
 * REST Web Service
 *
 * @author PC_fixe_1
 */
@Path("Terminator")
public class TerminatorResource {
    
    protected int clientID;
    int answer;
    IntWA objectHandles = new IntWA(0);
    IntW handle_MoteurDroit = new IntW(0);
    IntW handle_MoteurGauche = new IntW(0);
    IntW handle_Sensor = new IntW(0);
    IntW handle_bubbleRob= new IntW(0);
    FloatWA Angles0 = new FloatWA(3); //angles voulus
    FloatWA Angles = new FloatWA(3); //angles instantannés
    float minSpeed=0.87F;
    float maxSpeed=5.23F;
    float backUntilTime=-1; 
    float speed=(minSpeed+maxSpeed)*0.5F;
    BoolW result =new BoolW(false);
    int codeRetour;
    float temps;
    remoteApi vrep = new remoteApi();

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
    @Path("/getSimulationTime")
    private String getSimulationTime(){
        
        FloatWA outFloats= new FloatWA(1);

		int result=vrep.simxCallScriptFunction(clientID,"bubbleRob",remoteApi.sim_scripttype_childscript, "getSimulationTime_function",null,null,null,null,null,outFloats,null,null,remoteApi.simx_opmode_blocking);
		if (result==remoteApi.simx_return_ok) {
			float returnValue = outFloats.getArray()[0];
			return formatJSON("time", returnValue);
		} else {
			System.out.println("Remote function call to simGetSimulationTime failed : " + codeRetourDescription(result));
			return formatJSON("time", -1);
		}

           
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/connexionVREP")
    public String connexionVREP() {
        			// Connexion a VREP
                            //vrep = new remoteApi();
                           /* vrep.simxFinish(-1);
                            int answer = 0;
                            int clientID = vrep.simxStart("127.0.0.1", 19999, true, true, 5000, 5);
                            if (clientID != -1) {
                                System.out.println("Connected to remote API server");
                            
                                answer = 1;
                                
                            }*/
                            return(formatJSON("ConnexionVrep",connexionVrep()));
	}
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/initVariables")
    private void initVariables() {
        			// initialisation des variables
                        codeRetour = vrep.simxGetObjects(clientID, vrep.sim_handle_all, objectHandles, vrep.simx_opmode_blocking);
			if (codeRetour == remoteApi.simx_return_ok)
				System.out.format("Number of objects in the scene: %d\n", objectHandles.getArray().length);
			else
				System.out.format("simxGetObjects call returned with error code: " + codeRetourDescription(codeRetour));
                        
                        codeRetour = vrep.simxGetObjectHandle(clientID, "bubbleRob_rightMotor", handle_MoteurDroit, remoteApi.simx_opmode_blocking);
			if (codeRetour == remoteApi.simx_return_ok)
				System.out.println("handle MoteurDroit = " + handle_MoteurDroit.toString());

			codeRetour = vrep.simxGetObjectHandle(clientID, "bubbleRob_leftMotor", handle_MoteurGauche, remoteApi.simx_opmode_blocking);
			if (codeRetour == remoteApi.simx_return_ok)
				System.out.println("handle MoteurGauche = " + handle_MoteurGauche.toString());
                       
                        codeRetour = vrep.simxGetObjectHandle(clientID, "bubbleRob_sensingNose", handle_Sensor, remoteApi.simx_opmode_blocking);
			if (codeRetour == remoteApi.simx_return_ok)
				System.out.println("handle Sensor = " + handle_Sensor.toString());
                        
                        codeRetour = vrep.simxGetObjectHandle(clientID, "bubbleRob", handle_bubbleRob, remoteApi.simx_opmode_blocking);
			if (codeRetour == remoteApi.simx_return_ok)
				System.out.println("handle bubbleRob = " + handle_bubbleRob.toString());

			try {
				Thread.sleep(2000);
			} catch (InterruptedException ex) {
				Thread.currentThread().interrupt();
			}
                        vrep.simxGetObjectOrientation(clientID,handle_bubbleRob.getValue(),-1,Angles0, remoteApi.simx_opmode_streaming);
                            
	}
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/codeRetourDescription")
    private static String codeRetourDescription(int codeRetour) {
		switch (codeRetour) {
		case remoteApi.simx_return_ok:
			return "0:\tThe function executed fine.";
		case remoteApi.simx_return_novalue_flag:
			return "1 (i.e. bit 0):\tnovalue_flag \t"
					+ "There is no command reply in the input buffer. This should not always be considered as an error, depending on the selected operation mode.";
		case remoteApi.simx_return_timeout_flag:
			return "2 (i.e. bit 1):\ttimeout_flag \t"
					+ "The function timed out (probably the network is down or too slow).";
		case remoteApi.simx_return_illegal_opmode_flag:
			return "4 (i.e. bit 2):\tillegal_opmode_flag \t"
					+ "The specified operation mode is not supported for the given function.";
		case remoteApi.simx_return_remote_error_flag:
			return "8 (i.e. bit 3):\tremote_error_flag \t"
					+ "The function caused an error on the server side (e.g. an invalid handle was specified).";
		case remoteApi.simx_return_split_progress_flag:
			return "16 (i.e. bit 4):\tsplit_progress_flag \t"
					+ "The communication thread is still processing previous split command of the same type.";
		case remoteApi.simx_return_local_error_flag:
			return "32 (i.e. bit 5):\tlocal_error_flag \t" + "The function caused an error on the client side.";
		case remoteApi.simx_return_initialize_error_flag:
			return "64 (i.e. bit 6):\tinitialize_error_flag \t" + "simxStart was not yet called.";
		default:
			return codeRetour + ": code de retour inconnu";
		}

	}
    /*
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/connexionclient")
    protected String connexionclient() {
        			// Connexion au serveur simulant le robot théorique
                        try {
                                 URL url = new URL("http://localhost:8084/REST_Terminator/webresources/Terminator/orientationJSON");
                                    HttpURLConnection connexion = (HttpURLConnection) url.openConnection();
                                    connexion.setRequestProperty("User-Agent", "");
                                    connexion.connect();
                                    InputStream streamReponse = connexion.getInputStream();
                                    BufferedReader bufferReception = new BufferedReader(new InputStreamReader(streamReponse));
                                    String ligne = "";
                                    StringBuilder reponse = new StringBuilder();
                                    while ((ligne = bufferReception.readLine()) != null) {
                                           reponse.append(ligne + "\n");
                                     }
                                    JSONObject jObject = new JSONObject(reponse.toString());
                                    int answer1 = jObject.getInt("orientation");
                                    System.out.println("Connecté au client localhost... !"); 
                                    System.out.println("orientation = " + answer1);   
                                    connexion.disconnect();
                                    
                                    return formatJSON("connécté au client, orientation : ", answer1);
                                    
                            } 
                        
                        catch (Exception ex) {
                                            ex.printStackTrace();
                                            int answer = 0;
                                            return formatJSON("pas connécté au client, orientation", -1);
                        }  
        }*/
    
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/orientationJSON")
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
     
     public int connexionVrep() {
        			// Connexion a VREP
                            vrep = new remoteApi();
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
