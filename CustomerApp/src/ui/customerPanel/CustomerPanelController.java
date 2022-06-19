package ui.customerPanel;

import core.dtos.LoansDTO;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringExpression;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import ui.customerAddLoanPanel.CustomerAddLoanPanelController;
import ui.customerInfo.CustomerInfoController;
import ui.customerMatching.CustomerMatchingController;
import ui.customerPayment.CustomerPaymentController;
import ui.subcontrollers.CustomerSubController;
import utils.CustomerPaths;


import java.io.IOException;
import java.net.URL;

public class CustomerPanelController extends CustomerSubController {


    // Information panel
    @FXML private ScrollPane customerInfoComponent;
    @FXML private CustomerInfoController customerInfoComponentController;

    // addLoanComponent panel
    @FXML private ScrollPane addLoanComponent;
    @FXML private CustomerAddLoanPanelController addLoanComponentController;

    // Payment panel
    @FXML private BorderPane customerPaymentComponent;
    @FXML private CustomerPaymentController customerPaymentComponentController;


    // Matching/"Scramble" panel
    @FXML private StackPane customerMatchingComponent;
    @FXML public CustomerMatchingController customerMatchingComponentController;

    @FXML
    private Button informationButton;

    @FXML
    private Button scrambleButton;

    @FXML
    private Button paymentButton;

    @FXML
    private ScrollPane centerContent;

    @FXML
    private Label balanceText;

    @FXML
    private Button addLoanButton;

    @FXML
    public void initcomps(){
        initCustomerInfoComponent();
        initCustomerPaymentComponent();
        initCustomerMatchingComponent();
        initCustomerAddLoanComponent();
        centerContent.setContent(customerInfoComponent);
    }
    /*
    Response to the pressing of the information button in the Customer Panel.
    - clears data stored in the tables
    - fill with new data
    - set the center of the border pane to the information panel
     */
    @FXML
    void informationButtonPressed(ActionEvent event) {
        customerInfoComponentController.clearTables();
        customerInfoComponentController.setInfoForCustomerIntoTables();
        centerContent.setContent(customerInfoComponent);

    }

    @FXML
    void addLoanButtonPressed(ActionEvent event) {
        centerContent.setContent(addLoanComponent);
    }

    /*
    Response to the pressing of the payment button in the Customer Panel.
    - set the center of the border pane to the payment panel
 */
    @FXML
    void paymentButtonPressed(ActionEvent event) {
        customerPaymentComponentController.prepareNotificationArea();
        centerContent.setContent(customerPaymentComponent);
    }

    /*
    Response to the pressing of the Scramble/matching in the Customer Panel.
    - calls the set categories in the sub controller to be filled with needed values
    - set the center of the border pane to the matching component
     */
    @FXML
    void scrambleButtonPressed(ActionEvent event) {


        customerMatchingComponentController.setCategories();
        centerContent.setContent(customerMatchingComponent);

    }


    public void updateAllPanels(){

        initCustomerInfoComponent();
        initCustomerPaymentComponent();
        initCustomerMatchingComponent();
        initCustomerAddLoanComponent();
        customerInfoComponentController.setInfoForCustomerIntoTables();


        String id = mainController.getCustomerId();
        if(mainController.transactionsDTO == null){
            balanceText.setText("0.0");
        }else{
            balanceText.setText(String.valueOf(this.mainController.transactionsDTO.balance));
            customerInfoComponentController.setInfoForCustomerIntoTables();
        }

        balanceText.setId("balance");
        customerInfoComponentController.setInfoForCustomerIntoTables();

        customerInfoComponentController.clearTables();
        customerInfoComponentController.setInfoForCustomerIntoTables();
        centerContent.setContent(customerInfoComponent);
    }

    /**
     * initialises the customer information component and wires it and the main customer
     * component to point at each other.
     */
    private void initCustomerInfoComponent(){

        FXMLLoader loader = new FXMLLoader();
        URL url = getClass().getResource(CustomerPaths.CUSTOMER_INFO);
        loader.setLocation(url);
        try {
            System.out.println("helloooo vietnam!!");
            assert url != null;
            customerInfoComponent = loader.load(url.openStream());
            customerInfoComponentController = loader.getController();
            customerInfoComponentController.setMainController(mainController);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * initialises the customer payment component and wires it and the main customer
     * component to point at each other.
     */
    private void initCustomerPaymentComponent(){
        FXMLLoader loader = new FXMLLoader();
        URL url = getClass().getResource(CustomerPaths.CUSTOMER_PAYMENT);
        loader.setLocation(url);
        try {
            assert url != null;
            customerPaymentComponent = loader.load(url.openStream());
            customerPaymentComponentController = loader.getController();
            customerPaymentComponentController.setMainController(mainController);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * initialises the customer matching component and wires it and the main customer
     * component to point at each other.
     */
    private void initCustomerMatchingComponent(){
        FXMLLoader loader = new FXMLLoader();
        URL url = getClass().getResource(CustomerPaths.CUSTOMER_MATCHING);
        loader.setLocation(url);
        try {
            assert url != null;
            customerMatchingComponent = loader.load(url.openStream());
            customerMatchingComponentController = loader.getController();
            customerMatchingComponentController.setMainController(mainController);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private  void initCustomerAddLoanComponent(){
        FXMLLoader loader = new FXMLLoader();
        URL url = getClass().getResource(CustomerPaths.CUSTOMER_ADD_LOAN);
        loader.setLocation(url);
        try {
            assert url != null;
            addLoanComponent = loader.load(url.openStream());
            addLoanComponentController = loader.getController();
            addLoanComponentController.setMainController(mainController);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
