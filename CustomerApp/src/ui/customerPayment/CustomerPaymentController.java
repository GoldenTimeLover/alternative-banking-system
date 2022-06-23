package ui.customerPayment;

import core.Exceptions.LoanProccessingException;
import core.Exceptions.NotEnoughMoneyException;
import core.entities.Loan;
import core.entities.Notification;
import core.entities.Transaction;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import ui.notificationArea.NotificationAreaController;
import ui.notificationArea.singleNotificationController;
import ui.subcontrollers.CustomerSubController;
import utils.CustomerPaths;
import utils.http.CustomerHttpClient;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class CustomerPaymentController extends CustomerSubController {


    @FXML private VBox notificationArea;
    @FXML private NotificationAreaController notificationAreaController;


    @FXML
    private Button payCurrButton;
    @FXML
    private Button payEntireLoanButton;

    @FXML
    private Button payRiskDebt;
    @FXML
    private TableView<Loan> unpaidLoansTable;
    @FXML
    private Spinner<Integer> riskDebtSpinner;

    public ObservableList<Loan> paytingLoansObservableList;

    private Loan selectedLoan;

    @FXML
    private VBox notificationBox;
    private int amountOfNotifications;

    @FXML
    public void initialize(){


        SpinnerValueFactory<Integer> riskDebtSpinnerValueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0,10000,0);
        riskDebtSpinner.setValueFactory(riskDebtSpinnerValueFactory);

        riskDebtSpinner.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                riskDebtSpinner.increment(0); // won't change value, but will commit editor
            }
        });

        paytingLoansObservableList = unpaidLoansTable.getItems();
        amountOfNotifications = 0;
    }



    public void prepareNotificationArea(){




        if(mainController.notificationDTO != null && mainController.notificationDTO.notificationList != null){
            for (Notification n: mainController.notificationDTO.notificationList) {

                    createNotification(n.getTitle(),n.getDate(),n.getContent());
            }

        }



    }

    public void createNotification(String title,int date,String content) {

        notificationBox.getChildren().clear();
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
    public void unlockPaymentButtons(){
        this.payCurrButton.setDisable(false);
        this.payEntireLoanButton.setDisable(false);
        this.payRiskDebt.setDisable(false);
    }

    public void prepareTable(){


        //id
        TableColumn<Loan,String> idColumn = new TableColumn<>("Loan ID");
        idColumn.setMinWidth(150);
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));



        //owner
        TableColumn<Loan,String> ownerColumn = new TableColumn<>("Owner");
        ownerColumn.setMinWidth(150);
        ownerColumn.setCellValueFactory(c-> new SimpleStringProperty(c.getValue().getOwnerName()));

        //category
        TableColumn<Loan,String> categoryColumn = new TableColumn<>("Category");
        categoryColumn.setMinWidth(150);
        categoryColumn.setCellValueFactory(c-> new SimpleStringProperty(c.getValue().getCategory()));

        //start date
        TableColumn<Loan,Integer> startDateColumn = new TableColumn<>("Start Date");
        startDateColumn.setMinWidth(150);
        startDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));


        //amount
        TableColumn<Loan,Double> amountColumn = new TableColumn<>("Amount");
        amountColumn.setMinWidth(150);
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));


        //status
        TableColumn<Loan,String> statusColumn = new TableColumn<>("Status");
        statusColumn.setMinWidth(150);
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        //length of time
        TableColumn<Loan,Integer> lengthColumn = new TableColumn<>("Time Length");
        lengthColumn.setMinWidth(150);
        lengthColumn.setCellValueFactory(new PropertyValueFactory<>("lengthOfTime"));

        //Intreset Rate
        TableColumn<Loan,Integer> interestColumn = new TableColumn<>("Interest Rate");
        interestColumn.setMinWidth(150);
        interestColumn.setCellValueFactory(new PropertyValueFactory<>("interestRate"));


        //Time between each payment
        TableColumn<Loan,Integer> timeBetweenCol = new TableColumn<>("pays every");
        timeBetweenCol.setMinWidth(150);
        timeBetweenCol.setCellValueFactory(new PropertyValueFactory<>("timeBetweenPayments"));

        TableColumn<Loan,Double> aaaa = new TableColumn<>("Total to be paid");
        aaaa.setMinWidth(150);
        aaaa.setCellValueFactory(new PropertyValueFactory<>("completeAmountToBePaid"));


        TableColumn<Loan,Double> bbb = new TableColumn<>("paid so far");
        bbb.setMinWidth(150);
        bbb.setCellValueFactory(new PropertyValueFactory<>("amountPaidUntilNow"));



        TableColumn<Loan, Boolean> ccc = new TableColumn<>("did pay this yazs");
        ccc.setMinWidth(150);
        ccc.setCellValueFactory(new PropertyValueFactory<>("paidThisYaz"));


        unpaidLoansTable.getColumns().addAll(idColumn,timeBetweenCol,startDateColumn,amountColumn,
                statusColumn,lengthColumn,interestColumn,aaaa,bbb,ccc);


    }

    @FXML
    void payCurrButtonPressed(ActionEvent event) {
        sendPaymentRequest("payCurr",0);
    }
    @FXML
    void payRiskDebtPressed(ActionEvent event) {
        ObservableList<Loan> selectedItems = this.unpaidLoansTable.getSelectionModel().getSelectedItems();
        // if no loan was selected from the table
        if(selectedItems.size() == 0){
            mainController.showAlert(Alert.AlertType.WARNING,"Warning - No loan selected!","You are trying to pay a loan but no loan is selected.\nplease try" +
                    "selecting one of the loans in the table that need paying.");
        }
        else if(!selectedItems.get(0).getStatus().equals(Loan.LoanStatus.RISK)){
            mainController.showAlert(Alert.AlertType.WARNING,"Warning - Loan not at risk!","You are trying to pay a " +
                    " to pay the debt of a loan that is not in risk mode!");
        }
        else{
            this.selectedLoan = selectedItems.get(0);

            if(riskDebtSpinner.getValue().equals(0)){
                return;
            }
            // if the customer doesn't have enough money to cover the debt
            if(riskDebtSpinner.getValue() > mainController.customerSnapshot.loansDTO.balance){
                mainController.showAlert(Alert.AlertType.WARNING,"Warning - Not enough money!","You are trying to pay the debt of '" + selectedLoan.getId()+" '" +
                        " but the amount entered is higher than your balance");
            }else{
                    sendPaymentRequest("payRisk",riskDebtSpinner.getValue());
            }

        }

    }
    @FXML
    void payEntireLoanButtonPressed(ActionEvent event) {
            sendPaymentRequest("payEntire",0);

    }


    private void sendPaymentRequest(String type,int amount){

        ObservableList<Loan> selectedItems = this.unpaidLoansTable.getSelectionModel().getSelectedItems();

        if(selectedItems.size() == 0){
            mainController.showAlert(Alert.AlertType.WARNING,"Warning - No loan selected!","You are trying to pay a loan but no loan is selected.\nplease try" +
                    "selecting one of the loans in the table that need paying.");
        }
        else{
            this.selectedLoan = selectedItems.get(0);

            String finalUrl = HttpUrl
                    .parse(CustomerPaths.PAY_LOAN)
                    .newBuilder()
                    .addQueryParameter("type",type)
                    .addQueryParameter("user",this.mainController.getUsername())
                    .addQueryParameter("loanId", selectedLoan.getId())
                    .addQueryParameter("amount", String.valueOf(amount))
                    .build()
                    .toString();
            CustomerHttpClient.runAsync(finalUrl, "GET", null, new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {

                }
                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String s = response.body().string();

                }
            });
        }
    }
}
