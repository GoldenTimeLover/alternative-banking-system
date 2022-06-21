package ui;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import ui.login.CustomerLoginController;
import utils.CustomerPaths;

public class CustomerMain extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {


        primaryStage.setTitle("A.B.S - Alternative Banking System");
        FXMLLoader loader = new FXMLLoader(getClass().getResource(CustomerPaths.CUSTOMER_LOGIN));
        Parent root = loader.load();


        //get login controller
        CustomerLoginController customerLoginController = loader.getController();


        primaryStage.setScene(new Scene(root, 1050, 600));
        primaryStage.getScene().getStylesheets().add(CustomerPaths.LIGHT_PRIMARY_THEME);
        primaryStage.getScene().getStylesheets().add(CustomerPaths.LIGHT_BODY_THEME);

        customerLoginController.setPrimaryStage(primaryStage);
        customerLoginController.initialize(primaryStage);



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
