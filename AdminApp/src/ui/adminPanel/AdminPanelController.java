package ui.adminPanel;

import core.entities.Customer;
import core.entities.Loan;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import resources.paths.Paths;
import ui.components.SubController;
import ui.components.loanDetails.LoanDetailsComp;
import ui.subcontroller.AdminSubController;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;

public class AdminPanelController extends AdminSubController {


    @FXML
    private Button increaseYazButton;

    @FXML
    private Button loadFileButton;
    @FXML
    private TableView<Loan> loansTableView;

    @FXML
    private TableView<Customer> customersTableView;

    @FXML
    private Button showLoanDetailsButton;

    private Loan selectedLoan;




    @FXML
    void showLoanDetailsButtonPressed(ActionEvent event) {
        ObservableList<Loan> selectedItems = this.loansTableView.getSelectionModel().getSelectedItems();


        // if no loan was selected from the table
        if(selectedItems.size() == 0){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning - No loan selected!");

            alert.setContentText("You pressed the loan details button but no loan was selected.\n" +
                    "Please try selecting a loan from the list and only then pressing the button.(:");

            ButtonType yesButton = new ButtonType("Ok");
            alert.getButtonTypes().setAll(yesButton);
            Optional<ButtonType> result = alert.showAndWait();
            return;
        }else {

            this.selectedLoan = selectedItems.get(0);

            createShowLoanComponent(selectedLoan);


        }
    }


    @FXML
    void increaseYazButtonPressed(ActionEvent event) {

            this.mainController.IncreaseYaz(event);
            this.loadDataIntoTables();
    }

    @FXML
    void loadFileButtonPressed(ActionEvent event) {

        this.mainController.loadXMLButtonPressed(event);
    }

    public void unlockPanelButtons(){


         increaseYazButton.setDisable(false);
        loadDataIntoTables();
    }

    @SuppressWarnings("unchecked")
    private void loadDataIntoTables(){

        // clear tableview of previous data
        loansTableView.getColumns().clear();
        loansTableView.getItems().clear();

        //load loans into table
        loadLoansItoTable();
        //set disable
        loansTableView.setDisable(false);

        // clear tableview of previous data
        customersTableView.getColumns().clear();
        customersTableView.getItems().clear();

        //load customer data into table
        loadCustomersIntoTable();
        //set disable
        customersTableView.setDisable(false);


    }

