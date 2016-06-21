
package chatroommodel;

import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class AlertBox {
    
    
    
    public static void display(String str)
    {
           
        AnchorPane ap = new AnchorPane();
        
        Text message = new Text(30,50,str);
        message.setId("message-text");
        
        message.setLayoutX(20.0);
        message.setLayoutY(30.0);
        ap.getChildren().add(message);
        
        Stage window = new Stage();
        
        window.setTitle("Error!!");
                
        Scene scene = new Scene(ap,400,150);
        scene.getStylesheets().add(Chatroommodel.class.getResource("AlertBoxstyle.css").toExternalForm());
        window.setScene(scene);
        window.showAndWait();
        
    }
}
