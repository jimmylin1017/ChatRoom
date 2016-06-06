package chatroommodel;

import static chatroommodel.Chatroommodel.cl;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import static java.lang.Thread.sleep;
import java.net.InetAddress;
import java.net.Socket;
import javafx.application.Platform;
import javafx.scene.control.Hyperlink;
import javafx.scene.text.Text;

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
    
    public void threadclose()
    {
        clientSkt.terminate();
    }
    
    public void setToClient() {

        clientSkt = new clientSocketController(host, port, name,controller);
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
        if(!clientSkt.socketoff())
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
    private String name;
    private BufferedReader theInputStream;
    private PrintStream theOutputStream;
    public String message;
    private final FXMLDocumentController controller;
    
    
    public final void terminate()
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
    
    public clientSocketController(String host, int port,String name,FXMLDocumentController controller)
    {
        this.controller = controller;
        try
        {
            this.host = InetAddress.getByName(host);
            this.port = port;
            this.name = name;
        }
        catch (IOException e)
        {
           terminate();
           Platform.runLater(new Runnable(){
               @Override
               public void run()
               {
                   AlertBox.display("Connection failed!");
                   Platform.exit();
                   System.exit(0);
               }
            });
        }
    }

    // exit chat room
    
    
    public boolean socketoff()
    {
        if(clientSkt==null)
            return true;
        else
            return false;
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

                if(message.charAt(0) == '#' && message.charAt(1) == ' ')
                {
                    System.out.println(message);
                    String splstr[] = message.split(" ",2);
                    final String msg = splstr[1];
                    
                    controller.taforumdisplay.appendText(msg + "\n");
                }
                else if(message.charAt(0) == '#' && message.charAt(1) == '@' && message.charAt(2) == '!')
                {
                    //System.out.println(message);
                    String splstr[] = message.split(" ",4);
                    final String msg = splstr[3];
                    
                    System.out.println(msg);
                    
                    Platform.runLater(new Runnable(){
                    @Override
                    public void run()
                    {
                        Hyperlink hyper = new Hyperlink(msg);
                        hyper.setId(msg);
                        
                        /*Make evey hyperlink on its own line.*/
                        Text tx = new Text("\n");
                        
                        /*Hyperlink event*/
                        hyper.setOnAction(e -> {
                            controller.taforumdisplay.clear();
                            cl.sendMessage("@GETPOST "+hyper.getId());
                        });
                        
                        /*Set hyperlink style.*/
                        hyper.setStyle("-fx-text-fill: blue;"
                                + "-fx-font-size: 20pt;");
                        controller.taforumarticle.getChildren().add(hyper);
                        controller.taforumarticle.getChildren().add(tx);
                    }
                    });
                    
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
            System.out.println("lower run");
            terminate();
            Platform.runLater(new Runnable(){
               @Override
               public void run()
               {
                   AlertBox.display("Connection failed!");
                   Platform.exit();
                   System.exit(0);
               }
            });
            
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
