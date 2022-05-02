package ui.components.adminPanel;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import ui.PrimaryController;

public class AdminPanelController {

    public PrimaryController mainController;

    public void setMainController(PrimaryController mainController) {
        this.mainController = mainController;
    }

    @FXML
    private Button increaseYazButton;

    @FXML
    private Button loadFileButton;

    @FXML
    private Button showLoansButton;

    @FXML
    private Button showCustomersButton;



    @FXML
    void increaseYazButtonPressed(ActionEvent event) {
            this.mainController.IncreaseYaz(event);
    }

    @FXML
    void loadFileButtonPressed(ActionEvent event) {

        this.mainController.loadXMLButtonPressed(event);
    }

    @FXML
    void showCustomersButtonPressed(ActionEvent event) {

    }

    @FXML
    void showLoansButtonPressed(ActionEvent event) {

    }

    public void unlockPanelButtons(){


         increaseYazButton.setDisable(false);
         showLoansButton.setDisable(false);
         showCustomersButton.setDisable(false);
    }
}
