package ui.components.customerPanel;

import core.entities.Customer;
import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;

import resources.paths.Paths;
import ui.components.SubController;

import ui.components.customerInfo.CustomerInfoController;

import java.io.IOException;
import java.net.URL;

public class CustomerPanelController extends SubController {


    @FXML private ScrollPane customerInfoComponent;
    @FXML private CustomerInfoController customerInfoComponentController;



    @FXML
    private Button informationButton;

    @FXML
    private Button scrambleButton;

    @FXML
    private Button paymentButton;

    @FXML
    private ScrollPane centerContent;

    @FXML
    void informationButtonPressed(ActionEvent event) {


        customerInfoComponentController.clearTables();
        customerInfoComponentController.setInfoForCustomerIntoTables();
        centerContent.setContent(customerInfoComponent);

    }

    @FXML
    void paymentButtonPressed(ActionEvent event) {

    }

    @FXML
    void scrambleButtonPressed(ActionEvent event) {

    }


    public void setPanelForCustomer(){

        initCustomerInfoComponent();
        customerInfoComponentController.setInfoForCustomerIntoTables();
        centerContent.setContent(customerInfoComponent);

    }


    private void initCustomerInfoComponent(){

        FXMLLoader loader = new FXMLLoader();
        URL url = getClass().getResource(Paths.customerInfoComp);
        loader.setLocation(url);
        try {
            assert url != null;
            customerInfoComponent = loader.load(url.openStream());
            customerInfoComponentController = loader.getController();
            customerInfoComponentController.setMainController(mainController);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
