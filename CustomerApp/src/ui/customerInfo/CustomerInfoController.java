package ui.customerInfo;

import com.google.gson.Gson;
import core.dtos.LoansDTO;
import core.dtos.SingleLoanDTO;
import core.dtos.TransactionsDTO;
import core.entities.Customer;
import core.entities.Loan;
import core.entities.Transaction;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import ui.subcontrollers.CustomerSubController;
import utils.CustomerPaths;
import utils.http.CustomerHttpClient;
import utils.xml.ClientXmlParser;

import java.io.IOException;
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


    public ObservableList<Loan> givingLoansObservableList;
    public ObservableList<Loan> borrowingLoansObservableList;
    public ObservableList<Transaction> transactionObservableList;

    @FXML
    public void initialize(){
        SpinnerValueFactory<Integer> valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0,1000000,0);


        amountSpinner.setValueFactory(valueFactory);

        amountSpinner.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                amountSpinner.increment(0); // won't change value, but will commit editor
            }
        });

        borrowingLoansObservableList = loanerLoansTable.getItems();
        givingLoansObservableList = lenderLoansTable.getItems();
        transactionObservableList = accountTransactionsTable.getItems();
    }


    @FXML
    void depositButtonPressed(ActionEvent event) {
        sendTransaction("deposit");

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
        sendTransaction("withdraw");
    }

    public void setInfoForCustomerIntoTables(){


        clearTables();
        loadGivingLoansTable();
        loadLendingLoansTable();
        loadTransactionTable();


    }

    private void sendTransaction(String type){

        if (amountSpinner.getValue() == 0){
            return;
        }
        String customerId = mainController.getUsername();

        int amount = amountSpinner.getValue();
        Gson gson = new Gson();

        try{
            String finalUrl = HttpUrl
                    .parse(CustomerPaths.ADD_TRANSACTION)
                    .newBuilder()
                    .addQueryParameter("type",type)
                    .addQueryParameter("user",this.mainController.getUsername())
                    .addQueryParameter("amount", String.valueOf(amountSpinner.getValue()))
                    .build()
                    .toString();
            CustomerHttpClient.runAsync(finalUrl, "GET", null, new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {

                }
                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {



                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }


        amountSpinner.getValueFactory().setValue(0);

        loadTransactionTable();
    }

    private void loadGivingLoansTable(){

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



        lenderLoansTable.getColumns().addAll(idColumn,
                ownerColumn,amountColumn,
                lengthColumn , interestColumn,
                timeBetweenCol, categoryColumn,
                statusColumn );


    }
    private void loadLendingLoansTable(){


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


        loanerLoansTable.getColumns().addAll(idColumn,
                ownerColumn,amountColumn,
                lengthColumn , interestColumn,
                timeBetweenCol, categoryColumn,
                statusColumn );

    }
    private void loadTransactionTable(){

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




        accountTransactionsTable.getColumns().addAll(amountCol,dateCol,typeCol,beforeCol,afterCol);
    }
}
