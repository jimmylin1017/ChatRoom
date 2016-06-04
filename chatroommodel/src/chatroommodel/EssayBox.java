



package chatroommodel;

/*
*********************

Purpose: This is the box for writing an essay.

*********************
*/

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class EssayBox {
    
    static String context="";
    
    public String display()
    {
        
        
        
        /*Anchor pane*/
        AnchorPane ap = new AnchorPane();
        
        
        /*For Text area*/
        TextArea ta = new TextArea();
        ta.setLayoutX(20.0);
        ta.setLayoutY(60.0);
        ta.setPrefHeight(660.0);
        ta.setPrefWidth(660.0);  
        ta.setWrapText(true);
        ap.getChildren().add(ta);
        
        /*Button for sending essay.*/
        Button btn = new Button("Send essay");
        btn.setLayoutX(20.0);
        btn.setLayoutY(20.0);
        btn.setPrefHeight(30.0);
        btn.setPrefWidth(120.0);
        ap.getChildren().add(btn);
       
        /*Label for indexing title*/
        Label label = new Label("Title : ");
        label.setLayoutX(200.0);
        label.setLayoutY(20.0);
        label.setPrefHeight(30.0);
        label.setPrefWidth(60.0);
        ap.getChildren().add(label);
       
        /*The textfield for essay title.*/
        TextField tf = new TextField();
        tf.setLayoutX(260.0);
        tf.setLayoutY(20.0);
        tf.setPrefHeight(30.0);
        tf.setPrefWidth(420.0);
        ap.getChildren().add(tf);
        
        /*Stage*/
        Stage window = new Stage();
        window.setTitle("Writing essay");
        
        /*Scene*/
        Scene scene = new Scene(ap,700,740);
        scene.getStylesheets().add(Chatroommodel.class.getResource("EssayBoxstyle.css").toExternalForm());
         
        /*The action for send button.*/
        btn.setOnAction(e -> {
            System.out.println("You clicked send\n");
            context = tf.getText();
            window.close();
        });
        
        window.setScene(scene);
        window.showAndWait();
        
        return context;
    }
    
    
}
