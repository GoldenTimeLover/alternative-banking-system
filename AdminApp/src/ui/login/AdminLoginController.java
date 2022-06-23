package ui.login;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import resources.paths.Paths;
import ui.PrimaryController;
import utils.http.AdminHttpClient;
import utils.resources.AdminPaths;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public class AdminLoginController {


    private Stage primaryStage;
    public PrimaryController adminMainController;
    public String currentUser = null;

    @FXML
    private TextField userNameTextField;

    @FXML
    private Label errorMessageLabel;

    @FXML
    private Button loginButton;


    private final StringProperty errorMsgProperty = new SimpleStringProperty();

    @FXML
    public void initialize(Stage primaryStage) {
        this.primaryStage = primaryStage;
        errorMessageLabel.textProperty().bind(errorMsgProperty);
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @FXML
    void EnterButtonClicked(KeyEvent event) {

    }

    @FXML
    void loginButtonClicked(ActionEvent event) {


        String userName = userNameTextField.getText();
        if (userName.isEmpty()) {
            System.out.println("empty login info ");
            errorMsgProperty.set("User name is empty. You can't login with empty user name");
            return;
        }

        //noinspection ConstantConditions
        String finalUrl = HttpUrl
                .parse(AdminPaths.LOGIN_PAGE)
                .newBuilder()
                .addQueryParameter("adminUsername", userName)
                .build()
                .toString();


        AdminHttpClient.runAsync(finalUrl,"GET",null ,new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
//                        errorMsgProperty.set("Something went wrong ):")
                                System.out.println("something went wrong")
                );
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    String responseMessage = response.body().string();
                    Platform.runLater(() ->
                            errorMsgProperty.set("Login failed: " + responseMessage));
                } else {
                    Platform.runLater(() -> {
                        try{
                            System.out.println("i am in log in page!");
                            currentUser = userName;


                            FXMLLoader loader = new FXMLLoader(getClass().getResource(AdminPaths.PRIMARY));
                            Parent root = loader.load();
                            adminMainController = loader.getController();



                            // close app window for a second
                            primaryStage.hide();

                            // set the scene to the main screen
                            primaryStage.setScene(new Scene(root, 1050, 600));
                            primaryStage.getScene().getStylesheets().add(Paths.LIGHT_PRIMARY_THEME);
                            primaryStage.getScene().getStylesheets().add(Paths.LIGHT_BODY_THEME);

                            // show the app again
                            primaryStage.show();
                            adminMainController.initialize(primaryStage);
                            adminMainController.adminPanel();
                            adminMainController.adminNameProperty.set(userName);
                        }
                        catch(Exception ignore) {}
                    });
                }
                Objects.requireNonNull(response.body()).close();
                response.close();
            }
        });
    }

    @FXML
    void quitButtonClicked(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    void userNameKeyTyped(KeyEvent event) {
        errorMsgProperty.set("");
    }

}