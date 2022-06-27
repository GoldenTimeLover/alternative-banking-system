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
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import resources.paths.Paths;
import ui.login.AdminLoginController;
import utils.http.AdminHttpClient;
import utils.resources.AdminPaths;

import java.awt.*;
import java.io.IOException;
import java.util.Optional;


public class AdminMain extends Application {


    private AdminLoginController adminLoginController;
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
        adminLoginController = loader.getController();


        primaryStage.setScene(new Scene(root, 1050, 600));
        primaryStage.getScene().getStylesheets().add(Paths.LIGHT_PRIMARY_THEME);
        primaryStage.getScene().getStylesheets().add(Paths.LIGHT_BODY_THEME);

        adminLoginController.setPrimaryStage(primaryStage);
        adminLoginController.initialize(primaryStage);



        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {

                String finalUrl = HttpUrl
                        .parse(AdminPaths.LOGOUT)
                        .newBuilder()
                        .addQueryParameter("username",adminLoginController.currentUser)
                        .build()
                        .toString();

                AdminHttpClient.runAsync(finalUrl,"GET",null, new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {

                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                    }
                });



            }
        });

        primaryStage.show();



    }

    @Override
    public void stop() throws Exception {
        AdminHttpClient.shutdown();
        adminLoginController.adminMainController.close();
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}
