package Organiza;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author Daniel
 */
public class Driver extends Application
{
    @Override
    public void start(final Stage stage) throws Exception
    {
        final FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLDocument.fxml"));

        final Parent root = (Parent) loader.load();
        final FXMLDocumentController controller = loader.getController();
        controller.setStage(stage);

        Scene scene = new Scene(root);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        //ResizeHelper.addResizeListener(stage);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        launch(args);
    }

}
