/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cubot2;

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
public class Cubot2 {
     
     
    
            
    public static void main(String[] args) {
        
         new Cubot2();
    }
    
    public Cubot2(){
        
        float minSpeed=0.87F;
        float maxSpeed=5.23F;
        float speed=(minSpeed+maxSpeed)*0.5F; ///vitesses des moteurs des roues
       
        float temps = 0;  ///variable pour vérifier à intervalles réguliers l'orientation du robot
        
         float backUntilTime=-1;  
        

            connexionclient(); 
            
            double[] posB = getPositionDestination(); //coordonnées x,y,z du point objectif

            while (getTime()<500){ //simulation de temps 500

                       if (ProximitySensor() == "true") // si obstacle detecté
                       
                       { backUntilTime = getTime()+4;}
                       
                       if (backUntilTime >getTime()) // On tourne
                       { temps=getTime()+10;
                       
                           setVitesseMoteur("gauche",speed/8);
                           setVitesseMoteur("droit",-speed/8);
                       }
                           
                      else 
                       {
                           if (getTime()>temps) // on se remet dans l'orientation de la droite passant entre le point A et B
                           {   
                           setVitesseMoteur("gauche",0);
                           setVitesseMoteur("droit",0); 
                           double orien = getOrientation();
                         double orientationvoulue = Math.toDegrees(-getTrajectoire( getPositionBubbleRob(), getPositionDestination()));
                           orien=Math.toDegrees(orien);   
                               if ((orien <orientationvoulue + 2 ) && (orien> orientationvoulue - 2))
                               {   System.out.println(orientationvoulue);
                                   System.out.println(orien);
                                   temps = getTime()+10;}
                            
                               //il tourne sur lui meme
                               
                               if (Math.abs(orientationvoulue - orien)<Math.abs(360 -orientationvoulue+orien)){

                                    if (orientationvoulue-orien>0){
                                    //il tourne vers la gauche
                                    setVitesseMoteur("gauche",-speed/8);
                                    setVitesseMoteur("droit",speed/8);}
                                    else {
                                    //il tourne vers la droite
                                    setVitesseMoteur("gauche",speed/8);
                                    setVitesseMoteur("droit",-speed/8);
                                    }
                               }
                               else{
                               if (360 -orientationvoulue+orien>0){
                                // il tourne vers la gauche
                                setVitesseMoteur("gauche",-speed/8);
                                setVitesseMoteur("droit",speed/8);
                               
                               }
                               else{
                                 //il tourne vers la droite
                                 setVitesseMoteur("gauche",speed/8);
                                 setVitesseMoteur("droit",-speed/8);}
                                
                               }  
                          
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
        			// Connexion au serveur qui permet de se connecter avec V-REP
                        try {
                              
                                 URL url = new URL("http://localhost:8080/REST_Terminator/webresources/Terminator/connexionVREP");
                                    HttpURLConnection connexion = (HttpURLConnection) url.openConnection();
                                    connexion.setRequestProperty("User-Agent", "");
                                    connexion.connect();
                                    InputStream streamReponse = connexion.getInputStream();
                                    BufferedReader bufferReception = new BufferedReader(new InputStreamReader(streamReponse));
                                    System.out.println("Program started");
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
protected static double[] getPositionBubbleRob(){
     try {
            URL url = new URL("http://localhost:8080/REST_Terminator/webresources/Terminator/getPositionBubbleRob");
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
               double answer1 = jObject.getDouble("x");
               double answer2 = jObject.getDouble("y");
               connexion.disconnect();
               double answer[]={answer1,answer2};
               return answer;

     }
      catch (Exception ex) {
                  ex.printStackTrace();
                  double[] answer = {110,110};
                   return answer;
                        }  
}

protected static double[] getPositionDestination(){
     try {
            URL url = new URL("http://localhost:8080/REST_Terminator/webresources/Terminator/getPositionDestination");
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
               double answer1 = jObject.getDouble("x");
               double answer2 = jObject.getDouble("y");
               connexion.disconnect();
               double answer[]={answer1,answer2};
               return answer;

     }
      catch (Exception ex) {
                  ex.printStackTrace();
                  double[] answer = {110,110};
                   return answer;
                        }  
}
protected static double getTrajectoire(double[] pointA, double[] pointB) {
   
  double angle = Math.atan(Math.abs(pointB[1] - pointA[1])/Math.abs(pointB[0] - pointA[0]));
  return angle;
}
}
