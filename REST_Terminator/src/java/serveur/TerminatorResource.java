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
import javax.ws.rs.QueryParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.core.MediaType;
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
    IntW handle_destination = new IntW(0);
    IntW handle_MoteurDroit = new IntW(0);
    IntW handle_MoteurGauche = new IntW(0);
    IntW handle_Sensor = new IntW(0);
    IntW handle_bubbleRob= new IntW(0);
    FloatWA Angles = new FloatWA(3); 
    FloatWA positionBubbleRob = new FloatWA(3); 
    FloatWA pointB = new FloatWA(3); 

    int codeRetour;
    
    remoteApi vrep = new remoteApi();

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of TerminatorResource
     */
    public TerminatorResource() {
    }


    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/connexionVREP")
    public int connexionVREP() {
        // Connexion a VREP
        //Returns le Client ID
        vrep.simxFinish(-1); //Fermeture d'autres possibles connexions précédentes
        int clientID = vrep.simxStart("127.0.0.1", 19999, true, true, 5000, 5);
        if (clientID != -1) {
            vrep.simxAddStatusbarMessage(clientID,"HELLO VREP",remoteApi. simx_opmode_oneshot);    //Ecrit sur la console V-REP
        }
        return clientID;
	}
    
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/getSimulationTime")
    public String getSimulationTime(){
        //Renvoie le temps dans la simulation V-REP sous format JSON
        //Si erreur, renvoie -1 en format JSON
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
    @Path("/getOrientation")
    public String getOrientation(){
        //renvoie l'angle gamma des angles d'Euler de BubbleRob par rapport aux axes V-REP sous format JSON
            vrep.simxGetObjectHandle(clientID, "bubbleRob", handle_bubbleRob, remoteApi.simx_opmode_blocking);   
            vrep.simxGetObjectOrientation(clientID,handle_bubbleRob.getValue(),-1,Angles, remoteApi.simx_opmode_streaming);
            double orientation =  Angles.getArray()[2];
            return formatJSON4("orientation", orientation);
    }
 
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/ProximitySensor")
    public String proximitysensor(){
        //Renvoie la valeur fournie par le SensorDetector de BubbleRob avec format JSON
        //Renvoie un Boolen dans un JSON format (FALSE si pas d'obsstacle, True sinon)
            BoolW result =new BoolW(false);
            vrep.simxGetObjectHandle(clientID, "bubbleRob_sensingNose", handle_Sensor, remoteApi.simx_opmode_blocking);
            vrep.simxReadProximitySensor(clientID, handle_Sensor.getValue(),result, null, null, null, remoteApi.simx_opmode_streaming);
            return formatJSON3("objetdetecte", result.getValue());
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/setMotor")
    public int  setspeedmotor(@QueryParam("moteur") String moteur, @QueryParam("valeur") float valeur){
        //Return 1 tourjours
        //Change les valeurs des moteurs de BubbleRob
            if ("gauche".equals(moteur)){
                vrep.simxGetObjectHandle(clientID, "bubbleRob_leftMotor", handle_MoteurGauche, remoteApi.simx_opmode_blocking);
                vrep.simxSetJointTargetVelocity(clientID, handle_MoteurGauche.getValue(), valeur, remoteApi.simx_opmode_oneshot);
                }
            else {
                vrep.simxGetObjectHandle(clientID, "bubbleRob_rightMotor", handle_MoteurDroit, remoteApi.simx_opmode_blocking);
                vrep.simxSetJointTargetVelocity(clientID, handle_MoteurDroit.getValue(), valeur, remoteApi.simx_opmode_blocking);}
            return 1;
        }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/getPositionDestination")
    public String PositionDestination(){
            //Renvoie les coordonnées [x, y] de la destination de BubbleRob en format JSON
            //La destination doit s'appeler POINT_B sur la scène V-REP !
            vrep.simxGetObjectHandle(clientID, "POINT_B", handle_destination, remoteApi.simx_opmode_blocking);
            vrep.simxGetObjectPosition(clientID, handle_destination.getValue(),-1, pointB, remoteApi.simx_opmode_streaming);
            double x =  pointB.getArray()[0];
            double y = pointB.getArray()[1];
            return formatJSON2(x, y);
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/getPositionBubbleRob")
    public String PositionBubbleRob(){
            //Renvoie les coordonnées [x, y] de BubbleRob en format JSON
            vrep.simxGetObjectHandle(clientID, "bubbleRob", handle_bubbleRob, remoteApi.simx_opmode_blocking);
            vrep.simxGetObjectPosition(clientID, handle_bubbleRob.getValue(),-1, positionBubbleRob, remoteApi.simx_opmode_streaming);
            double x =  positionBubbleRob.getArray()[0];
            double y = positionBubbleRob.getArray()[1];
            return formatJSON2(x, y);
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


    
    protected String formatJSON(String nom,  float resultat) {
        //Format JSON avec un float
        //On peut passer en paramètre le nom et le float associé
        String json = "{";
        json += "\"" + nom + "\":" + resultat + "}";
        return json;
    }
     protected String formatJSON2(double x,   double y) {
        //Format JSON avec deux doubles [x,y]
        String json = "{\n";
        json += "\t\t\"x\": \"" + x + "\",\n";
        json += "\t\t\"y\": \"" + y+ "\",\n";
        json += "}";
        return json;
       
    }
     
     protected String formatJSON3(String nom,  Boolean resultat) {
        //Format JSON avec un Booléan
        //On peut passer en paramètre le nom et le Booléan associé
        String json = "{";
        json += "\"" + nom + "\":" + resultat + "}";
 
        return json;
    }
     
     protected String formatJSON4(String nom,  double resultat) {
         //Format JSON avec un double
        //On peut passer en paramètre le nom et le Booléan associé
        String json = "{";
        json += "\"" + nom + "\":" + resultat + "}";
        return json;
    }
}
