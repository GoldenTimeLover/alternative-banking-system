package ui.components.notificationArea;

import core.entities.Notification;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import resources.paths.Paths;
import ui.components.SubController;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static com.sun.javafx.scene.control.skin.Utils.getResource;

public class NotificationAreaController extends SubController {




    @FXML
    private VBox notificationBox;


    public void clear(){

        notificationBox.getChildren().clear();
    }
    public void createNotification(String title,int date,String content) {

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource(Paths.singleNotification));
            Node singleWordTile = loader.load();

            singleNotificationController singleNotController = loader.getController();
            singleNotController.titleText.setText(title);
            singleNotController.dateText.setText("Date: " + date);
            singleNotController.contentText.setText(content);

            notificationBox.getChildren().add(singleWordTile);

        }catch (IOException e){
            e.printStackTrace();
        }
    }


}
