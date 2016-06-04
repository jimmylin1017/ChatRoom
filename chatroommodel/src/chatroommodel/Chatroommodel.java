

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
    private clientSocketController clientSkt;
    
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
        
        Scene scene = new Scene(root);
       
        String str = PortBox.display();//Get the port number and ipaddress from PortBox.
        /*str = potr + " " + ipaddr*/
        String splstr[] = str.split(" ");
       
        
        String _host = splstr[1];
        
        int _port = parseInt(splstr[0]);
        
        String _name = splstr[2];
        
        
        System.out.println(_name);
     
        
        Chatroommodel cl = new Chatroommodel(_host, _port, _name);
        
        
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
        
        
        /*Need to put the function into button.*/
        cl.setToClient();
        
        
    }
    public Chatroommodel()
    {
        
    }
    public Chatroommodel(String host,int port,String name)
    {
        this.port = port;
        this.host = host;
        this.name = name;
    }
    
    public void setToClient() 
    {

        clientSkt = new clientSocketController(host, port);
        clientSkt.start();

        try
        {
            // waiting for Server accepted
            sleep(100);
        }
        catch(InterruptedException e)
        {
            System.out.println(e.getMessage());
        }

        Scanner keyboard = new Scanner(System.in);

        //System.out.println("please input your name");
        
        /*Move user name setting to the front, with port number and ip address*/
       
        clientSkt.DataOutput(name);


        // input message
        while(true)
        {
            String message = keyboard.nextLine();
            String commandArr[] = message.split(" ", 2);

            // exit command
                if(message.toUpperCase().equals("@EXIT"))
                {
                        clientSkt.terminate();
                        break;
                }
            else if(commandArr[0].toUpperCase().equals("@RENAME") && commandArr.length < 2)
            {
                System.out.println("Command Error!");
                continue;
            }
            else if(commandArr[0].toUpperCase().equals("@POST") && commandArr.length < 2)
            {
                System.out.println("Command Error!");
                continue;
            }
            else if(commandArr[0].toUpperCase().equals("@GETPOST") && commandArr.length < 2)
            {
                System.out.println("Command Error!");
                continue;
            }
            else
            {
                clientSkt.DataOutput(message);
            }
        }

        //keyboard.close();
    }
    
}

class clientSocketController extends Thread
{
    private Socket clientSkt;
    private InetAddress host;
    private int port;
    private BufferedReader theInputStream;
    private PrintStream theOutputStream;
    public String message;

    public clientSocketController(String host, int port)
    {
        try
        {
            this.host = InetAddress.getByName(host);
            this.port = port;
        }
        catch (IOException e)
        {
           System.out.println(e.getMessage());
        }
    }

    // exit chat room
    public void terminate()
    { 
    	try
    	{
            clientSkt.close();
    	}
        catch(Exception e)
    	{
    		System.out.println(e.getMessage());
    	}
    }

    public void run()
    {
        try
        {
            clientSkt = new Socket(host, port);
            System.out.println("Connected");
            
            theInputStream = new BufferedReader(new InputStreamReader(clientSkt.getInputStream()));
            theOutputStream = new PrintStream(clientSkt.getOutputStream());
            
            //System.out.println("__START__");
            
            while((message = theInputStream.readLine()) != null)
            {
                /*
            	try
                {
                    sleep(100);
                }
                catch(InterruptedException e)
                {
                    System.out.println(e.getMessage());
                }
                */

                if(message.charAt(0) == '#' && message.charAt(1) == ' ')
                {
                    System.out.println(message);
                }
                else
                {
                    System.out.println(message);
                }
            }

            if(message == null)
            {
                clientSkt.close();

                System.out.println("Disconnecetion");
            }

        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
        }
    }

    public void DataOutput(String data)
    {
        theOutputStream.println(data);
    }

    public void DataNumOutput(int data)
    {
        theOutputStream.println(data);
    }

}