package ui.components.adminPanel;

import core.entities.Customer;
import core.entities.Loan;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import ui.PrimaryController;
import ui.components.SubController;

import java.util.List;

public class AdminPanelController extends SubController {


    @FXML
    private Button increaseYazButton;

    @FXML
    private Button loadFileButton;
    @FXML
    private TableView<Loan> loansTableView;

    @FXML
    private TableView<Customer> customersTableView;



    @FXML
    void increaseYazButtonPressed(ActionEvent event) {
            this.mainController.IncreaseYaz(event);
    }

    @FXML
    void loadFileButtonPressed(ActionEvent event) {

        this.mainController.loadXMLButtonPressed(event);
    }

    @FXML
    void showCustomersButtonPressed(ActionEvent event) {

    }

    @FXML
    void showLoansButtonPressed(ActionEvent event) {

    }

    public void unlockPanelButtons(){


         increaseYazButton.setDisable(false);
        loadDataIntoTables();
    }

    @SuppressWarnings("unchecked")
    private void loadDataIntoTables(){


        //load loans into table
        loadLoansItoTable();
        //set disable
        loansTableView.setDisable(false);

        loadCustomersIntoTable();
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

        customersTableView.setItems(customers);
        customersTableView.getColumns().addAll(idColumn,balanceColumn,givenColumn);


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


        loansTableView.setItems(loans);
        loansTableView.getColumns().addAll(idColumn,ownerColumn,categoryColumn,startDateColumn,amountColumn,
                statusColumn,lengthColumn,interestColumn);
    }
}
