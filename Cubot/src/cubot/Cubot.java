/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cubot;

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
 * @author User
 */
public class Cubot {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        // TODO code application logic here
         new Cubot();
    }
    
    public Cubot(){
        
        float minSpeed=0.87F;
        float maxSpeed=5.23F;
        float backUntilTime=-1; 
        float speed=(minSpeed+maxSpeed)*0.5F;
        float temps = 0;
        int orientationvoulue = -1;
		
                
            System.out.println("Program started");
            System.out.println("Connecté a vrep" + connexionclient());
            System.out.println(getOrientation());
                
                
            while (getTime()<500){
                       if (ProximitySensor() == "true") // si obstacle detecté
                       
                       { backUntilTime = getTime()+4;}
                       
                       if (backUntilTime >getTime()) // On tourne
                       { temps=getTime()+10;
                           setVitesseMoteur("gauche",speed/8);
                           setVitesseMoteur("droit",-speed/8);
                       }
                           
                      else 
                       {
                           if (getTime()>temps) // on se remet dans l'axe de angles0
                           {        
                               if ((getOrientation() <orientationvoulue + 0.1 ) && (getOrientation()> orientationvoulue - 0.1))
                               {   temps = getTime()+10;}
                            
                               //il tourne sur lui meme
                                setVitesseMoteur("gauche",-speed/8);
                                setVitesseMoteur("droit",speed/8); 
                          
                           }
                           
                           else
                           {
                               //il va tout droit
                             setVitesseMoteur("gauche",speed);
                           setVitesseMoteur("droit",speed); 
                                            }
                       
                       }
                          

			System.out.println("fin d'un cycle");
    }
    }
 protected static int connexionclient() {
        			// Connexion au serveur simulant le robot théorique
                        try {
                                 URL url = new URL("http://localhost:8080/REST_Terminator/webresources/Terminator/connexionVREP");
                                    HttpURLConnection connexion = (HttpURLConnection) url.openConnection();
                                    connexion.setRequestProperty("User-Agent", "");
                                    connexion.connect();
                                    InputStream streamReponse = connexion.getInputStream();
                                    BufferedReader bufferReception = new BufferedReader(new InputStreamReader(streamReponse));
                                    
                                    return 1;
                                    
                            }
                        
                        catch (Exception ex) {
                                            ex.printStackTrace();
                                            int answer = 0;
                                            return answer;
                        }  
        }
 
  protected static int getTime() {
        			
                        try {
                                 URL url = new URL("http://localhost:8080/REST_Terminator/webresources/Terminator/getSimulationTime");
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
                                    int answer1 = jObject.getInt("time");  
                                    connexion.disconnect();
                                    return answer1;
                                    
                            } 
                        
                        catch (Exception ex) {
                                            ex.printStackTrace();
                                            int answer = 0;
                                            return answer;
                        }  
        }
        
  protected static double getOrientation() {
        			
                        try {
                                 URL url = new URL("http://localhost:8080/REST_Terminator/webresources/Terminator/getOrientation");
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
                                    Double answer1 = jObject.getDouble("orientation");
                                    connexion.disconnect();
                                    return answer1;
                                    
                            } 
                        
                        catch (Exception ex) {
                                            ex.printStackTrace();
                                            double answer = 110;
                                            return answer;
                        }  
        }
  
  
protected static String ProximitySensor() {
        			
                        try {
                                 URL url = new URL("http://localhost:8080/REST_Terminator/webresources/Terminator/ProximitySensor");
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
                                    Boolean answer1 = jObject.getBoolean("objetdetecte");  
                                    connexion.disconnect();
                                    return answer1.toString();
                                    
                            } 
                        
                        catch (Exception ex) {
                                            ex.printStackTrace();
                                            return "il y a une erreur";
                        }  
        }

protected static int setVitesseMoteur(String moteur, float vitesse){
     try {
            URL url = new URL("http://localhost:8080/REST_Terminator/webresources/Terminator/setMotor?moteur=" + moteur +"&valeur=" + vitesse);
            HttpURLConnection connexion = (HttpURLConnection) url.openConnection();
            connexion.setRequestProperty("User-Agent", "");
            connexion.connect();
            InputStream streamReponse = connexion.getInputStream();
            BufferedReader bufferReception = new BufferedReader(new InputStreamReader(streamReponse));                      
            connexion.disconnect();    
            return 1;
}
    catch (Exception ex) {
        ex.printStackTrace();
        return 0;}  
}
}
