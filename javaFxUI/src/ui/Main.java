package ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.image.Image;
import javafx.stage.Stage;
import resources.paths.Paths;



public class Main extends Application{



    /*
    main function for the program, set's initial title calls the fxml loader and launches the
    primary scene which calls other components as needed.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {


        primaryStage.setTitle("A.B.S - Alternative Banking System");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("primaryScene.fxml"));
        Parent root = loader.load();
        primaryStage.setScene(new Scene(root, 1050, 600));

        primaryStage.getScene().getStylesheets().add(Paths.LIGHT_PRIMARY_THEME);
        primaryStage.getScene().getStylesheets().add(Paths.LIGHT_BODY_THEME);

        primaryStage.getIcons().add(new Image("/resources/logo.png"));

        //Set the Stage
        PrimaryController primaryController = loader.getController();
        primaryController.setPrimaryStage(primaryStage);
        //show the stage
        primaryStage.show();



    }

    public static void main(String[] args)
    {
        launch(Main.class);
    }
}
