
package chatroommodel;


import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

/*
********************

Purpose: Control the file FXMLDocument.fxml.

********************
*/

public class FXMLDocumentController implements Initializable {

    
    /*FXML declaration***************************/
    @FXML
    private AnchorPane anchorpane;
    @FXML
    private Button Upload;
    @FXML
    private Button enter;
    @FXML
    private TextArea taforumdisplay;
    @FXML
    private TextArea tachatinput;
    @FXML
    private TextArea tachatdisplay;
    @FXML
    private TextFlow taforumarticle;
    @FXML
    private MenuItem mr;
    @FXML
    private MenuItem ml;
    @FXML
    private MenuItem de;
    @FXML
    private MenuItem wr;
    @FXML
    private MenuItem reload;

    
    /*Normal variables**************************/
 
    double forumwidth = 392.0,forumheight = 762.0;
    double forumleftx = 21.0, forumrightx = 426.0;
    double forumoffset = 16;
    
    /*1 for forumarticle, 2 for forumdisplay, 0 for default.*/
    public static int con = 0;          
    

 
    
    @FXML
    private void EnterAction(ActionEvent event) 
    {
        System.out.println("You clicked Enter!");
    }
    
    @FXML
    private void MenuForumeAction(ActionEvent event)
    {
        tachatdisplay.setOpacity(0);
        tachatdisplay.setDisable(true);
        tachatinput.setOpacity(0);
        tachatinput.setDisable(true);
        
        
        taforumarticle.setOpacity(1);
        taforumarticle.setDisable(false);
        taforumarticle.setStyle("-fx-background-color: #F8F8F8;"
                + "-fx-border-color: blue;");
        
        taforumdisplay.setOpacity(1);
        taforumdisplay.setDisable(false);
        
        mr.setDisable(false);
        ml.setDisable(false);
        de.setDisable(false);
        wr.setDisable(false);
        reload.setDisable(false);
        
        enter.setDisable(true);
        enter.setOpacity(0);
        
        if(con==1)
            MaximizeLeft(event);
        else if(con==2)
            MaximizeRight(event);
        
        System.out.println("You clicked Forum!");
    }
    
    @FXML
    private void MenuLobbyAction(ActionEvent event)
    {
        taforumarticle.setOpacity(0);
        taforumarticle.setDisable(true);
        taforumdisplay.setOpacity(0);
        taforumdisplay.setDisable(true);
        
        tachatdisplay.setOpacity(1);
        tachatdisplay.setDisable(false);
        tachatinput.setOpacity(1);
        tachatinput.setDisable(false);
        
        mr.setDisable(true);
        ml.setDisable(true);
        de.setDisable(true);
        wr.setDisable(true);
        reload.setDisable(true);
        
        enter.setDisable(false);
        enter.setOpacity(1);
        
        System.out.println("You clicked Lobby!");
    }
    @FXML
    private void Writing(ActionEvent event)
    {
        /*Add hyperlink for one line.*/
        
        EssayBox eb = new EssayBox();
        
        String context = eb.display();
      
        /*Generate hyperlink*/
        Hyperlink hyper = new Hyperlink(context);
     
        
        /*Make evey hyperlink on its own line.*/
        Text tx = new Text("\n");
        
        /*Set hyperlink style.*/
        hyper.setStyle("-fx-text-fill: blue;"
                + "-fx-font-size: 20pt;");
        

        
        taforumarticle.getChildren().add(hyper);
        taforumarticle.getChildren().add(tx);
    }
        
    @FXML
    private void MaximizeRight(ActionEvent event)
    {
        taforumdisplay.setOpacity(1);
        taforumdisplay.setDisable(false);
        
        taforumdisplay.setLayoutX(forumleftx);
        
        taforumdisplay.setPrefSize(forumwidth*2.0+forumoffset, forumheight);
        taforumarticle.setOpacity(0);
        taforumarticle.setDisable(true);
        con = 2;
        System.out.println("You clicked MaximizeRight");
    }
    @FXML
    private void MaximizeLeft(ActionEvent event)
    {
        taforumarticle.setOpacity(1);
        taforumarticle.setDisable(false);
        taforumarticle.setPrefSize(forumwidth*2.0+forumoffset, forumheight);
        
        taforumdisplay.setLayoutX(forumrightx);
        taforumdisplay.setOpacity(0);
        taforumdisplay.setDisable(true);
        con = 1;
        System.out.println("You clicked MaximizeLeft");
    }
    @FXML
    private void Default(ActionEvent event)
    {
        taforumdisplay.setOpacity(1);
        taforumdisplay.setDisable(false);
        
        taforumarticle.setOpacity(1);
        taforumarticle.setDisable(false);
        
        taforumdisplay.setLayoutX(forumrightx);
        
        taforumdisplay.setPrefSize(forumwidth, forumheight);
        taforumarticle.setPrefSize(forumwidth, forumheight);
        con = 0;
        
    }
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        /*anchorpane.setStyle("-fx-background-color: #191919;");      //Change background color here.
        tfchatinput.setStyle("-fx-background-color: #CCBBFF;");
        tfchatdisplay.setStyle("-fx-text-fill: black;");*/
    }    
  
    
}
