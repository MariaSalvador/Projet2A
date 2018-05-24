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
import java.lang.System;

/**
 *
 * @author User
 */
public class Cubot {

    public static void main(String[] args) {

        new Cubot();
    }

    public Cubot() {

        float minSpeed = 0.87F;
        float maxSpeed = 5.23F;
        float speed = (minSpeed + maxSpeed) * 0.5F; ///vitesses des moteurs des roues

        float backToAxisTime = 0;  ///variable pour vérifier à intervalles réguliers l'orientation du robot
        double[] posBubble;
        float backUntilTime = -1;
        double TimeEntry;
        double TimeOut;

        connexionclient();

        /*L'utilisateur a la possibilité de prendre comme destination le point qui s'appelle dans V-REP "POINT_B"
            sinon, il peut aussi donner les cordonnées lui même sous forme de double {x,y}
            double[] posB = getPositionDestination();
        
         */
        double[] posB = {+1.2000e+0, -2.0000e-1}; //coordonnées x,y,z du point objectif

        while (getTime() < 5000) { //simulation de temps 500

            /* ------------For Testing purpose only -------------
                TimeEntry = System.currentTimeMillis();
                TimeEntry = getTime();*/
            if (ProximitySensor() == "true") // si obstacle detecté     
            {
                backUntilTime = getTime() + 4;
            }

            if (backUntilTime > getTime()) // On tourne
            {
                backToAxisTime = getTime() + 10;

                setVitesseMoteur("gauche", speed / 8);
                setVitesseMoteur("droit", -speed / 8);
            } else {
                if (getTime() > backToAxisTime) // on se remet dans l'orientation de la droite passant entre le point A et B
                {
                    setVitesseMoteur("gauche", 0);
                    setVitesseMoteur("droit", 0);  // On arrete les 2 moteurs pour calculer l'angle, sinon le calcul est faux
                    double orien = getOrientation();
                    double orientationvoulue = Math.toDegrees(getTrajectoire(getPositionBubbleRob(), posB));
                    orien = Math.toDegrees(orien);   // transformation en degrées
                    if ((orien < orientationvoulue + 2) && (orien > orientationvoulue - 2)) // Si on est dans le bon axe, avec une erreur de 2 degrés
                    {
                        backToAxisTime = getTime() + 10;

                    }

                    //il tourne sur lui meme
                    //Plusieurs cas possibles pour que le Robot tourne de façon a perdre le moins de temps possible
                    if (Math.abs(orientationvoulue - orien) < Math.abs(360 - orientationvoulue + orien)) {

                        if (orientationvoulue - orien > 0) {
                            //il tourne vers la gauche
                            setVitesseMoteur("gauche", -speed / 8);
                            setVitesseMoteur("droit", speed / 8);
                        } else {
                            //il tourne vers la droite
                            setVitesseMoteur("gauche", speed / 8);
                            setVitesseMoteur("droit", -speed / 8);
                        }
                    } else {
                        if (360 - orientationvoulue + orien > 0) {
                            // il tourne vers la gauche
                            setVitesseMoteur("gauche", -speed / 8);
                            setVitesseMoteur("droit", speed / 8);

                        } else {
                            //il tourne vers la droite
                            setVitesseMoteur("gauche", speed / 8);
                            setVitesseMoteur("droit", -speed / 8);
                        }

                    }

                } else {
                    //il va tout droit
                    setVitesseMoteur("gauche", speed);
                    setVitesseMoteur("droit", speed);
                }

            }

            /*------------For testing purpose only----------------
                        TimeOut =  System.currentTimeMillis();
                        System.out.println(TimeWhile(TimeEntry, TimeOut));
                        TimeOut = getTime();*/
        }
        System.out.println("Simulation terminée");

    }

