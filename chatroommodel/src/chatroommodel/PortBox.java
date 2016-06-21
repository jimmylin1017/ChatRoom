
package chatroommodel;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/*
***************

Purpose: This is a Box that get IP address and Port number.

***************
*/


public class PortBox {
    
    static String port;

    public static String display()
    {
        Stage stage = new Stage();
        stage.setTitle("Login");
        
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Text scenetitle = new Text("Welcome");
        scenetitle.setFont(new Font("Arial", 30));
        grid.add(scenetitle, 0, 0, 2, 1);

        Text IP = new Text(10,20,"  IP   address:");
        grid.add(IP, 0, 1);

        TextField IPaddr = new TextField();
        grid.add(IPaddr, 1, 1);

        Text pt = new Text(10,20,"Port number:");
        grid.add(pt, 0, 2);

        TextField Portnumber = new TextField();
        grid.add(Portnumber, 1, 2);
        
        Text na = new Text(10,20,"        Name:");
        grid.add(na, 0, 3);
        
        TextField name = new TextField();
        grid.add(name, 1, 3);

        Button btn = new Button("Sign in");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(btn);
        grid.add(hbBtn, 1, 4);

        final Text actiontarget = new Text();
        grid.add(actiontarget, 1, 6);
        
        btn.setOnAction(e -> {
            if(Portnumber.getText().trim().isEmpty() 
            || IPaddr.getText().trim().isEmpty()
            || name.getText().trim().isEmpty())
            {
                //System.out.println("zz111");
                port = null;
            }
            else    
                port = Portnumber.getText()+" "+IPaddr.getText()+" "+name.getText();
            
            stage.close();
        });
        stage.setOnCloseRequest(e -> {
            port = "-1";
        });
        
        Scene scene = new Scene(grid, 300, 375);
        scene.getStylesheets().add(Chatroommodel.class.getResource("PortBoxstyle.css").toExternalForm());
        scenetitle.setId("welcome-text");       //For specifying the item in css.
        
        
        stage.setScene(scene);
        stage.showAndWait();                    //Need to wait for the input.
        
        return port;
    }

}
