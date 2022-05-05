package ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Objects;
import java.util.Optional;

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
        primaryStage.setScene(new Scene(root));

        primaryStage.getIcons().add(new Image("/resources/logo.png"));

        //Set the Stage
        PrimaryController primaryController = loader.getController();
        primaryController.setPrimaryStage(primaryStage);
        //show the stage
        primaryStage.show();






        //Are you sure you want to quit popup

//        primaryStage.setOnCloseRequest(event -> {
//            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
//            alert.setTitle("Exit dialog");
//            alert.setHeaderText(null);
//            alert.setContentText("Are you sure you want to exit?");
//            ButtonType yesButton = new ButtonType("Yes");
//            ButtonType noButton = new ButtonType("No");
//
//            alert.getButtonTypes().setAll(yesButton, noButton);
//            Optional<ButtonType> result = alert.showAndWait();
//            if (result.get() != yesButton){
//                event.consume();
//            }
//        });
    }

    public static void main(String[] args)
    {
        launch(Main.class);
    }
}