    protected static int connexionclient() {
        /* Connexion au serveur qui permet de se connecter avec V-REP
        *return 1 si bonne connexion, 0 sinon
        *faire attention aux ports: ici 8080*/
        try {

            URL url = new URL("http://localhost:8080/REST_Terminator/webresources/Terminator/connexionVREP");
            HttpURLConnection connexion = (HttpURLConnection) url.openConnection();
            connexion.setRequestProperty("User-Agent", "");
            connexion.connect();
            InputStream streamReponse = connexion.getInputStream();
            BufferedReader bufferReception = new BufferedReader(new InputStreamReader(streamReponse));
            return 1;
        } catch (Exception ex) {
            ex.printStackTrace();
            int answer = 0;
            return answer;
        }
    }

    protected static int getTime() {
        /* renvoie le temps de simulation dans V-REP
        * 0 si erreur
        *Faire attention aux ports : ici 8080*/
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

        } catch (Exception ex) {
            ex.printStackTrace();
            int answer = 0;
            return answer;
        }
    }

    protected static double getOrientation() {
        /*renvoie l'angle gamma des Angles d'Euler de BubbleRob par rapport au référentiel V-REP
        *renvoie 999 si erreur 
        *attention aux ports: ici 8080*/
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

        } catch (Exception ex) {
            ex.printStackTrace();
            double answer = 999;
            return answer;
        }
    }

    protected static String ProximitySensor() {
        /*renvoie TRUE si détecte obstacle 
        *False sinon
        *"il y a une erreur" en cas d'erreur*/
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

        } catch (Exception ex) {
            ex.printStackTrace();
            return "il y a une erreur";
        }
    }

    protected static int setVitesseMoteur(String moteur, float vitesse) {
        /*paramètres: moteur doit être "droit" ou "gauche" .vitesse un float 
        *return 1 
        *0 si erreur de connexion
        *attention aux ports: ici 8080*/
        try {
            URL url = new URL("http://localhost:8080/REST_Terminator/webresources/Terminator/setMotor?moteur=" + moteur + "&valeur=" + vitesse);
            HttpURLConnection connexion = (HttpURLConnection) url.openConnection();
            connexion.setRequestProperty("User-Agent", "");
            connexion.setDoInput(true);
            connexion.connect();
            InputStream streamReponse = connexion.getInputStream();
            BufferedReader bufferReception = new BufferedReader(new InputStreamReader(streamReponse));
            connexion.disconnect();
            return 1;
        } catch (Exception ex) {
            ex.printStackTrace();
            return 0;
        }
    }

    protected static double[] getPositionBubbleRob() {
        /*Renvoie liste de double avec la coordonée x et y de BubbleRob [x, y]
        * [110,110] si erreur
        *Attention aux ports: ici 8080*/
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
            double answer[] = {answer1, answer2};
            return answer;

        } catch (Exception ex) {
            ex.printStackTrace();
            double[] answer = {110, 110};
            System.out.println("ERREUUR");
            return answer;
        }
    }

    protected static double[] getPositionDestination() {
        /*renvoie la position de la destination de la trajectoire de BubbleRob sous forme de tableau de double [x,y]
        *par rapport au référenciel V-REP
        *renvoie [110,110 ]si erreur
        *Attention au ports: ici 8080*/
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
            double answer[] = {answer1, answer2};
            return answer;

        } catch (Exception ex) {
            ex.printStackTrace();
            double[] answer = {110, 110};
            return answer;
        }
    }

    protected static double getTrajectoire(double[] pointA, double[] pointB) {
        //Paramètres pointA,pointB deux doubles avec les coordonnées [x,y]
        //Renvoie l'angle qu'il faut tourner pour se mettre dans l'axe  qui passe par les 2 points
        double angle = Math.atan((pointB[1] - pointA[1]) / (pointB[0] - pointA[0]));
        return angle;
    }

    protected static double TimeWhile(double TimeEntry, double TimeOut) {
        /* ---------For testing purpose only--------
        Cette fonction renvoie une différence entre deux temps. 
        Il faut décommenter la ligne de code qui calcule TimeEntry et TimeOut au début et fin de boucle
         */
        return TimeOut - TimeEntry;

    }
}
