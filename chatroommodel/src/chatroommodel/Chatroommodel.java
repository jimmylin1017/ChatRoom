

package chatroommodel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import static java.lang.Integer.parseInt;
import static java.lang.Thread.sleep;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


/*
******************************
Author: Robert Lee
Date: 2016 summer
Purpose: Write a chatroom and forum system for course Soft Engineering.
Version: 1.0
******************************
 */



public class Chatroommodel extends Application
{
    public int port;
    public String host;
    public String name;
    public static Client cl;
    
    @Override
    public void start(Stage stage) throws Exception {
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLDocument.fxml"));
        Parent root = loader.load();
        FXMLDocumentController controller = (FXMLDocumentController) loader.getController();
        //Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
        Scene scene = new Scene(root);
       
        String str = PortBox.display();//Get the port number and ipaddress from PortBox.
        /*str = potr + " " + ipaddr*/
        String splstr[] = str.split(" ");
       
        
        String _host = splstr[1];
        
        int _port = parseInt(splstr[0]);
        
        String _name = splstr[2];
        
        
        System.out.println(_name);
         
       
        cl = new Client(_host, _port,_name,controller);
    	
        
        /*
        try{
        }catch()
        {
        }
        */
        
	
	
            
        //scene.getStylesheets().add(Chatroommodel.class.getResource("StyleSheet.css").toExternalForm());
        stage.setResizable(false);
        stage.setTitle("Chatroom");
        stage.setScene(scene);
        stage.show();
        cl.setToClient();
        
        
    }
    
}
