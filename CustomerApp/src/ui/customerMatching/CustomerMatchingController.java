package ui.customerMatching;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import core.dtos.LoansDTO;
import core.entities.Loan;

import core.entities.Transaction;
import core.utils.utils;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import utils.CustomerPaths;
import javafx.scene.layout.StackPane;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import ui.subcontrollers.CustomerSubController;
import utils.http.CustomerHttpClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CustomerMatchingController extends CustomerSubController {

    @FXML
    private StackPane myStackPane;
    @FXML
    private Spinner<Integer> loanAmountInput;

    @FXML
    private ListView<String> categorySpinner;

    @FXML
    private Spinner<Integer> mininumIntrerestSpinner;

    @FXML
    private Spinner<Integer> minLoanYazSpinner;

    @FXML
    private Spinner<Integer> maxOpenLoansSpinner;

    @FXML
    private Spinner<Integer> maxPercentageSpinner;

    @FXML
    private Button filterLoansButton;

    @FXML
    private TableView<Loan> availableLoansTable;

    @FXML
    private Button financeLoansButton;


    private ObservableList<String> selectedCategories;
    private ObservableList<Loan> selectedLoans;

    private ObservableList<Loan> matchingLoans;




    @FXML
    public void initialize(){

        //amount field
        SpinnerValueFactory<Integer> amountValueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0,1000000,0);
        loanAmountInput.setValueFactory(amountValueFactory);

        loanAmountInput.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                loanAmountInput.increment(0); // won't change value, but will commit editor
            }
        });


        // interest field
        SpinnerValueFactory<Integer> mininumIntrerestSpinnerValueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0,1000000,0);
        mininumIntrerestSpinner.setValueFactory(mininumIntrerestSpinnerValueFactory);

        mininumIntrerestSpinner.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                mininumIntrerestSpinner.increment(0); // won't change value, but will commit editor
            }
        });

        //yaz field
        SpinnerValueFactory<Integer> minLoanYazSpinnerValueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0,1000000,0);
        minLoanYazSpinner.setValueFactory(minLoanYazSpinnerValueFactory);

        minLoanYazSpinner.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                minLoanYazSpinner.increment(0); // won't change value, but will commit editor
            }
        });

        // max open loans field
        SpinnerValueFactory<Integer> maxOpenLoansSpinnerValueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0,1000000,0);
        maxOpenLoansSpinner.setValueFactory(maxOpenLoansSpinnerValueFactory);

        maxOpenLoansSpinner.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                maxOpenLoansSpinner.increment(0); // won't change value, but will commit editor
            }
        });


        // percentage of loan field
        SpinnerValueFactory<Integer> maxPercentageSpinnerValueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0,1000000,0);
        maxPercentageSpinner.setValueFactory(maxPercentageSpinnerValueFactory);

        maxPercentageSpinner.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                maxPercentageSpinner.increment(0); // won't change value, but will commit editor
            }
        });


        // category list initialization
        categorySpinner.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        categorySpinner.setOnMouseClicked(new EventHandler<Event>() {

            @Override
            public void handle(Event event) {
                selectedCategories =  categorySpinner.getSelectionModel().getSelectedItems();
            }

        });

        availableLoansTable.getSelectionModel().setSelectionMode(
                SelectionMode.MULTIPLE
        );

        availableLoansTable.setOnMouseClicked(new EventHandler<Event>() {

            @Override
            public void handle(Event event) {
                selectedLoans =  availableLoansTable.getSelectionModel().getSelectedItems();

            }

        });


    }

    @FXML
    void filterLoansButtonPressed(ActionEvent event) {


        String customerId = mainController.getCustomerId();
        int amount = loanAmountInput.getValue();
        int minYaz = minLoanYazSpinner.getValue();
        int minInterest = mininumIntrerestSpinner.getValue();
        int amountOfOpenLoans = maxOpenLoansSpinner.getValue();
        int maxPercentageOfLoan = maxPercentageSpinner.getValue();

        if (amount == 0){
            return;
        }
        double balance;
        try{
        balance = mainController.customerSnapshot.loansDTO.balance;
        }catch (NullPointerException e){
            balance = 0.0;
        }
        if (amount > balance){

            mainController.showAlert(Alert.AlertType.WARNING,"Warning - trying to overdraft!","You are trying to withdraw" + amount + " but the customer only has " + balance +
                    " in their account!");
            return;
        }

        List<String> temp;
        if (selectedCategories == null || selectedCategories.size() == 0){
            temp = new ArrayList<>();
        }else{
            temp = new ArrayList<>(selectedCategories);
        }



        try{
            Gson gson = new Gson();
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("customerId",mainController.getUsername());
            jsonObject.addProperty("amount", amount);
            jsonObject.addProperty("interest", minInterest);
            jsonObject.addProperty("time", minYaz);
            jsonObject.addProperty("amountOpen", amountOfOpenLoans);
            jsonObject.addProperty("maxPercent", maxPercentageOfLoan);

            String finalUrl = HttpUrl
                    .parse(CustomerPaths.FILTER_LOANS)
                    .newBuilder()
                    .addQueryParameter("filters",jsonObject.toString())
                    .addQueryParameter("categories",gson.toJson(temp))
                    .build()
                    .toString();
            CustomerHttpClient.runAsync(finalUrl, "GET", null, new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {

                }
                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                    Platform.runLater(()->{
                                try {
                                    String s = response.body().string();
                                    Gson gson = new Gson();
                                    LoansDTO loansDTO = gson.fromJson(s,LoansDTO.class);

                                    availableLoansTable.getItems().clear();
                                    availableLoansTable.getColumns().clear();
                                    loadLendingLoansTable(loansDTO);
                                }
                                catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                    );

                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }




    }

    public void setCategories(){

        String finalUrl = HttpUrl
                .parse(CustomerPaths.GET_CATEGORIES)
                .newBuilder()
                .build()
                .toString();

        CustomerHttpClient.runAsync(finalUrl, "GET", null, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                Platform.runLater(()->{
                            try {
                                categorySpinner.getItems().clear();
                                String s = response.body().string();
                                Gson gson = new Gson();
                                List<String> ls = gson.fromJson(s, new TypeToken<List<String>>(){}.getType());

                                for (String cat : ls){
                                    categorySpinner.getItems().add(cat);
                                }
                            }
                            catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                );

            }
        });
    }



    private void loadLendingLoansTable(LoansDTO loansDTO){

        ObservableList<Loan> loans = FXCollections.observableArrayList();

        int size = loansDTO.loanList.size();
        for (int i = 0; i <size; i++) {
            loans.add(new Loan(loansDTO.loanList.get(i),loansDTO.loanList.get(i).owenerName));
        }



        //id
        TableColumn<Loan,String> idColumn = new TableColumn<>("Loan ID");
        idColumn.setMinWidth(200);
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));



        //owner
        TableColumn<Loan,String> ownerColumn = new TableColumn<>("Owner");
        ownerColumn.setMinWidth(200);
        ownerColumn.setCellValueFactory(c-> new SimpleStringProperty(c.getValue().getOwnerName()));

        //category
        TableColumn<Loan,String> categoryColumn = new TableColumn<>("Category");
        categoryColumn.setMinWidth(200);
        categoryColumn.setCellValueFactory(c-> new SimpleStringProperty(c.getValue().getCategory()));

        //start date
        TableColumn<Loan,Integer> startDateColumn = new TableColumn<>("Start Date");
        startDateColumn.setMinWidth(200);
        startDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));


        //amount
        TableColumn<Loan,Double> amountColumn = new TableColumn<>("Amount");
        amountColumn.setMinWidth(200);
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));


        //status
        TableColumn<Loan,String> statusColumn = new TableColumn<>("Status");
        statusColumn.setMinWidth(200);
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        //length of time
        TableColumn<Loan,Integer> lengthColumn = new TableColumn<>("Time Length");
        lengthColumn.setMinWidth(200);
        lengthColumn.setCellValueFactory(new PropertyValueFactory<>("lengthOfTime"));

        //Intreset Rate
        TableColumn<Loan,Integer> interestColumn = new TableColumn<>("Interest Rate");
        interestColumn.setMinWidth(200);
        interestColumn.setCellValueFactory(new PropertyValueFactory<>("interestRate"));


        //Time between each payment
        TableColumn<Loan,Integer> timeBetweenCol = new TableColumn<>("pays every");
        timeBetweenCol.setMinWidth(200);
        timeBetweenCol.setCellValueFactory(new PropertyValueFactory<>("timeBetweenPayments"));


        availableLoansTable.setItems(loans);
        availableLoansTable.getColumns().addAll(idColumn,
                ownerColumn,amountColumn,
                lengthColumn , interestColumn,
                timeBetweenCol, categoryColumn,
                statusColumn );


    }

    private void clearFilters(){

        this.loanAmountInput.getValueFactory().setValue(0);
        this.categorySpinner.getSelectionModel().clearSelection();
        this.maxOpenLoansSpinner.getValueFactory().setValue(0);
        this.maxPercentageSpinner.getValueFactory().setValue(0);
        this.minLoanYazSpinner.getValueFactory().setValue(0);
        this.mininumIntrerestSpinner.getValueFactory().setValue(0);


    }

    @FXML
    void financeLoansButtonPressed(ActionEvent event) {

        if(selectedLoans == null || selectedLoans.size() == 0){
            return;
        }

        String customerId = mainController.getUsername();
        double amount = (double) loanAmountInput.getValue();
        double amountForEach = utils.round(amount / selectedLoans.size());

        for (int i = 0; i < selectedLoans.size(); i++) {

            Loan curLoan = selectedLoans.get(i);

            String finalUrl = HttpUrl
                    .parse(CustomerPaths.FINANCE_LOAN)
                    .newBuilder()
                    .addQueryParameter("loanId",curLoan.getId())
                    .addQueryParameter("amount", String.valueOf(amountForEach))
                    .addQueryParameter("loanOwner", curLoan.getOwnerName())
                    .addQueryParameter("lenderName", customerId)
                    .addQueryParameter("maxPercentage", String.valueOf(maxPercentageSpinner.getValue()))
                    .build()
                    .toString();

            CustomerHttpClient.runAsync(finalUrl, "GET", null, new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {

                    Platform.runLater(()->{
                            mainController.showAlert(Alert.AlertType.ERROR,"Error","Something went wrong!");
                    });
                }
                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                    Platform.runLater(()->{
                                try {
                                    String s = response.body().string();
                                    mainController.showAlert(Alert.AlertType.INFORMATION,"Success",s);
                                }
                                catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                    );

                }
            });


        }

        clearFilters();

        //clear table
        this.availableLoansTable.getColumns().clear();
        this.availableLoansTable.getItems().clear();

    }
}
