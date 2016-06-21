

package chatroommodel;

import static java.lang.Integer.parseInt;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


/*
******************************
Author: Robert Lee
Developed date: 2016 spring to summer
Purpose: Write a chatroom and forum system for course Soft Engineering.
Version: 1.3
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
        scene.getStylesheets().add(Chatroommodel.class.getResource("Chatroomstyle.css").toExternalForm());
        
        
        String str = PortBox.display();//Get the port number and ipaddress from PortBox.
        /*str = potr + " " + ipaddr*/
        
        
        if(str!=null)
        {
            if(str!="-1")
            {
                String splstr[] = str.split(" ");
       
        
                String _host = splstr[1];

                int _port = parseInt(splstr[0]);

                String _name = splstr[2];


                System.out.println(_name);
                cl = new Client(_host, _port,_name,controller);
                cl.setToClient();




                stage.setResizable(false);
                stage.setTitle("Chat & Forum Client");
                stage.setScene(scene);
                stage.show();

                /*When press x button on the top-rigth.*/
                stage.setOnCloseRequest(e -> {
                   cl.threadclose();
                    Platform.exit();
                    System.exit(0);
                });
                }
            
        }
        else
        {
            AlertBox.display("The text field cannot be empty!");
        }
        
        
        
        
    }
    
}