    private void loadCustomersIntoTable(){
        ObservableList<Customer> customers = FXCollections.observableArrayList();

        int size = mainController.getEngine().getCustomers().size();
        List<Customer> temp = mainController.getEngine().getCustomers();

        for (int i = 0; i <size; i++) {
            customers.add(temp.get(i));
        }

        //id
        TableColumn<Customer,String> idColumn = new TableColumn<>("Customer ID");
        idColumn.setMinWidth(100);
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        //balance
        TableColumn<Customer,Double> balanceColumn = new TableColumn<>("Balance");
        balanceColumn.setMinWidth(100);
        balanceColumn.setCellValueFactory(new PropertyValueFactory<>("balance"));

        //owner
        TableColumn<Customer,Integer> givenColumn = new TableColumn<>("Amount given loans");
        givenColumn.setMinWidth(100);
        givenColumn.setCellValueFactory(c-> c.getValue().getAmountOfLoansGiven().asObject());


        //amount of new loans asking
        TableColumn<Customer,Integer> aaa = new TableColumn<>("New loaner loans");
        aaa.setMinWidth(150);
        aaa.setCellValueFactory(c-> new SimpleIntegerProperty(c.getValue().getAmountOfStatusLoan(Loan.LoanStatus.NEW,false)).asObject());


        //amount of pending loans asking
        TableColumn<Customer,Integer> bbb = new TableColumn<>("Pending loaner loans");
        bbb.setMinWidth(150);
        bbb.setCellValueFactory(c-> new SimpleIntegerProperty(c.getValue().getAmountOfStatusLoan(Loan.LoanStatus.PENDING,false)).asObject());

        //amount of active loans asking
        TableColumn<Customer,Integer> ccc = new TableColumn<>("Active loaner loans");
        ccc.setMinWidth(150);
        ccc.setCellValueFactory(c-> new SimpleIntegerProperty(c.getValue().getAmountOfStatusLoan(Loan.LoanStatus.ACTIVE,false)).asObject());

        //amount of risk loans asking
        TableColumn<Customer,Integer> ddd = new TableColumn<>("Risk loaner loans");
        ddd.setMinWidth(150);
        ddd.setCellValueFactory(c-> new SimpleIntegerProperty(c.getValue().getAmountOfStatusLoan(Loan.LoanStatus.RISK,false)).asObject());

        //amount of finished loans asking
        TableColumn<Customer,Integer> eee = new TableColumn<>("Finish loaner loans");
        eee.setMinWidth(150);
        eee.setCellValueFactory(c-> new SimpleIntegerProperty(c.getValue().getAmountOfStatusLoan(Loan.LoanStatus.FINISHED,false)).asObject());


        //amount of new loans giving
        TableColumn<Customer,Integer> AAA = new TableColumn<>("New lender loans");
        AAA.setMinWidth(150);
        AAA.setCellValueFactory(c-> new SimpleIntegerProperty(c.getValue().getAmountOfStatusLoan(Loan.LoanStatus.NEW,true)).asObject());


        //amount of pending loans giving
        TableColumn<Customer,Integer> BBB = new TableColumn<>("Pending lender loans");
        BBB.setMinWidth(150);
        BBB.setCellValueFactory(c-> new SimpleIntegerProperty(c.getValue().getAmountOfStatusLoan(Loan.LoanStatus.PENDING,true)).asObject());

        //amount of active loans giving
        TableColumn<Customer,Integer> CCC = new TableColumn<>("Active lender loans");
        CCC.setMinWidth(150);
        CCC.setCellValueFactory(c-> new SimpleIntegerProperty(c.getValue().getAmountOfStatusLoan(Loan.LoanStatus.ACTIVE,true)).asObject());

        //amount of risk loans giving
        TableColumn<Customer,Integer> DDD = new TableColumn<>("Risk lender loans");
        DDD.setMinWidth(150);
        DDD.setCellValueFactory(c-> new SimpleIntegerProperty(c.getValue().getAmountOfStatusLoan(Loan.LoanStatus.RISK,true)).asObject());

        //amount of finished loans giving
        TableColumn<Customer,Integer> EEE = new TableColumn<>("Finish lender loans");
        EEE.setMinWidth(150);
        EEE.setCellValueFactory(c-> new SimpleIntegerProperty(c.getValue().getAmountOfStatusLoan(Loan.LoanStatus.FINISHED,true)).asObject());

        customersTableView.setItems(customers);
        customersTableView.getColumns().addAll(idColumn,balanceColumn,givenColumn,aaa,bbb,ccc,ddd,eee,AAA,BBB,CCC,DDD,EEE);


    }

    private void loadLoansItoTable(){
        ObservableList<Loan> loans = FXCollections.observableArrayList();

        int size = mainController.getEngine().getLoans().size();
        List<Loan> temp = mainController.getEngine().getLoans();
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



//        TableColumn<Loan,String> investorsCol =  new TableColumn<>("Investors");
//        investorsCol.setMinWidth(100);
//        investorsCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getInvestorString()));
//
//        TableColumn<Loan,Double> amountRaised = new TableColumn<>("money needed to activate");
//        amountRaised.setMinWidth(100);
//        amountRaised.setCellValueFactory(new PropertyValueFactory<>("remainingAmount"));

        loansTableView.setItems(loans);
        loansTableView.getColumns().addAll(idColumn,
                ownerColumn,amountColumn,
                lengthColumn , interestColumn,
                timeBetweenCol, categoryColumn,
                statusColumn );
    }

    private void createShowLoanComponent(Loan l){

        ScrollPane scrollPane = new ScrollPane();
        LoanDetailsComp loanDetailsComp = new LoanDetailsComp();
        FXMLLoader loader = new FXMLLoader();
        URL url = getClass().getResource(Paths.loanInfo);
        loader.setLocation(url);
        try {
            assert url != null;
            scrollPane = loader.load(url.openStream());
            loanDetailsComp = loader.getController();
            loanDetailsComp.setMainController(mainController);
        } catch (IOException e) {
            e.printStackTrace();
        }

        loanDetailsComp.setInfoFromLoan(l);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Loan Info");
        alert.setHeaderText("Info about loan " + selectedLoan.getId());


        alert.getDialogPane().setContent(scrollPane);
        alert.setResizable(true);

        ButtonType yesButton = new ButtonType("Ok");
        alert.getButtonTypes().setAll(yesButton);
        Optional<ButtonType> result = alert.showAndWait();
        return;
    }
}
