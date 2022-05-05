package ui.components.customerMatching;

import core.entities.Customer;
import core.entities.Loan;
import core.utils.utils;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import ui.components.SubController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class CustomerMatchingController extends SubController {

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

    @FXML
    public void initialize(){
        System.out.println("initialize Matching controller");


        //amount field
        SpinnerValueFactory<Integer> amountValueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0,1000000,0);
        loanAmountInput.setValueFactory(amountValueFactory);

        // interest field
        SpinnerValueFactory<Integer> mininumIntrerestSpinnerValueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0,1000000,0);
        mininumIntrerestSpinner.setValueFactory(mininumIntrerestSpinnerValueFactory);

        //yaz field
        SpinnerValueFactory<Integer> minLoanYazSpinnerValueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0,1000000,0);
        minLoanYazSpinner.setValueFactory(minLoanYazSpinnerValueFactory);

        // max open loans field
        SpinnerValueFactory<Integer> maxOpenLoansSpinnerValueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0,1000000,0);
        maxOpenLoansSpinner.setValueFactory(maxOpenLoansSpinnerValueFactory);

        // percentage of loan field
        SpinnerValueFactory<Integer> maxPercentageSpinnerValueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0,1000000,0);
        maxPercentageSpinner.setValueFactory(maxPercentageSpinnerValueFactory);


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


        String customerId = mainController.getUserSelectorCB().getValue().getId();
        int amount = loanAmountInput.getValue();
        int minYaz = minLoanYazSpinner.getValue();
        int minInterest = mininumIntrerestSpinner.getValue();


        if (amount == 0){
            return;
        }

        double balance = mainController.getEngine().findCustomerById(customerId).getBalance();
        if (amount > balance){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning - trying to overdraft!");

            alert.setContentText("You are trying to withdraw" + amount + " but the customer only has " + balance +
                    " in their account!");

            ButtonType yesButton = new ButtonType("Oh my bad!");
            alert.getButtonTypes().setAll(yesButton);
            Optional<ButtonType> result = alert.showAndWait();
            return;
        }

        List<String> temp;
        if (selectedCategories == null || selectedCategories.size() == 0){
            temp = mainController.getEngine().getCategories();
        }else{
            temp = new ArrayList<>(selectedCategories);
        }


        List<Loan> availableLoans = mainController.getEngine().findPossibleLoanMatches(customerId, temp, (double) amount, (double) minInterest, minYaz);

        loadLendingLoansTable(availableLoans);



    }

    public void setCategories(){

        for (String cat : mainController.getEngine().getCategories()){
            this.categorySpinner.getItems().add(cat);
        }

    }
    private void loadLendingLoansTable(List<Loan> availableLoans){

        ObservableList<Loan> loans = FXCollections.observableArrayList();


        loans.addAll(availableLoans);

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


        availableLoansTable.setItems(loans);
        availableLoansTable.getColumns().addAll(idColumn,ownerColumn,categoryColumn,startDateColumn,amountColumn,
                statusColumn,lengthColumn,interestColumn);
    }


    @FXML
    void financeLoansButtonPressed(ActionEvent event) {

        if(selectedLoans == null || selectedLoans.size() == 0){
            return;
        }
        String customerId = mainController.getUserSelectorCB().getValue().getId();
        double amount = (double) loanAmountInput.getValue();
        double amountForEach = utils.round(amount / selectedLoans.size());
        for (int i = 0; i < selectedLoans.size(); i++) {

            Loan loan = selectedLoans.get(i);
            double amountGiven = Math.min(loan.getRemainingAmount(),amountForEach);
            mainController.getEngine().matchLoan(loan.getId(), amountGiven, customerId);
            System.out.println("Matched " + amountGiven + "$ To loan '" + loan.getId() +"'");

        }


    }
}
