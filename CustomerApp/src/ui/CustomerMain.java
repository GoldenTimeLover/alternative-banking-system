package ui;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import ui.login.CustomerLoginController;
import utils.CustomerPaths;
import utils.http.CustomerHttpClient;

import java.io.IOException;

public class CustomerMain extends Application {

    private CustomerLoginController customerLoginController;

    @Override
    public void start(Stage primaryStage) throws Exception {


        primaryStage.setTitle("A.B.S - Alternative Banking System");
        FXMLLoader loader = new FXMLLoader(getClass().getResource(CustomerPaths.CUSTOMER_LOGIN));
        Parent root = loader.load();


        //get login controller
        customerLoginController = loader.getController();


        primaryStage.setScene(new Scene(root, 1050, 600));
        primaryStage.getScene().getStylesheets().add(CustomerPaths.LIGHT_PRIMARY_THEME);
        primaryStage.getScene().getStylesheets().add(CustomerPaths.LIGHT_BODY_THEME);

        customerLoginController.setPrimaryStage(primaryStage);
        customerLoginController.initialize(primaryStage);



        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {

                String finalUrl = HttpUrl
                        .parse(CustomerPaths.LOGOUT)
                        .newBuilder()
                        .addQueryParameter("username",customerLoginController.currentUser)
                        .build()
                        .toString();

                CustomerHttpClient.runAsync(finalUrl,"GET",null, new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {

                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    }
                });

                try {
                    stop();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        primaryStage.show();



    }

    @Override
    public void stop() throws Exception {
        CustomerHttpClient.shutdown();
        customerLoginController.primaryController.close();
    }


    public static void main(String[] args)
    {
        launch(args);
    }
}
