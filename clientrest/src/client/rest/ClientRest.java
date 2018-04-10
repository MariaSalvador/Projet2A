/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.rest;

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
public class ClientRest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        System.out.println("Program started");
                       
       System.out.println(connexionclient()) ;
    }
    
    
    
    protected static String connexionclient() {
        			// Connexion au serveur simulant le robot théorique
                        try {
                                 URL url = new URL("http://localhost:8080/REST_Terminator/webresources/Terminator/connexionVREP");
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
                                   // JSONObject jObject = new JSONObject(reponse.toString());
                                   // int answer1 = jObject.getInt("orientation");
                                    System.out.println("Connecté au client localhost... !");  
                                    connexion.disconnect();
                                    return reponse.toString();
                                    
                            } 
                        
                        catch (Exception ex) {
                                            ex.printStackTrace();
                                            String answer = "Erreur";
                                            return answer;
                        }  
        }
    
}
