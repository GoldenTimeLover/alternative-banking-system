package ui.customerInfo;

import core.entities.Customer;
import core.entities.Loan;
import core.entities.Transaction;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import ui.subcontrollers.CustomerSubController;

import java.util.List;
import java.util.Optional;

public class CustomerInfoController extends CustomerSubController {

    @FXML
    private TableView<Loan> loanerLoansTable;

    @FXML
    private TableView<Loan> lenderLoansTable;

    @FXML
    private TableView<Transaction> accountTransactionsTable;

    @FXML
    private Button depositButton;

    @FXML
    private Button withdrawButton;

    @FXML
    private Spinner<Integer> amountSpinner;



    @FXML
    void depositButtonPressed(ActionEvent event) {


        if (amountSpinner.getValue() == 0){
            return;
        }
        String customerId = mainController.getUserSelectorCB().getValue().getId();

        int amount = amountSpinner.getValue();
        mainController.getEngine().addTransactionToCustomer(customerId,(double) amount, Transaction.TransactionType.DEPOSIT);
        Customer customer = mainController.getEngine().findCustomerById(mainController.getUserSelectorCB().getValue().getId());

        accountTransactionsTable.getItems().clear();
        accountTransactionsTable.getColumns().clear();

        amountSpinner.getValueFactory().setValue(0);

        loadTransactionTable(customer);
    }

    public void clearTables(){

        loanerLoansTable.getItems().clear();
        lenderLoansTable.getItems().clear();
        accountTransactionsTable.getItems().clear();


        loanerLoansTable.getColumns().clear();
        lenderLoansTable.getColumns().clear();
        accountTransactionsTable.getColumns().clear();

        loanerLoansTable.refresh();
        lenderLoansTable.refresh();
        accountTransactionsTable.refresh();
    }
    @FXML
    void withdrawButtonPressed(ActionEvent event) {
        if (amountSpinner.getValue() == 0 ){
            return;
        }
        String customerId = mainController.getUserSelectorCB().getValue().getId();
        int amount = amountSpinner.getValue();
        Customer customer = mainController.getEngine().findCustomerById(mainController.getUserSelectorCB().getValue().getId());

        if(amount > customer.getBalance()){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning - trying to overdraft!");

            alert.setContentText("You are trying to withdraw" + amount + " but the customer only has " + customer.getBalance() +
                    " in their account!");

            ButtonType yesButton = new ButtonType("Cool");
            alert.getButtonTypes().setAll(yesButton);
            Optional<ButtonType> result = alert.showAndWait();
            return;

        }
        mainController.getEngine().addTransactionToCustomer(customerId,(double) amount, Transaction.TransactionType.WITHDRAW);

        accountTransactionsTable.getItems().clear();
        accountTransactionsTable.getColumns().clear();

        amountSpinner.getValueFactory().setValue(0);

        loadTransactionTable(customer);
    }

    public void setInfoForCustomerIntoTables(){

        System.out.println("set Info For Customer Into Tables");

//        clearTables();
//
//        Customer customer = mainController.getEngine().findCustomerById(mainController.getUserSelectorCB().getValue().getId());
//        loadGivingLoansTable(customer);
//        loadLendingLoansTable(customer);
//        loadTransactionTable(customer);


        SpinnerValueFactory<Integer> valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0,1000000,0);


        amountSpinner.setValueFactory(valueFactory);

        amountSpinner.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                amountSpinner.increment(0); // won't change value, but will commit editor
            }
        });
    }

    private void loadGivingLoansTable(Customer customer){
        ObservableList<Loan> loans = FXCollections.observableArrayList();

        int size = customer.getGivingLoans().size();
        List<Loan> temp = customer.getGivingLoans();
        for (int i = 0; i <size; i++) {
            loans.add(temp.get(i));
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



        lenderLoansTable.setItems(loans);
        lenderLoansTable.getColumns().addAll(idColumn,
                ownerColumn,amountColumn,
                lengthColumn , interestColumn,
                timeBetweenCol, categoryColumn,
                statusColumn );


    }
    private void loadLendingLoansTable(Customer customer){

        ObservableList<Loan> loans = FXCollections.observableArrayList();

        int size = customer.getTakingLoans().size();
        List<Loan> temp = customer.getTakingLoans();
        for (int i = 0; i <size; i++) {
            loans.add(temp.get(i));
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


        loanerLoansTable.setItems(loans);
        loanerLoansTable.getColumns().addAll(idColumn,
                ownerColumn,amountColumn,
                lengthColumn , interestColumn,
                timeBetweenCol, categoryColumn,
                statusColumn );

    }
    private void loadTransactionTable(Customer customer){
        ObservableList<Transaction> transactions = FXCollections.observableArrayList();

        int size = customer.getTransactions().size();
        List<Transaction> temp = customer.getTransactions();
        for (int i = 0; i <size; i++) {
            transactions.add(temp.get(i));
        }

        //id
        TableColumn<Transaction,Double> amountCol = new TableColumn<>("Amount");
        amountCol.setMinWidth(100);
        amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));

        //date
        TableColumn<Transaction,Integer> dateCol = new TableColumn<>("Date");
        dateCol.setMinWidth(100);
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));

        //type
        TableColumn<Transaction,String> typeCol = new TableColumn<>("Type");
        typeCol.setMinWidth(100);
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));


        //balance before

        TableColumn<Transaction,Double> beforeCol = new TableColumn<>("Balance Before");
        beforeCol.setMinWidth(100);
        beforeCol.setCellValueFactory(new PropertyValueFactory<>("balanceBefore"));

        // balance after
        TableColumn<Transaction,Double> afterCol = new TableColumn<>("Balance After");
        afterCol.setMinWidth(100);
        afterCol.setCellValueFactory(new PropertyValueFactory<>("balanceAfter"));






        accountTransactionsTable.setItems(transactions);
        accountTransactionsTable.getColumns().addAll(amountCol,dateCol,typeCol,beforeCol,afterCol);
    }
}
