package ui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import resources.paths.Paths;
import ui.login.AdminLoginController;
import utils.resources.AdminPaths;

import java.awt.*;
import java.util.Optional;


public class AdminMain extends Application {

    /*
main function for the program, set's initial title calls the fxml loader and launches the
primary scene which calls other components as needed.
 */
    @Override
    public void start(Stage primaryStage) throws Exception {


        primaryStage.setTitle("A.B.S - Alternative Banking System");
        FXMLLoader loader = new FXMLLoader(getClass().getResource(AdminPaths.adminLogin));
        Parent root = loader.load();


        //get login controller
        AdminLoginController adminLoginController = loader.getController();


        primaryStage.setScene(new Scene(root, 1050, 600));
        primaryStage.getScene().getStylesheets().add(Paths.LIGHT_PRIMARY_THEME);
        primaryStage.getScene().getStylesheets().add(Paths.LIGHT_BODY_THEME);

        adminLoginController.setPrimaryStage(primaryStage);
        adminLoginController.initialize(primaryStage);



        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                System.out.println("ohhh weee i'm closing down this app!");
            }
        });

        primaryStage.show();



    }

    public static void main(String[] args)
    {
        launch(args);
    }
}
