import java.io.BufferedReader;
import java.io.DataOutput;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Client extends Thread
{
	public int port;
	public String host;
	private clientSocketController clientSkt;

	public static void main(String[] args)
    {
    	Scanner keyboard = new Scanner(System.in);
     	
        System.out.print("Host:");
        String _host = keyboard.next();
    	
        System.out.print("Port:");
        int _port = keyboard.nextInt();
    	
    	Client cl = new Client(_host, _port);
    	cl.setToClient();
    	
        keyboard.close();
    }
	
    public Client(String host,int port)
	{
		this.port = port;
		this.host = host;
	}
	
	public void setToClient() {

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
        
        System.out.println("please input your name");
        String names = keyboard.nextLine();
        // setting user name
        clientSkt.DataOutput(names);
        
        System.out.println("Please choose 1.Add a new group\t2.Join a specific group");
        try
        {
            // waiting for Server show all group
            sleep(1000);
        }
        catch(InterruptedException e)
        {
            System.out.println(e.getMessage());
        }

        String type = keyboard.nextLine();
        while(!(type.equals("1") || type.equals("2")))
        {
            System.out.println("Please choose 1.Add a new group\t2.Join a specific group");
            type = keyboard.nextLine();
        }

        // choose type of group
        clientSkt.DataOutput(type);
        
        String groupname = null;
		int afterConvert = Integer.parseInt(type);
		
        if(afterConvert == 2)
		{
            // join a group
			System.out.println("Input group name");
			groupname = keyboard.nextLine();
			clientSkt.DataOutput(groupname);			
		}
		else
		{
            // creat new group
			System.out.println("Input group name");
			groupname = keyboard.nextLine();
			clientSkt.DataOutput(groupname);
		}

        // input message
        while(true)
        {
        	String message = keyboard.nextLine();
            String command[] = message.split(" ", 2);

            // exit command
        	if(message.toUpperCase().equals("@EXIT"))
        	{
        		clientSkt.terminate();
        		break;
       	 	}
            else if(command[0].toUpperCase().equals("@RENAME") && command.length < 2)
            {
                System.out.println("Command Error!");
                continue;
            }

            clientSkt.DataOutput(message);
        }

        keyboard.close();
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

                System.out.println(message);

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
