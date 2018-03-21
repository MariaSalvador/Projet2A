import coppelia.IntW;
import coppelia.IntWA;
import coppelia.FloatWA;
import coppelia.remoteApi;
import coppelia.BoolW;




import java.io.*;
import java.net.URL;
import java.net.MalformedURLException;
import java.lang.Object;
import java.net.URLConnection;
import java.net.HttpURLConnection;
import org.json.JSONObject;
import org.json.JSONArray;


/**
 *
 * @author PC_fixe_1
 */

public class Cubot {

    
    protected remoteApi vrep;
    protected int clientID;
    
    public static void main(String[] args) {
        new Cubot();
    }
    
    public Cubot(){
		
                
		System.out.println("Program started");
		
		vrep = new remoteApi();
		vrep.simxFinish(-1); // just in case, close all opened connections
		
		int clientID = vrep.simxStart("127.0.0.1", 19999, true, true, 5000, 5);
		if (clientID != -1) {
			System.out.println("Connected to remote API server");

                        
                        
                        // On nomme tout les composants
                        
                        
			IntWA objectHandles = new IntWA(0);
			int codeRetour = vrep.simxGetObjects(clientID, vrep.sim_handle_all, objectHandles, vrep.simx_opmode_blocking);
			if (codeRetour == remoteApi.simx_return_ok)
				System.out.format("Number of objects in the scene: %d\n", objectHandles.getArray().length);
			else
				System.out.format("simxGetObjects call returned with error code: " + codeRetourDescription(codeRetour));

			
			IntW handle_MoteurDroit = new IntW(0);
			IntW handle_MoteurGauche = new IntW(0);
			IntW handle_Sensor = new IntW(0);
                        IntW handle_bubbleRob= new IntW(0);
                        
                        
                        
                        
                        
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
                        
                        
                        
                        
                       
                       FloatWA Angles0 = new FloatWA(3); //angles voulus
                       FloatWA Angles = new FloatWA(3); //angles instantannés
                       float minSpeed=0.87F;
                       float maxSpeed=5.23F;
                       float backUntilTime=-1; 
                       float speed=(minSpeed+maxSpeed)*0.5F;
                       vrep.simxGetObjectOrientation(clientID,handle_bubbleRob.getValue(),-1,Angles0, remoteApi.simx_opmode_streaming);
                       BoolW result =new BoolW(false);
                       float temps=0;
                       
                       float orientationvoulue = connexionclient() ;
                       
                       
                       
                       
                       while (getSimulationTime()<500){
                       codeRetour = vrep.simxReadProximitySensor(clientID, handle_Sensor.getValue(),result, null, null, null, remoteApi.simx_opmode_streaming);
                       
                       if (result.getValue() == true) // si obstacle detecté
                       { backUntilTime = getSimulationTime()+4;}
                       
                       if (backUntilTime >getSimulationTime()) // On tourne
                       { temps=getSimulationTime()+10;
                           vrep.simxSetJointTargetVelocity(clientID, handle_MoteurGauche.getValue(), speed/8, remoteApi.simx_opmode_blocking);
                           vrep.simxSetJointTargetVelocity(clientID, handle_MoteurDroit.getValue(), -speed/8, remoteApi.simx_opmode_blocking);
                       }
                           
                      else 
                       {
                           if (getSimulationTime()>temps) // on se remet dans l'axe de angles0
                           {
                              vrep.simxGetObjectOrientation(clientID,handle_bubbleRob.getValue(),-1,Angles, remoteApi.simx_opmode_streaming);
                             

                               if ((Angles.getArray()[2] <orientationvoulue + 0.1 ) && (Angles.getArray()[2]> orientationvoulue - 0.1))
                               {   temps = getSimulationTime()+10;}
                            
                               //il tourne sur lui meme
                            vrep.simxSetJointTargetVelocity(clientID, handle_MoteurDroit.getValue(), speed/8, remoteApi.simx_opmode_blocking);
                            vrep.simxSetJointTargetVelocity(clientID, handle_MoteurGauche.getValue(), -speed/8, remoteApi.simx_opmode_blocking); 
                           }
                           
                           else
                           {
                               //il va tout droit
                             vrep.simxSetJointTargetVelocity(clientID, handle_MoteurGauche.getValue(), speed, remoteApi.simx_opmode_blocking); 
                             vrep.simxSetJointTargetVelocity(clientID, handle_MoteurDroit.getValue(), speed, remoteApi.simx_opmode_blocking);

                           }
                       }
                           
                           
                           
                                       
                       }

			System.out.println("fin bLAAAAAAAAAAA");
			// Now send some data to V-REP in a non-blocking fashion:
			vrep.simxAddStatusbarMessage(clientID, "Hello V-REP!", remoteApi.simx_opmode_oneshot);

			// Before closing the connection to V-REP, make sure that the last command sent
			// out had time to arrive. You can guarantee this with (for example):
			IntW pingTime = new IntW(0);
			vrep.simxGetPingTime(clientID, pingTime);

			// Now close the connection to V-REP:
			vrep.simxFinish(clientID);
		} else
			System.out.println("Failed connecting to remote API server");
		System.out.println("Program ended");
                
                
        
	}
        private float getSimulationTime() {
		
		FloatWA outFloats= new FloatWA(1);

		int result=vrep.simxCallScriptFunction(clientID,"bubbleRob",remoteApi.sim_scripttype_childscript, "getSimulationTime_function",null,null,null,null,null,outFloats,null,null,remoteApi.simx_opmode_blocking);
		if (result==remoteApi.simx_return_ok) {
			float returnValue = outFloats.getArray()[0];
			return returnValue;
		} else {
			System.out.println("Remote function call to simGetSimulationTime failed : " + codeRetourDescription(result));
			return -1;
		}	

	}
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
        
        
        
        protected static int connexionclient() {
        			// Connexion au serveur simulant le robot théorique
                        try {
                                 URL url = new URL("http://localhost:8084/REST_Terminator/webresources/Terminator/orientation");
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
                                    return answer1;
                                    
                            } 
                        
                        catch (Exception ex) {
                                            ex.printStackTrace();
                                            int answer = 0;
                                            return answer;
                        }  
        }
}

