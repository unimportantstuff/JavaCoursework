package coursework;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * @author Kartik Nair
 */

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Java Coursework");
        primaryStage.setScene(new Scene(root, 450, 400));
        primaryStage.show();
    }

    @Override
    public void stop() throws IOException {
        // Override the default stop method to make sure handleClose() is called
        Controller.handleClose();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
