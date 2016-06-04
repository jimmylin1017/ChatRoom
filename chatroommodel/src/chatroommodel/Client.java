package chatroommodel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import static java.lang.Thread.sleep;
import java.net.InetAddress;
import java.net.Socket;

public class Client
{
    public int port;
    public String host;
    public String name;
    private clientSocketController clientSkt;
    private final FXMLDocumentController controller;
	
    public Client(String host,int port, String name, FXMLDocumentController controller)
    {
        this.port = port;
        this.host = host;
        this.name = name;
        this.controller = controller;
    }
	
    public void setToClient() {

        clientSkt = new clientSocketController(host, port, controller);
        Thread clientThread = new Thread(clientSkt);
        
        clientThread.start();
        
        try
        {
            // waiting for Server accepted
            sleep(100);
        }
        catch(InterruptedException e)
        {
            System.out.println(e.getMessage());
        }

        // setting user name
        clientSkt.DataOutput(name);
    }

    public void sendMessage(String msg)
    {
        // input message
        String message = msg;
        String commandArr[] = message.split(" ", 2);

        // exit command
        if(message.toUpperCase().equals("@EXIT"))
        {
            clientSkt.terminate();
        }
        else if(commandArr[0].toUpperCase().equals("@RENAME") && commandArr.length < 2)
        {
            System.out.println("Command Error!");
        }
        else if(commandArr[0].toUpperCase().equals("@POST") && commandArr.length < 2)
        {
            System.out.println("Command Error!");
        }
        else if(commandArr[0].toUpperCase().equals("@GETPOST") && commandArr.length < 2)
        {
            System.out.println("Command Error!");
        }
        else
        {
            clientSkt.DataOutput(message);
        }
    }
}

class clientSocketController extends FXMLDocumentController implements Runnable
{
    private Socket clientSkt;
    private InetAddress host;
    private int port;
    private BufferedReader theInputStream;
    private PrintStream theOutputStream;
    public String message;
    private final FXMLDocumentController controller;

    public clientSocketController(String host, int port,FXMLDocumentController controller)
    {
        this.controller = controller;
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
    
    @Override
    public void run()
    {
        try
        {
            clientSkt = new Socket(host, port);
            System.out.println("Connected");
            
            theInputStream = new BufferedReader(new InputStreamReader(clientSkt.getInputStream()));
            theOutputStream = new PrintStream(clientSkt.getOutputStream());
                        
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
                    final String msg = message;
                    controller.tachatdisplay.appendText(msg + "\n");
                }
                else
                {
                    System.out.println(message);
                    final String msg = message;
                    controller.tachatdisplay.appendText(msg + "\n");
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
