package ui.customerMatching;

import core.entities.Loan;
import core.tasks.match.GetMatchingLoansService;
import core.utils.utils;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

import ui.subcontrollers.CustomerSubController;

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
        System.out.println("initialize Matching controller");


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

        double balance = 0;
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



//        final GetMatchingLoansService service = new GetMatchingLoansService(mainController.getEngine().getLoans(),customerId, temp, (double) amount, (double) minInterest, minYaz,amountOfOpenLoans,maxPercentageOfLoan);
//
//        Region veil = new Region();
//        veil.setStyle("-fx-background-color: rgba(0,0,0,0.4)");
//        veil.setPrefSize(400,400);
//        ProgressIndicator p = new ProgressIndicator();
//        p.setMaxSize(140,140);
//        p.setStyle("-fx-progress-color: orange");
//
//
//        p.progressProperty().bind(service.progressProperty());
//        veil.visibleProperty().bind(service.runningProperty());
//        p.visibleProperty().bind(service.runningProperty());
//
//
//        this.availableLoansTable.getItems().clear();
//        this.availableLoansTable.getColumns().clear();
//        this.loadLendingLoansTable();
//
//        this.availableLoansTable.itemsProperty().bind(service.valueProperty());
//
//        myStackPane.getChildren().addAll(veil,p);
//        service.start();
//


    }

    public void setCategories(){

//        for (String cat : mainController.getEngine().getCategories()){
//            this.categorySpinner.getItems().add(cat);
//        }

    }
    private void loadLendingLoansTable(){


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

            Loan loan = selectedLoans.get(i);
            double amountGiven = Math.min(loan.getRemainingAmount(),amountForEach);
//            double finalAmountLoaned = mainController.getEngine().matchLoan(loan.getId(), amountGiven, customerId,maxPercentageSpinner.getValue());
            double finalAmountLoaned = 0;
            System.out.println();


            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Matched loan");
            alert.setHeaderText("Loan '"+loan.getId()+"' has been matched.");

            if(loan.getStatus().equals(Loan.LoanStatus.ACTIVE)){
                alert.setContentText("Matched " + finalAmountLoaned + "$ To loan '" + loan.getId() +"'.\nThe" +
                        "loan has gathered all the required funds and is now ACTIVE.");
            }else{
                alert.setContentText("Matched " + finalAmountLoaned + "$ To loan '" + loan.getId() +"'.\n" +
                        "The loan needs " + loan.getRemainingAmount() +" in order to become active.");
            }

            ButtonType yesButton = new ButtonType("Ok");
            alert.getButtonTypes().setAll(yesButton);
            Optional<ButtonType> result = alert.showAndWait();

        }

        clearFilters();

        //clear table
        this.availableLoansTable.getColumns().clear();
        this.availableLoansTable.getItems().clear();

    }
}
