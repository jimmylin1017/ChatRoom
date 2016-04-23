package Client;

import java.io.BufferedReader;
import java.io.DataOutput;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Client{
	/*public int port;
	public String hostname;
	private ChatClientSocket clientSkt;*/
	public static void main(String[] args)
    {
    	Scanner keyboard=new Scanner(System.in);
     	System.out.print("Host:");
    	String name=keyboard.next();
    	System.out.print("Port:");
    	int num=keyboard.nextInt();
    	
    	/*Client c1=new Client();
    	c1.set(name, num);
    	c1.setToClient();
    	keyboard.close();*/
    	Clientexecute c1=new Clientexecute(name,num);
    	c1.setToClient();
    	
    	
    }
	/*public void set(String name,int port)
	{
		this.port=port;
		hostname=name;
	}
	
	public void setToClient() {

        clientSkt = new ChatClientSocket(hostname, port);
        clientSkt.start();
        try {
            sleep(100);
        } catch (InterruptedException e) {};
        
        Scanner keyboard=new Scanner(System.in);
        System.out.println("please input your name");
        String names=keyboard.nextLine();
        clientSkt.DataOutput(names);
        
        System.out.println("Please choose 1.Add a new group\t2.Join a specific group");
        try {
            sleep(1000);
        } catch (InterruptedException e) {};
        String type=keyboard.nextLine();
        clientSkt.DataOutput(type);
        
        String groupname=null;
		int afterConvert = Integer.parseInt(type);
		if (afterConvert==2)
		{
			System.out.println("Input group name");
			groupname=keyboard.nextLine();
			clientSkt.DataOutput(groupname);
			
			System.out.println("join");
			
		}
		else 
		{
			System.out.println("Input group name");
			groupname=keyboard.nextLine();
			clientSkt.DataOutput(groupname);
			System.out.println("new");
		}
        while(true)
        {
        	String name=keyboard.nextLine();
        	clientSkt.DataOutput(name);
        	if (name.equals("EXIT"))
        	{
        		 
        		clientSkt.terminate();
        		break;
        		
       	 	}
        }
        keyboard.close();
    }*/
}


class Clientexecute extends Thread{
	
	public int port;
	public String hostname;
	private ChatClientSocket clientSkt;
	
	public Clientexecute(String name,int num)
	{
		hostname = name;
		port = num;
	}
	public void setToClient() {

        clientSkt = new ChatClientSocket(hostname, port);
        
        
        Scanner keyboard=new Scanner(System.in);
        System.out.println("please input your name");
        String names=keyboard.nextLine();
        
        clientSkt.start();
        try {
            sleep(100);
        } catch (InterruptedException e) {};
        
        clientSkt.DataOutput(names);
        
        System.out.println("Please choose 1.Add a new group\t2.Join a specific group");
        /*try {
            sleep(1000);
        } catch (InterruptedException e) {};*/
        
        String type=keyboard.nextLine();
        clientSkt.DataOutput(type);
        
        String groupname=null;
		int afterConvert = Integer.parseInt(type);
		if (afterConvert==2)
		{
			System.out.println("Input group name");
			groupname=keyboard.nextLine();
			clientSkt.DataOutput(groupname);
			
			System.out.println("join");
			
		}
		else 
		{
			System.out.println("Input group name");
			groupname=keyboard.nextLine();
			clientSkt.DataOutput(groupname);
			System.out.println("new");
		}
        while(true)
        {
        	String name=keyboard.nextLine();
        	clientSkt.DataOutput(name);
        	if (name.equals("EXIT"))
        	{
        		 
        		clientSkt.terminate();
        		break;
        		
       	 	}
        }
        keyboard.close();
    }
	
}
class ChatClientSocket extends Thread {
    private Socket skt;        
    private InetAddress host;  
    private int port;          
    private BufferedReader theInputStream;
    private PrintStream theOutputStream;
    public String message;    
 
    public void terminate()  { 
    	try
    	{
        skt.close();
    	}
    	catch(Exception e)
    	{
    		System.out.println("1"+e.getMessage());
    	}
    } 
 
    public ChatClientSocket(String ip, int port) {
        try {
           
            host = InetAddress.getByName(ip);
            this.port = port;
        }
        catch (IOException e) {
           System.out.println(e.getMessage());
        }
    }
    
    public void run() {
        try {
        	

            skt = new Socket(host, port);
            System.out.println("Connected");      
            theInputStream = new BufferedReader(new InputStreamReader(skt.getInputStream()));
            theOutputStream = new PrintStream(skt.getOutputStream());
            System.out.println("__START__");
            
            while((message = theInputStream.readLine()) != null) {
            	try {
                    sleep(100);
                } catch (InterruptedException e) {};
                System.out.println(message);
                if (message.equals("EXIT")) 
                {
                	message=null;
                	break;
                }
                
            }
 
            if(message == null) {
                skt.close();
           
                System.out.println("Disconnecetion");
            }
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
           
        }
    }
 
   
    public void DataOutput(String data) {
        theOutputStream.println(data);
    }
    public void DataNumOutput(int data) {
        theOutputStream.println(data);
    }
}
