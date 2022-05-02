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



    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.setTitle("A.B.S - Alternative Banking System");

        Parent load = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("primaryScene.fxml")));
        Scene scene = new Scene(load,600,500);

        primaryStage.getIcons().add(new Image("/resources/logo.png"));
        //primaryStage.getScene().getStylesheets().add(BodyComponentsPaths.LIGHT_MAIN_THEME);


        primaryStage.setScene(scene);
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
