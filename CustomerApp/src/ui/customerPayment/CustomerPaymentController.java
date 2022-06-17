package ui.customerPayment;

import core.Exceptions.LoanProccessingException;
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

import ui.notificationArea.NotificationAreaController;
import ui.subcontrollers.CustomerSubController;

import java.util.List;

public class CustomerPaymentController extends CustomerSubController {


//    @FXML private VBox notificationArea;
//    @FXML private NotificationAreaController notificationAreaController;


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



    private Loan selectedLoan;



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
    }


    public void prepareNotificationArea(){

//        notificationAreaController.clear();
//        String id = mainController.getUserSelectorCB().getValue().getId();
//
//
//        List<Notification> notifications = mainController.getEngine().getNotifications().get(id);
//
//        for (Notification n: notifications) {
//            notificationAreaController.createNotification(n.getTitle(),n.getDate(),n.getContent());
//        }


//        prepareTable();
        unlockPaymentButtons();
    }

    private void unlockPaymentButtons(){
        this.payCurrButton.setDisable(false);
        this.payEntireLoanButton.setDisable(false);
        this.payRiskDebt.setDisable(false);
    }

    private void prepareTable(){

        unpaidLoansTable.getItems().clear();
        unpaidLoansTable.getColumns().clear();

        String id = mainController.getCustomerId();
        ObservableList<Loan> myActiveLoans = FXCollections.observableArrayList();
//        for(Loan loan : mainController.info.loanList){
//
//            if (loan.getOwnerName().equals(id)){
//                myActiveLoans.add(loan);
//            }
//        }

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

        unpaidLoansTable.setItems(myActiveLoans);
        unpaidLoansTable.getColumns().addAll(idColumn,timeBetweenCol,startDateColumn,amountColumn,
                statusColumn,lengthColumn,interestColumn,aaaa,bbb,ccc);


    }

    @FXML
    void payCurrButtonPressed(ActionEvent event) {
        ObservableList<Loan> selectedItems = this.unpaidLoansTable.getSelectionModel().getSelectedItems();

        if(selectedItems.size() == 0){
            mainController.showAlert(Alert.AlertType.WARNING,"Warning - No loan selected!","You are trying to pay a loan but no loan is selected.\nplease try" +
                    "selecting one of the loans in the table that need paying.");
        }
        else{
            this.selectedLoan = selectedItems.get(0);
//            try {
////                mainController.getEngine().payCurrLoan(selectedLoan);
//                prepareTable();
//            } catch (NotEnoughMoneyException e) {
//
//                mainController.showAlert(Alert.AlertType.WARNING,"Warning - Not enough money!",
//                        "You are trying to make a payment on the loan '" + selectedLoan.getId()+" '" +
//                                " but the amount required is " + e.amountTryingToExtract + "$ and you have only " + e.balance +"$ in your account.");
//            } catch (LoanProccessingException e) {
//                mainController.showAlert(Alert.AlertType.WARNING,"Warning - Can't pay loan now",e.getMessage());
//            }
        }

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
            if(riskDebtSpinner.getValue() > selectedLoan.getBorrower().getBalance()){
                mainController.showAlert(Alert.AlertType.WARNING,"Warning - Not enough money!","You are trying to pay the debt of '" + selectedLoan.getId()+" '" +
                        " but the amount entered is higher than your balance");
            }else{
                // pay off entire loan
//                mainController.getEngine().payLoanDebtAmount(selectedLoan,riskDebtSpinner.getValue());
                riskDebtSpinner.getValueFactory().setValue(0);
                prepareTable();
            }

        }

    }
    @FXML
    void payEntireLoanButtonPressed(ActionEvent event) {
        ObservableList<Loan> selectedItems = this.unpaidLoansTable.getSelectionModel().getSelectedItems();


        // if no loan was selected from the table
        if(selectedItems.size() == 0){
            mainController.showAlert(Alert.AlertType.WARNING,"Warning - No loan selected!","You are trying to pay a loan but no loan is selected.\nplease try" +
                    "selecting one of the loans in the table that need paying.");
        }else {

            this.selectedLoan = selectedItems.get(0);

            // if the customer doesn't have enough money to cover the loan
            if(selectedLoan.getCompleteAmountToBePaid() - selectedLoan.getAmountPaidUntilNow() > selectedLoan.getBorrower().getBalance()){
                mainController.showAlert(Alert.AlertType.WARNING,"Warning - Not enough money!","You are trying to pay off the entire loan '" + selectedLoan.getId()+" '" +
                        " but the amount required is " + (selectedLoan.getCompleteAmountToBePaid() - selectedLoan.getAmountPaidUntilNow())
                        + "$ and you have only " + selectedLoan.getBorrower().getBalance() +"$ in your account." );
            }else{
                // pay off entire loan
//                mainController.getEngine().payEntireLoan(selectedLoan);
                mainController.showAlert(Alert.AlertType.CONFIRMATION,"Payed of entire loan","You have successfully paid off the entire loan '" + selectedLoan.getId() + "'.\nThe loan is now FINISHED");
                prepareTable();
            }

        }
    }
}
