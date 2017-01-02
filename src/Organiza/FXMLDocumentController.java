package Organiza;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import java.awt.Point;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

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
    private JFXButton btnTv;
    @FXML
    private ScrollPane scrollMovies;
    @FXML
    private ScrollPane scrollTV;
    @FXML
    private TableView<TableImage> tableMovies;
    @FXML
    private Pane paneMovieInfo;
    @FXML
    private JFXButton btnHideInfo;

    //Movie Info pane items
    @FXML
    private ImageView imgViewMovie;
    @FXML
    private ImageView imgViewBackground;
    @FXML
    private TextArea textTitle;
    @FXML
    private TextArea textSummary;
    @FXML
    private Label lblRating;
    @FXML
    private Label lblYear;
    @FXML
    private Label lblGenre;
    @FXML
    private Label lblDirector;
    @FXML
    private Label lblActors;
    @FXML
    private Label lblRuntime;
    @FXML
    private JFXComboBox comboSort;
    @FXML
    private Pane paneMovieControl;

    private static Stage stage;
    private Mouse mouse;
    private static double xOffset = 0;
    private static double yOffset = 0;
    private boolean tvSelected = false;
    private DataGrabber data;
    private ObservableList<TableImage> imageList = FXCollections.observableArrayList();
    //Map table coordinate to imdb movie id
    private HashMap<Point, String> movieMap = new HashMap<>();

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
        //data.sendRequest();
        //data.populateMovies();
        //setupTable();
    }

    @FXML
    private void btnHideInfoClicked(ActionEvent event)
    {
        tableMovies.getSelectionModel().clearSelection();

        FadeTransition infoTrans = new FadeTransition(Duration.millis(200), paneMovieInfo);
        infoTrans.setFromValue(1);
        infoTrans.setToValue(0);
        infoTrans.setInterpolator(Interpolator.EASE_OUT);

        FadeTransition scrollTrans = new FadeTransition(Duration.millis(200), scrollMovies);
        scrollTrans.setFromValue(0);
        scrollTrans.setToValue(1);
        scrollTrans.setDelay(Duration.millis(100));
        scrollTrans.setInterpolator(Interpolator.EASE_IN);

        ParallelTransition parallel = new ParallelTransition();
        parallel.getChildren().addAll(infoTrans, scrollTrans);
        parallel.play();

        parallel.setOnFinished(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event)
            {
                paneMovieInfo.setVisible(false);
            }
        });
    }

    @FXML
    private void btnTvClicked(ActionEvent event)
    {
        tvSelected = true;
        btnTv.setStyle("-fx-text-fill: #5CC7EB; -fx-font-weight: bold; -fx-font-size: 22;");
        btnMovies.setStyle("-fx-text-fill: #b9b9b9; -fx-font-weight: normal; -fx-font-size: 22;");
        scrollMovies.setVisible(false);
        scrollTV.setVisible(true);

        if (paneMovieInfo.isVisible())
        {
            btnHideInfoClicked(null);
        }
    }

    @FXML
    private void btnMoviesClicked(ActionEvent event)
    {
        tvSelected = false;
        btnTv.setStyle("-fx-text-fill: #b9b9b9; -fx-font-weight: normal; -fx-font-size: 22;");
        btnMovies.setStyle("-fx-text-fill: #5CC7EB; -fx-font-weight: bold; -fx-font-size: 22;");
        scrollMovies.setVisible(true);
        scrollTV.setVisible(false);

        comboSort.getItems().clear();
        comboSort.getItems().addAll("Title", "Length", "Year");

        paneMovieInfo.setVisible(true);

        if (paneMovieInfo.isVisible())
        {
            btnHideInfoClicked(null);
        }
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

        //setup movie info pane
        Rectangle clip = new Rectangle(1000, 700);
        clip.setLayoutX(paneMovieInfo.getWidth());
        clip.setLayoutY(paneMovieInfo.getHeight());
        paneMovieInfo.setClip(clip);
        paneMovieInfo.setVisible(false);
        /*comboSort.setPromptText("Title");
        comboSort.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>()
        {
            public void changed(ObservableValue<? extends String> observable,
                    String oldValue, String newValue)
            {
                comboSort.setPromptText(newValue);
                //data.sortMovies(newValue);
            }
        });*/

        //Read in movies
        btnMoviesClicked(null);
        data.populateMovies();
        setupTable();
    }

    public void showMovieInfo(Movie movie)
    {
        imgViewMovie.setImage(new Image("file:" + movie.posterURL));
        imgViewBackground.setImage(new Image("file:" + movie.posterURL));
        textTitle.setText(movie.Title);
        textSummary.setText(movie.Plot);
        lblRating.setText(movie.tomatoRating);
        lblYear.setText(movie.Year);
        lblGenre.setText(movie.Genre);
        lblDirector.setText(movie.Director);
        lblActors.setText(movie.Actors);
        lblRuntime.setText(movie.Runtime);
        paneMovieInfo.setVisible(true);

        FadeTransition infoTrans = new FadeTransition(Duration.millis(200), paneMovieInfo);
        infoTrans.setFromValue(0);
        infoTrans.setToValue(1);
        infoTrans.setInterpolator(Interpolator.EASE_IN);
        infoTrans.setDelay(Duration.millis(100));

        FadeTransition scrollTrans = new FadeTransition(Duration.millis(200), scrollMovies);
        scrollTrans.setFromValue(1);
        scrollTrans.setToValue(0);
        scrollTrans.setInterpolator(Interpolator.EASE_OUT);

        ParallelTransition parallel = new ParallelTransition();
        parallel.getChildren().addAll(infoTrans, scrollTrans);
        parallel.play();
    }

    public void setupTable()
    {
        scrollMovies.setFitToWidth(true);
        tableMovies.getSelectionModel().setCellSelectionEnabled(true);
        tableMovies.getSelectionModel().getSelectedCells().addListener(new ListChangeListener<TablePosition>()
        {
            @Override
            public void onChanged(Change change)
            {
                for (TablePosition pos : tableMovies.getSelectionModel().getSelectedCells())
                {
                    //System.out.println("Row: " + pos.getRow() + " Col: " + pos.getColumn());
                    Point selected = new Point(pos.getRow(), pos.getColumn());
                    if (pos.getRow() % 2 == 0)
                    {
                        showMovieInfo(data.getMovie(movieMap.get(selected)));
                    }
                }
            }
        });
        populateTable(data.movieList);
        Pane header = (Pane) tableMovies.lookup("TableHeaderRow");
        if (header != null && header.isVisible())
        {
            header.setMaxHeight(0);
            header.setMinHeight(0);
            header.setPrefHeight(0);
            header.setVisible(false);
            header.setManaged(false);
        }
    }

    public void populateTable(ArrayList<Movie> movieList)
    {
        TableColumn<TableImage, ImageView> col1 = new TableColumn<>("col1");
        ImageView img1 = null;
        TableColumn<TableImage, ImageView> col2 = new TableColumn<>("col2");
        ImageView img2 = null;
        TableColumn<TableImage, ImageView> col3 = new TableColumn<>("col3");
        ImageView img3 = null;
        TableColumn<TableImage, ImageView> col4 = new TableColumn<>("col4");
        ImageView img4 = null;
        TableColumn<TableImage, ImageView> col5 = new TableColumn<>("col5");
        ImageView img5 = null;

        boolean lastRowFilled = false;
        int count = 0;
        int col = 0;
        int row = 0;

        for (Movie movie : movieList)
        {
            col = count % 5;
            //SET Top row
            if (count < 5)
            {
                switch (count)
                {
                    case 0:
                        img1 = new ImageView(new Image("file:" + movie.posterURL));
                        img1.setPreserveRatio(true);
                        img1.setFitWidth(170);
                        col1.setPrefWidth(img1.getFitWidth() + 25);
                        col1.setStyle("-fx-background-color: #1b1b1b;");
                        col1.setCellValueFactory(new PropertyValueFactory<TableImage, ImageView>("image1"));
                        break;
                    case 1:
                        img2 = new ImageView(new Image("file:" + movie.posterURL));
                        img2.setPreserveRatio(true);
                        img2.setFitWidth(170);
                        col2.setPrefWidth(img2.getFitWidth() + 25);
                        col2.setStyle("-fx-background-color: #1b1b1b;");
                        col2.setCellValueFactory(new PropertyValueFactory<TableImage, ImageView>("image2"));
                        break;
                    case 2:
                        img3 = new ImageView(new Image("file:" + movie.posterURL));
                        img3.setPreserveRatio(true);
                        img3.setFitWidth(170);
                        col3.setPrefWidth(img3.getFitWidth() + 25);
                        col3.setStyle("-fx-background-color: #1b1b1b;");
                        col3.setCellValueFactory(new PropertyValueFactory<TableImage, ImageView>("image3"));
                        break;
                    case 3:
                        img4 = new ImageView(new Image("file:" + movie.posterURL));
                        img4.setPreserveRatio(true);
                        img4.setFitWidth(170);
                        col4.setPrefWidth(img4.getFitWidth() + 25);
                        col4.setStyle("-fx-background-color: #1b1b1b;");
                        col4.setCellValueFactory(new PropertyValueFactory<TableImage, ImageView>("image4"));
                        break;
                    case 4:
                        img5 = new ImageView(new Image("file:" + movie.posterURL));
                        img5.setPreserveRatio(true);
                        img5.setFitWidth(170);
                        col5.setPrefWidth(img5.getFitWidth() + 25);
                        col5.setStyle("-fx-background-color: #1b1b1b;");
                        col5.setCellValueFactory(new PropertyValueFactory<TableImage, ImageView>("image5"));
                        break;
                }
            }
            //ELSE set all the other rows
            else
            {
                switch (count % 5)
                {
                    case 0:
                        img1 = new ImageView(new Image("file:" + movie.posterURL));
                        img1.setPreserveRatio(true);
                        img1.setFitWidth(170);
                        break;
                    case 1:
                        img2 = new ImageView(new Image("file:" + movie.posterURL));
                        img2.setPreserveRatio(true);
                        img2.setFitWidth(170);
                        break;
                    case 2:
                        img3 = new ImageView(new Image("file:" + movie.posterURL));
                        img3.setPreserveRatio(true);
                        img3.setFitWidth(170);
                        break;
                    case 3:
                        img4 = new ImageView(new Image("file:" + movie.posterURL));
                        img4.setPreserveRatio(true);
                        img4.setFitWidth(170);
                        break;
                    case 4:
                        img5 = new ImageView(new Image("file:" + movie.posterURL));
                        img5.setPreserveRatio(true);
                        img5.setFitWidth(170);
                        break;
                }
            }

            //Add row to imagelist
            if (count != 0 && (count + 1) % 5 == 0)
            {
                if (count == 4)
                {
                    tableMovies.getColumns().addAll(col1, col2, col3, col4, col5);
                }
                imageList.add(new TableImage(img1, img2, img3, img4, img5));
                imageList.add(new TableImage(null, null, null, null, null));
                img1 = null;
                img2 = null;
                img3 = null;
                img4 = null;
                img5 = null;

                lastRowFilled = true;
            }
            else
            {
                lastRowFilled = false;
            }
            movieMap.put(new Point(row, col), movie.imdbID);

            if (lastRowFilled)
            {
                row += 2;
            }
            count++;
        }

        //IF last row is not filled
        if (!lastRowFilled)
        {
            imageList.add(new TableImage(img1, img2, img3, img4, img5));
            imageList.add(new TableImage(null, null, null, null, null));
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
        btnExit.setStyle("-fx-background-color: transparent;");
    }

    @FXML
    void btnMinimizeMouseEntered(MouseEvent event)
    {
        btnMinimize.setStyle("-fx-background-color: #4c4c4c;");
    }

    @FXML
    void btnMinimizeMouseExited(MouseEvent event)
    {
        btnMinimize.setStyle("-fx-background-color: transparent;");
    }

    @FXML
    void btnHideInfoMouseEntered(MouseEvent event)
    {
        btnHideInfo.setStyle("-fx-text-fill: #a2dff4;");
    }

    @FXML
    void btnHideInfoMouseExited(MouseEvent event)
    {
        btnHideInfo.setStyle("-fx-text-fill: #5cc7eb");
    }

    @FXML
    void btnMoviesMouseEntered(MouseEvent event)
    {
        if (!tvSelected)
        {
            btnMovies.setStyle("-fx-text-fill: #5CC7EB; -fx-font-weight: bold; -fx-font-size: 22; -fx-background-color: #3c3c3c;");
        }
        else
        {
            btnMovies.setStyle("-fx-background-color: #3c3c3c;");
        }
    }

    @FXML
    void btnMoviesMouseExited(MouseEvent event)
    {
        if (!tvSelected)
        {
            btnMovies.setStyle("-fx-text-fill: #5CC7EB; -fx-font-weight: bold; -fx-font-size: 22;");
        }
        else
        {
            btnMovies.setStyle("-fx-background-color: #2c2c2c;");
        }
    }

    @FXML
    void btnTvMouseEntered(MouseEvent event)
    {
        if (tvSelected)
        {
            btnTv.setStyle("-fx-text-fill: #5CC7EB; -fx-font-weight: bold; -fx-font-size: 22; -fx-background-color: #3c3c3c;");
        }
        else
        {
            btnTv.setStyle("-fx-background-color: #3c3c3c;");
        }
    }

    @FXML
    void btnTvMouseExited(MouseEvent event)
    {
        if (tvSelected)
        {
            btnTv.setStyle("-fx-text-fill: #5CC7EB; -fx-font-weight: bold; -fx-font-size: 22;");
        }
        else
        {
            btnTv.setStyle("-fx-background-color: #2c2c2c;");
        }
    }
    // </editor-fold>    
}
