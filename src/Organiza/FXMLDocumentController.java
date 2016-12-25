package Organiza;

import com.jfoenix.controls.JFXButton;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;

/**
 *
 * @author Daniel
 */
public class FXMLDocumentController implements Initializable
{
    @FXML
    private Label label;
    @FXML
    private JFXButton btnFlat;
    @FXML
    private Pane mainPane;
    @FXML
    private Pane headerPane;
    @FXML
    private JFXButton btnExit;
    @FXML
    private JFXButton btnMinimize;

    private static Stage stage;
    private Mouse mouse;
    private static double xOffset = 0;
    private static double yOffset = 0;
    private DataGrabber data;
    
    
    
    
    
    

    @FXML
    private void handleButtonAction(ActionEvent event)
    {
        System.out.println("You clicked me!");
        label.setText("Hello World!");
    }

    @FXML
    private void btnExitPressed(ActionEvent event)
    {
        System.exit(0);
    }

    @FXML
    private void btnMinimizePressed(ActionEvent event)
    {
        stage.setIconified(true);
    }

    @FXML 
    private void btnFlatPressed(ActionEvent event)
    {
        data.getRequest();
    }
    
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        data = new DataGrabber();
        mouse = new Mouse();
        headerPane.setOnMousePressed(new EventHandler<MouseEvent>()
        {

            @Override
            public void handle(MouseEvent t)
            {
                //System.out.println("Pressed");
                //System.out.println("Mouse : " + t.getX() + " | " + t.getY());
                mouse.setX(t.getX());
                mouse.setY(t.getY());
            }
        });
        headerPane.setOnMouseDragged(new EventHandler<MouseEvent>()
        {

            @Override
            public void handle(MouseEvent t)
            {
                //System.out.println("Dragged");
                //System.out.println("Mouse : " + t.getX() + " | " + t.getY());
                mainPane.getScene().getWindow().setX(t.getScreenX() - mouse.getX() - 14);
                mainPane.getScene().getWindow().setY(t.getScreenY() - mouse.getY() - 14);
            }
        });
    }

    public void setStage(final Stage stage)
    {
        this.stage = stage;
    }

    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    // <editor-fold defaultstate="collapsed" desc="MouseBackgrounds">
    @FXML
    private void handleMouseEntered(MouseEvent event)
    {
        btnFlat.setStyle("-fx-background-color: #3b92c4;");
    }

    @FXML
    private void handleMouseExited(MouseEvent event)
    {
        btnFlat.setStyle("-fx-background-color: #2c2c2c;");
    }

    @FXML
    void btnExitMouseEntered(MouseEvent event)
    {
        btnExit.setStyle("-fx-background-color: #4c4c4c;");
    }

    @FXML
    void btnExitMouseExited(MouseEvent event)
    {
        btnExit.setStyle("-fx-background-color: #3c3c3c;");
    }

    @FXML
    void btnMinimizeMouseEntered(MouseEvent event)
    {
        btnMinimize.setStyle("-fx-background-color: #4c4c4c;");
    }

    @FXML
    void btnMinimizeMouseExited(MouseEvent event)
    {
        btnMinimize.setStyle("-fx-background-color: #3c3c3c;");
    }
    // </editor-fold>    
}
