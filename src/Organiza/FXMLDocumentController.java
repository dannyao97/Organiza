package Organiza;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.cells.editors.base.JFXTreeTableCell;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
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
    private JFXButton btnFlat;
    @FXML
    private Pane mainPane;
    @FXML
    private Pane headerPane;
    @FXML
    private JFXButton btnExit;
    @FXML
    private JFXButton btnMinimize;
    @FXML
    private JFXButton btnMovies;
    @FXML
    private ScrollPane scrollMovies;
    @FXML
    private ScrollPane scrollTV;
    @FXML
    private TableView tableMovies;

    private static Stage stage;
    private Mouse mouse;
    private static double xOffset = 0;
    private static double yOffset = 0;
    private DataGrabber data;
    private ObservableList<TableImage> imageList = FXCollections.observableArrayList();

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

        setTable();
    }

    public void setTable()
    {
        TableColumn<TableImage, ImageView> col = new TableColumn<>();
        tableMovies.getColumns().add(col);
        
        imageList.add(new TableImage(new ImageView("https://images-na.ssl-images-amazon.com/images/M/MV5BMjA5NDQyMjc2NF5BMl5BanBnXkFtZTcwMjg5ODcyMw@@._V1_SX300.jpg")));
        col.setPrefWidth(50);
        col.setCellValueFactory(new PropertyValueFactory<TableImage, ImageView>("image"));

        int i = 0;
        while (i != 10)
        {

            try
            {
                imageList.add(new TableImage(new ImageView("https://images-na.ssl-images-amazon.com/images/M/MV5BMjA5NDQyMjc2NF5BMl5BanBnXkFtZTcwMjg5ODcyMw@@._V1_SX300.jpg")));
            } catch (Exception e)
            {
            }
            i++;
        }
        tableMovies.setItems(imageList);
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
