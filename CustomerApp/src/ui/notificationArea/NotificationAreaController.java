package ui.notificationArea;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import ui.subcontrollers.CustomerSubController;
import utils.CustomerPaths;


import java.io.IOException;

public class NotificationAreaController extends CustomerSubController {




    @FXML
    private VBox notificationBox;


    public void clear(){

        notificationBox.getChildren().clear();
    }
    public void createNotification(String title,int date,String content) {

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource(CustomerPaths.SINGLE_NOTIFICATION));
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
