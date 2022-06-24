package ui.adminPanel;

import core.dtos.AdminLoanDTO;
import core.dtos.AdminSnapshot;
import core.dtos.CustomerSnapshot;
import core.entities.Customer;
import core.entities.Loan;

import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;

import org.jetbrains.annotations.NotNull;

import ui.loanDetail.LoanDetailAlert;
import ui.subcontroller.AdminSubController;
import utils.http.AdminHttpClient;
import utils.http.AdminSnapshotRefresher;
import utils.resources.AdminPaths;

import java.io.Closeable;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class AdminPanelController extends AdminSubController implements Closeable {


    @FXML
    private Button increaseYazButton;

    @FXML
    public Button decreaseYazButton;

    @FXML
    private Button loadFileButton;
    @FXML
    private TableView<AdminLoanDTO> loansTableView;

    @FXML
    private TableView<Customer> customersTableView;

    @FXML
    private Button showLoanDetailsButton;

    private AdminLoanDTO selectedLoan;


    private Timer timer;
    private TimerTask snapshotRefresher;

    public ObservableList<AdminLoanDTO> customerLoansObservableList;
    public ObservableList<Customer> customerObservableList;



    @FXML
    public void initialize(){

        customerLoansObservableList = loansTableView.getItems();
        customerObservableList = customersTableView.getItems();

    }


    @FXML
    void showLoanDetailsButtonPressed(ActionEvent event) {
        ObservableList<AdminLoanDTO> selectedItems = this.loansTableView.getSelectionModel().getSelectedItems();


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



    private void updateSnapshot(AdminSnapshot adminSnapshot){


        if (adminSnapshot != null){
            Platform.runLater(() -> {
                mainController.currentYazProperty.set(String.valueOf(adminSnapshot.currentYaz));


                List<Customer> customers = new ArrayList<>();
                List<CustomerSnapshot> cSnapList = adminSnapshot.customerSnapshotList;
                for (int i = 0; i < adminSnapshot.customerSnapshotList.size(); i++) {
                    CustomerSnapshot snapshot = cSnapList.get(i);
                    customers.add(new Customer(snapshot.loansDTO.userName, (int) snapshot.loansDTO.balance,new ArrayList<>(),new ArrayList<>(),new ArrayList<>()));

                }

                customerObservableList.clear();
                customerObservableList.addAll(customers);

                if (adminSnapshot.loanList.size() != customerLoansObservableList.size() || loansChanged(adminSnapshot.loanList,customerLoansObservableList)){
                    customerLoansObservableList.clear();
                    customerLoansObservableList.addAll(adminSnapshot.loanList);
                }

                mainController.isSystemRewindProperty.set(adminSnapshot.isRewind);
            });
        }
    }

    private boolean loansChanged(List<AdminLoanDTO> snapshotList,List<AdminLoanDTO> localList){

        Map<String,String> status = new HashMap<>();
        for (int i = 0; i < snapshotList.size(); i++) {
            status.put(snapshotList.get(i).getId(),snapshotList.get(i).getStatus());
        }

        for (int i=0;i<localList.size();i++){
            if(!status.get(localList.get(i).getId()).equals(localList.get(i).getStatus())){
                return true;
            }
        }
        return false;

    }

    public void startSnapshotRefresher(){
        snapshotRefresher = new AdminSnapshotRefresher(this::updateSnapshot, mainController.getAdminName()); // new snapshotrefersher
        timer = new Timer();
        timer.schedule(snapshotRefresher,500,500);
    }


    public void loadCustomersIntoTable(){


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


        customersTableView.getColumns().addAll(idColumn,balanceColumn,givenColumn,aaa,bbb,ccc,ddd,eee,AAA,BBB,CCC,DDD,EEE);


    }

    public void loadLoansItoTable(){

        //id
        TableColumn<AdminLoanDTO,String> idColumn = new TableColumn<>("Loan ID");
        idColumn.setMinWidth(100);
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));



        //owner
        TableColumn<AdminLoanDTO,String> ownerColumn = new TableColumn<>("Owner");
        ownerColumn.setMinWidth(100);
        ownerColumn.setCellValueFactory(c-> new SimpleStringProperty(c.getValue().getOwnerName()));

        //category
        TableColumn<AdminLoanDTO,String> categoryColumn = new TableColumn<>("Category");
        categoryColumn.setMinWidth(100);
        categoryColumn.setCellValueFactory(c-> new SimpleStringProperty(c.getValue().getCategory()));

        //start date
        TableColumn<AdminLoanDTO,Integer> startDateColumn = new TableColumn<>("Start Date");
        startDateColumn.setMinWidth(100);
        startDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));


        //amount
        TableColumn<AdminLoanDTO,Double> amountColumn = new TableColumn<>("Amount");
        amountColumn.setMinWidth(100);
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));


        //status
        TableColumn<AdminLoanDTO,String> statusColumn = new TableColumn<>("Status");
        statusColumn.setMinWidth(100);
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        //length of time
        TableColumn<AdminLoanDTO,Integer> lengthColumn = new TableColumn<>("Time Length");
        lengthColumn.setMinWidth(100);
        lengthColumn.setCellValueFactory(new PropertyValueFactory<>("lengthOfTime"));

        //Intreset Rate
        TableColumn<AdminLoanDTO,Integer> interestColumn = new TableColumn<>("Interest Rate");
        interestColumn.setMinWidth(100);
        interestColumn.setCellValueFactory(new PropertyValueFactory<>("interestRate"));


        //Time between each payment
        TableColumn<AdminLoanDTO,Integer> timeBetweenCol = new TableColumn<>("pays every");
        timeBetweenCol.setMinWidth(100);
        timeBetweenCol.setCellValueFactory(new PropertyValueFactory<>("timeBetweenPayments"));



        loansTableView.getColumns().addAll(idColumn,
                ownerColumn,amountColumn,
                lengthColumn , interestColumn,
                timeBetweenCol, categoryColumn,
                statusColumn );
    }

    private void createShowLoanComponent(AdminLoanDTO l){

        ScrollPane scrollPane = new ScrollPane();
        LoanDetailAlert loanDetailsComp = new LoanDetailAlert();
        FXMLLoader loader = new FXMLLoader();
        URL url = getClass().getResource(AdminPaths.loanInfo);
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

    @FXML
    void decreaseYazButtonPressed(ActionEvent event) {


        //noinspection ConstantConditions
        String finalUrl = HttpUrl
                .parse(AdminPaths.ADMIN_REWIND_DECREASE)
                .newBuilder()
                .addQueryParameter("userName", mainController.getAdminName())
                .build()
                .toString();


        AdminHttpClient.runAsync(finalUrl,"GET",null ,new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                } else {
                    Platform.runLater(() -> {
                        try{
                        }
                        catch(Exception ignore) {}
                    });
                }
                Objects.requireNonNull(response.body()).close();
                response.close();
            }
        });
    }
    @FXML
    void increaseYazButtonPressed(ActionEvent event) {

        if(!mainController.isSystemRewindProperty.get()){

            String finalUrl = HttpUrl
                    .parse(AdminPaths.ADVANCE_TIME)
                    .newBuilder()
                    .addQueryParameter("userName", mainController.getAdminName())
                    .build()
                    .toString();
            AdminHttpClient.runAsync(finalUrl,"GET",null ,new Callback() {

                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Platform.runLater(() ->
//                        errorMsgProperty.set("Something went wrong ):")
                                    System.out.println("something went wrong")
                    );
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                    System.out.println("whattt");

                }

            });
        }else{

            //noinspection ConstantConditions
            String finalUrl = HttpUrl
                    .parse(AdminPaths.ADMIN_REWIND_INCREASE)
                    .newBuilder()
                    .addQueryParameter("userName", mainController.getAdminName())
                    .build()
                    .toString();


            AdminHttpClient.runAsync(finalUrl,"GET",null ,new Callback() {

                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    if (response.code() != 200) {
                    } else {
                        Platform.runLater(() -> {
                            try{
                            }
                            catch(Exception ignore) {}
                        });
                    }
                    Objects.requireNonNull(response.body()).close();
                    response.close();
                }
            });
        }
    }


    @Override
    public void close() throws IOException {
        if ( timer != null && snapshotRefresher != null) {
            timer.cancel();
            snapshotRefresher.cancel();
        }
    }
}
