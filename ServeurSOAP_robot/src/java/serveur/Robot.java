/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serveur;

import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;

import coppelia.IntW;
import coppelia.IntWA;
import coppelia.FloatWA;
import coppelia.remoteApi;
import coppelia.BoolW;


/**
 *
 * @author User
 */


@WebService(serviceName = "Robot")
public class Robot {
    
    
    

    /**
     * This is a sample web service operation
     */
    @WebMethod(operationName = "hello")
    public String hello(@WebParam(name = "name") String txt) {
        System.out.println("test");
        return "Hello haha" + txt + " !";
    }
    
    @WebMethod(operationName = "somme")
    public double somme(@WebParam(name = "a") double a, @WebParam(name = "b") double b) {
        
        return a+b;
    }
    
    @WebMethod(operationName = "initsimul")
    public String initsimul(){
     
     coppelia.remoteApi vrep ;
    vrep= new  coppelia.remoteApi();
    vrep.simxFinish(-1);
    int clientID = vrep.simxStart("127.0.0.1", 19999, true, true, 5000, 5);		// Connexion a VREP
    return "Ca marche";
   
	}
    
    public static void connexionVREP(){
        
   
    }

    
    }

