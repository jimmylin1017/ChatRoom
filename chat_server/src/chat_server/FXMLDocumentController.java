/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat_server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;


/**
 *
 * @author jimmy
 */
public class FXMLDocumentController implements Initializable {
    
    @FXML
    private TextArea textArea;
    
    enum Info {
        Connect, Disconnect,Chat
    }
    
    ArrayList<PrintWriter> writeMessageToClient;
    ArrayList<String> userNames;
    
    public class ClientHandler implements Runnable {
        
        BufferedReader readeFromClient;
        Socket clientSocket;
        PrintWriter wirteToClient;
        
        public ClientHandler(Socket clientSocket, PrintWriter wirteToClient) {
            this.wirteToClient = wirteToClient;
            try {
                this.clientSocket = clientSocket;
                InputStreamReader clientToServer = new InputStreamReader(this.clientSocket.getInputStream());
                readeFromClient = new BufferedReader(clientToServer);
            }
            catch (Exception e) {
                textArea.appendText("Unexpected error... \n");
            }
        }
                    
        @Override
        public void run() {
            
            String message;
            String[] splitMsg;
            
            try {
                /*
                    username:message:command
                */
                while ((message = readeFromClient.readLine()) != null) 
                {
                    textArea.appendText("Received: " + message + "\n");
                    
                    splitMsg = message.split(":");
                    textArea.appendText("---Split Message---\n");
                    for (String s : splitMsg) 
                    {
                        
                        textArea.appendText(s + "\n");
                    }
                    textArea.appendText("---Split Message---\n");
                    
                    if (Info.Connect.equals(splitMsg[2])) 
                    {
                        tellEveryone("# " + splitMsg[0] + " Connect");
                        userAdd(splitMsg[0]);
                    } 
                    else if (Info.Disconnect.equals(splitMsg[2])) 
                    {
                        tellEveryone("# " + splitMsg[0] + " Disconnect");
                        userRemove(splitMsg[0]);
                    } 
                    else if (Info.Chat.equals(splitMsg[2])) 
                    {
                        tellEveryone(message);
                    } 
                    else 
                    {
                        textArea.appendText("Message can not get Infomation \n");
                    }
                }
            }
            catch(Exception e) {
                textArea.appendText("No Conditions were met. \n");
            }
        }
    }
    public void userAdd (String userName) {
        //String message, add = ": :Connect", done = "Server: :Done"; 
        String name = userName;

        userNames.add(name);
        textArea.appendText("Add " + name + "\n");
        
        /*
        String[] userNamesArray = new String[userNames.size()];
        userNames.toArray(userNamesArray);

        for (String s : userNamesArray) 
        {
            message = (s + add);
            tellEveryone(message);
        }
        tellEveryone(done);
        */
    }
    
    public void userRemove (String userName) {
        //String message, add = ": :Connect", done = "Server: :Done";
        String name = userName;
        
        userNames.remove(name);
        textArea.appendText("Remove " + name + "\n");
        
        /*
        String[] userNamesArray = new String[userNames.size()];
        userNames.toArray(userNamesArray);

        for (String s : userNamesArray) 
        {
            message = (s + add);
            tellEveryone(message);
        }
        tellEveryone(done);
        */
    }
    
    public void tellEveryone(String message) {
        Iterator it = writeMessageToClient.iterator();
        
        while (it.hasNext()) 
        {
            try {
                PrintWriter writer = (PrintWriter) it.next();
                writer.println(message);
                textArea.appendText("Sending: " + message + "\n");
                writer.flush();
                //textArea..setCaretPosition(textArea.getDocument().getLength());

            }
            catch (Exception e) {
                textArea.appendText("Error telling everyone. \n");
            }
        }
    }
    
    public class ServerStart implements Runnable {
        @Override
        public void run() {
            
            writeMessageToClient = new ArrayList<>();
            userNames = new ArrayList<>();
            
            try {
                ServerSocket serverSock = new ServerSocket(8888);
                
                while (true) 
                {
                    textArea.appendText("Waiting for connection... \n");
                    Socket clientSock = serverSock.accept();
                    PrintWriter wirteToClient = new PrintWriter(clientSock.getOutputStream());
                    writeMessageToClient.add(wirteToClient);

                    Thread listener = new Thread(new ClientHandler(clientSock, wirteToClient));
                    listener.setDaemon(true);
                    listener.start();
                    textArea.appendText("Got a connection. \n");
                }
            }
            catch (Exception e) {
                textArea.appendText("Error making a connection. \n");
            }
        }
    }

    
    @FXML
    private void startButtonAction(ActionEvent event) {
        
        Thread startServer = new Thread(new ServerStart());
        startServer.setDaemon(true);
        startServer.start();

        textArea.appendText("Server started...\n");
    }
    
    @FXML
    private void shutDownButtonAction(ActionEvent event) {
        try {
            Thread.sleep(5000);
        }
        catch(InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        textArea.appendText("Server stopping... \n");
    }
    
    @FXML
    private void onlineUserButtonAction(ActionEvent event) {
        textArea.appendText("\n ---Online users--- \n");
        for (String s : userNames) {
            textArea.appendText(s + "\n");
        }
        textArea.appendText("\n ---Online users finish--- \n");
    }
    
    @FXML
    private void clearMessageButtonAction(ActionEvent event) {
        textArea.setText("");
    }
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
