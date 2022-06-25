package ui.customerPanel;

import core.dtos.AdminLoanDTO;
import core.dtos.CustomerSnapshot;
import core.dtos.LoansDTO;
import core.entities.Loan;
import core.entities.Transaction;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import ui.customerAddLoanPanel.CustomerAddLoanPanelController;
import ui.customerBuyLoan.CustomerBuyLoanController;
import ui.customerInfo.CustomerInfoController;
import ui.customerMatching.CustomerMatchingController;
import ui.customerPayment.CustomerPaymentController;
import ui.subcontrollers.CustomerSubController;
import utils.CustomerPaths;
import utils.http.UserSnapshotRefresher;


import java.io.Closeable;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class CustomerPanelController extends CustomerSubController implements Closeable {


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

    // Buying loan panel
    @FXML private ScrollPane customerBuyingLoanComponent;
    @FXML public CustomerBuyLoanController customerBuyingLoanComponentController;

    @FXML
    private Button informationButton;

    @FXML
    private Button scrambleButton;

    @FXML
    private Button paymentButton;

    @FXML
    private Button buyLoanButton;

    @FXML
    private ScrollPane centerContent;

    @FXML
    private Label balanceText;

    @FXML
    private Button addLoanButton;



    private final StringProperty balanceProperty = new SimpleStringProperty();
    private Timer timer;
    private TimerTask balanceRefresher;

    @FXML
    public void initialize() {
        balanceProperty.set("0");
        balanceText.textProperty().bind(Bindings.concat(balanceProperty,"$"));

    }
    @FXML
    public void initcomps(){
        initCustomerInfoComponent();
        initCustomerPaymentComponent();
        initCustomerMatchingComponent();
        initCustomerAddLoanComponent();
        initCustomerBuyLoanComponent();
        centerContent.setContent(customerInfoComponent);
    }

    public void bindButtons(){
        paymentButton.disableProperty().bind(mainController.isRewindModeProperty);
        addLoanButton.disableProperty().bind(mainController.isRewindModeProperty);
        scrambleButton.disableProperty().bind(mainController.isRewindModeProperty);
    }
    /*
    Response to the pressing of the information button in the Customer Panel.
    - clears data stored in the tables
    - fill with new data
    - set the center of the border pane to the information panel
     */
    @FXML
    void informationButtonPressed(ActionEvent event) {
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

    private void updateSnapshot(CustomerSnapshot snapshot) {
        Platform.runLater(() -> {

            if(snapshot == null || snapshot.loansDTO == null){
                balanceProperty.setValue("0");
            }else{
                balanceProperty.set(String.valueOf(snapshot.loansDTO.balance));

                List<Loan> loansCustomerIsAskingFor = new ArrayList<>();
                List<Loan> activeLoansPayingFor = new ArrayList<>();

                for (int i = 0; i < snapshot.loansDTO.loanList.size(); i++) {
                    loansCustomerIsAskingFor.add(new Loan(snapshot.loansDTO.loanList.get(i),snapshot.loansDTO.loanList.get(i).owenerName));
                    if (snapshot.loansDTO.loanList.get(i).status.equals(Loan.LoanStatus.RISK) ||
                            snapshot.loansDTO.loanList.get(i).status.equals(Loan.LoanStatus.ACTIVE)){
                        activeLoansPayingFor.add(new Loan(snapshot.loansDTO.loanList.get(i),snapshot.loansDTO.loanList.get(i).owenerName));
                    }
                }

                List<Loan> loansTheCustomerGave = new ArrayList<>();
                for (int i = 0; i < snapshot.loansDTO.loansCustomerGaveToOthers.size(); i++) {
                    loansTheCustomerGave.add(new Loan(snapshot.loansDTO.loansCustomerGaveToOthers.get(i),snapshot.loansDTO.loansCustomerGaveToOthers.get(i).owenerName));
                }




                List<Transaction> transactions = new ArrayList<>(snapshot.transactionsDTO.transactions);


                customerInfoComponentController.borrowingLoansObservableList.clear();
//                customerInfoComponentController.givingLoansObservableList.clear();
                customerInfoComponentController.transactionObservableList.clear();


                customerInfoComponentController.borrowingLoansObservableList.addAll(loansCustomerIsAskingFor);
//                customerInfoComponentController.givingLoansObservableList.addAll(loansTheCustomerGave);
                customerInfoComponentController.transactionObservableList.addAll(transactions);


                System.out.println(customerInfoComponentController.givingLoansObservableList.size());

                if(loansTheCustomerGave.size() != customerInfoComponentController.givingLoansObservableList.size()||
                        checkLoansStatusChange(loansTheCustomerGave,customerInfoComponentController.givingLoansObservableList)){
                    customerInfoComponentController.givingLoansObservableList.clear();
                    customerInfoComponentController.givingLoansObservableList.addAll(loansTheCustomerGave);
                }

                if(activeLoansPayingFor.size() != customerPaymentComponentController.paytingLoansObservableList.size()||
                        checkLoansStatusChange(activeLoansPayingFor,customerPaymentComponentController.paytingLoansObservableList)){
                    customerPaymentComponentController.paytingLoansObservableList.clear();
                    customerPaymentComponentController.paytingLoansObservableList.addAll(activeLoansPayingFor);
                }


                List<AdminLoanDTO> forSale = snapshot.forSale;


                if(forSale.size() != customerBuyingLoanComponentController.forSaleLoanObservable.size()||
                        checkLoanStatusChangeDTO(forSale,customerBuyingLoanComponentController.forSaleLoanObservable)){
                    customerBuyingLoanComponentController.forSaleLoanObservable.clear();
                    customerBuyingLoanComponentController.forSaleLoanObservable.addAll(forSale);
                }

                mainController.SpeardInfoToAll(snapshot);

                if(mainController.notificationDTO != null && mainController.notificationDTO.notificationList != null
                &&customerPaymentComponentController.notificationObservableList.size() !=mainController.notificationDTO.notificationList.size() ){
                    customerPaymentComponentController.prepareNotificationArea();
                }

                if(snapshot.isRewindMode && !centerContent.getContent().equals(customerInfoComponent)){
                    centerContent.setContent(customerInfoComponent);
                }



            }
        });
    }

    public boolean checkLoanStatusChangeDTO(List<AdminLoanDTO> activeLoansPayingFor,ObservableList<AdminLoanDTO> payingLoansObservable){
        Map<String,String> status = new HashMap<>();
        if(activeLoansPayingFor == null || payingLoansObservable == null){
            return false;
        }
        for (int i = 0; i < activeLoansPayingFor.size(); i++) {
            status.put(activeLoansPayingFor.get(i).getId(),activeLoansPayingFor.get(i).getStatus().toString());
        }

        for (int i=0;i<payingLoansObservable.size();i++){
            if(!status.get(payingLoansObservable.get(i).getId()).equals(payingLoansObservable.get(i).getStatus().toString())){
                return true;
            }
        }
        return false;
    }

    public boolean checkLoansStatusChange(List<Loan> activeLoansPayingFor, ObservableList<Loan> payingLoansObservable){
        Map<String,String> status = new HashMap<>();
        if(activeLoansPayingFor == null || payingLoansObservable == null){
            return false;
        }
        for (int i = 0; i < activeLoansPayingFor.size(); i++) {
            status.put(activeLoansPayingFor.get(i).getId(),activeLoansPayingFor.get(i).getStatus().toString());
        }

        for (int i=0;i<payingLoansObservable.size();i++){
            if(!status.get(payingLoansObservable.get(i).getId()).equals(payingLoansObservable.get(i).getStatus().toString())){
                return true;
            }
        }
        return false;
    }

    public void startListRefresher() {
        balanceRefresher= new UserSnapshotRefresher(mainController.getUsername(),
                this::updateSnapshot);
        timer = new Timer();
        timer.schedule(balanceRefresher, 500, 500);
    }

    public void updateAllPanels(){

        initCustomerInfoComponent();
        initCustomerPaymentComponent();
        initCustomerMatchingComponent();
        initCustomerAddLoanComponent();
        customerInfoComponentController.setInfoForCustomerIntoTables();

        balanceText.setId("balance");


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
            assert url != null;
            customerInfoComponent = loader.load(url.openStream());
            customerInfoComponentController = loader.getController();
            customerInfoComponentController.setMainController(mainController);
            customerInfoComponentController.setInfoForCustomerIntoTables();
            customerInfoComponentController.bindButtons();


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
            customerPaymentComponentController.prepareTable();
            customerPaymentComponentController.unlockPaymentButtons();
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

    private void initCustomerBuyLoanComponent(){

        FXMLLoader loader = new FXMLLoader();
        URL url = getClass().getResource(CustomerPaths.CUSTOMER_BUY_LOAN);
        loader.setLocation(url);
        try {
            assert url != null;
            customerBuyingLoanComponent = loader.load(url.openStream());
            customerBuyingLoanComponentController = loader.getController();
            customerBuyingLoanComponentController.setMainController(mainController);
            customerBuyingLoanComponentController.prepareTable();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void buyLoanButtonPressed(ActionEvent event){
        centerContent.setContent(customerBuyingLoanComponent);
    }

    @Override
    public void close() throws IOException {
        balanceProperty.set("0");
        if ( timer != null) {
            timer.cancel();
            balanceRefresher.cancel();
        }
    }
}
