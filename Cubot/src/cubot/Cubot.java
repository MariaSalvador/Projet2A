




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

    
    
    public static void main(String[] args) {
        new Cubot();
    }
    
    public Cubot(){
		
                
		System.out.println("Program started");
		
                
                       
                       
                       
                       
                       connexionclient() ;
                       
                       
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
                
                
        
        
        
        
        
        
        protected static int connexionclient() {
        			// Connexion au serveur simulant le robot théorique
                        try {
                                 URL url = new URL("http://localhost:8080/REST_Terminator/webresources/Terminator/orientation");
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

