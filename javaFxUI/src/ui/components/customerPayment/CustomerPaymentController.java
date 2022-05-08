package ui.components.customerPayment;

import core.Exceptions.NotEnoughMoneyException;
import core.entities.Loan;
import core.entities.Notification;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import ui.components.SubController;
import ui.components.notificationArea.NotificationAreaController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CustomerPaymentController extends SubController {


    @FXML private VBox notificationArea;
    @FXML private NotificationAreaController notificationAreaController;


    @FXML
    private Button payCurrButton;
    @FXML
    private Button payEntireLoanButton;
    @FXML
    private TableView<Loan> unpaidLoansTable;

    private Loan selectedLoan;



    @FXML
    public void initialize(){
        if(notificationAreaController != null){
            notificationAreaController.setMainController(this.mainController);
        }
        else{
            System.out.println("The loading of the controller in CustomerPaymentController did not work");
        }
    }


    public void prepareNotificationArea(){

        notificationAreaController.clear();
        String id = mainController.getUserSelectorCB().getValue().getId();


        List<Notification> notifications = mainController.getEngine().getNotifications().get(id);

        for (Notification n: notifications) {
            notificationAreaController.createNotification(n.getTitle(),n.getDate(),n.getContent());
        }


        prepareTable();
        unlockPaymentButtons();
    }

    private void unlockPaymentButtons(){
        this.payCurrButton.setDisable(false);
        this.payEntireLoanButton.setDisable(false);
    }

    private void prepareTable(){

        unpaidLoansTable.getItems().clear();
        unpaidLoansTable.getColumns().clear();

        String id = mainController.getUserSelectorCB().getValue().getId();
        ObservableList<Loan> myActiveLoans = FXCollections.observableArrayList();
        for(Loan loan : mainController.getEngine().getActiveLoans()){

            if (loan.getOwnerName().equals(id)){
                myActiveLoans.add(loan);
            }
        }

        //id
        TableColumn<Loan,String> idColumn = new TableColumn<>("Loan ID");
        idColumn.setMinWidth(100);
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));



        //owner
        TableColumn<Loan,String> ownerColumn = new TableColumn<>("Owner");
        ownerColumn.setMinWidth(100);
        ownerColumn.setCellValueFactory(c-> new SimpleStringProperty(c.getValue().getOwnerName()));

        //category
        TableColumn<Loan,String> categoryColumn = new TableColumn<>("Category");
        categoryColumn.setMinWidth(100);
        categoryColumn.setCellValueFactory(c-> new SimpleStringProperty(c.getValue().getCategory()));

        //start date
        TableColumn<Loan,Integer> startDateColumn = new TableColumn<>("Start Date");
        startDateColumn.setMinWidth(100);
        startDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));


        //amount
        TableColumn<Loan,Double> amountColumn = new TableColumn<>("Amount");
        amountColumn.setMinWidth(100);
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));


        //status
        TableColumn<Loan,String> statusColumn = new TableColumn<>("Status");
        statusColumn.setMinWidth(100);
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        //length of time
        TableColumn<Loan,Integer> lengthColumn = new TableColumn<>("Time Length");
        lengthColumn.setMinWidth(100);
        lengthColumn.setCellValueFactory(new PropertyValueFactory<>("lengthOfTime"));

        //Intreset Rate
        TableColumn<Loan,Integer> interestColumn = new TableColumn<>("Interest Rate");
        interestColumn.setMinWidth(100);
        interestColumn.setCellValueFactory(new PropertyValueFactory<>("interestRate"));


        //Time between each payment
        TableColumn<Loan,Integer> timeBetweenCol = new TableColumn<>("pays every");
        timeBetweenCol.setMinWidth(100);
        timeBetweenCol.setCellValueFactory(new PropertyValueFactory<>("timeBetweenPayments"));

        unpaidLoansTable.setItems(myActiveLoans);
        unpaidLoansTable.getColumns().addAll(idColumn,timeBetweenCol,startDateColumn,amountColumn,
                statusColumn,lengthColumn,interestColumn);


    }

    @FXML
    void payCurrButtonPressed(ActionEvent event) {
        ObservableList<Loan> selectedItems = this.unpaidLoansTable.getSelectionModel().getSelectedItems();

        if(selectedItems.size() == 0){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning - No loan selected!");

            alert.setContentText("You are trying to pay a loan but no loan is selected.\nplease try" +
                    "selecting one of the loans in the table that need paying.");

            ButtonType yesButton = new ButtonType("Ok");
            alert.getButtonTypes().setAll(yesButton);
            Optional<ButtonType> result = alert.showAndWait();
        }

        else{

            this.selectedLoan = selectedItems.get(0);


            try {
                mainController.getEngine().payCurrLoan(selectedLoan);
            } catch (NotEnoughMoneyException e) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning - Not enough money!");

                alert.setContentText("You are trying to make a payment on the loan '" + selectedLoan.getId()+" '" +
                        " but the amount required is " + e.amountTryingToExtract + "$ and you have only " + e.balance +"$ in your account." );

                ButtonType yesButton = new ButtonType("Ok");
                alert.getButtonTypes().setAll(yesButton);
                Optional<ButtonType> result = alert.showAndWait();
            }
        }

    }

    @FXML
    void payEntireLoanButtonPressed(ActionEvent event) {
        ObservableList<Loan> selectedItems = this.unpaidLoansTable.getSelectionModel().getSelectedItems();


        // if no loan was selected from the table
        if(selectedItems.size() == 0){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning - No loan selected!");

            alert.setContentText("You are trying to pay a loan but no loan is selected.\nplease try" +
                    "selecting one of the loans in the table that need paying.");

            ButtonType yesButton = new ButtonType("Ok");
            alert.getButtonTypes().setAll(yesButton);
            Optional<ButtonType> result = alert.showAndWait();
            return;
        }else {

            this.selectedLoan = selectedItems.get(0);

            // if the customer doesn't have enough money to cover the loan
            if(selectedLoan.getCompleteAmountToBePaid() - selectedLoan.getAmountPaidUntilNow() > selectedLoan.getBorrower().getBalance()){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning - Not enough money!");

                alert.setContentText("You are trying to pay off the entire loan '" + selectedLoan.getId()+" '" +
                        " but the amount required is " + (selectedLoan.getCompleteAmountToBePaid() - selectedLoan.getAmountPaidUntilNow())
                        + "$ and you have only " + selectedLoan.getBorrower().getBalance() +"$ in your account." );

                ButtonType yesButton = new ButtonType("Ok");
                alert.getButtonTypes().setAll(yesButton);
                Optional<ButtonType> result = alert.showAndWait();
                return;
            }else{
                // pay off entire loan
                mainController.getEngine().payEntireLoan(selectedLoan);
            }

        }
    }
}
