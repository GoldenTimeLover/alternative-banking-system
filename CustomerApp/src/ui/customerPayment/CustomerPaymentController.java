package ui.customerPayment;

import core.Exceptions.LoanProccessingException;
import core.Exceptions.NotEnoughMoneyException;
import core.entities.Loan;
import core.entities.Notification;
import core.entities.Transaction;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import ui.notificationArea.NotificationAreaController;
import ui.notificationArea.singleNotificationController;
import ui.subcontrollers.CustomerSubController;
import utils.CustomerPaths;
import utils.http.CustomerHttpClient;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class CustomerPaymentController extends CustomerSubController {


    @FXML private VBox notificationArea;
    @FXML private NotificationAreaController notificationAreaController;


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

    public ObservableList<Loan> paytingLoansObservableList;

    public ObservableList<Notification> notificationObservableList;

    private Loan selectedLoan;

    @FXML
    private VBox notificationBox;


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

        paytingLoansObservableList = unpaidLoansTable.getItems();
        notificationObservableList = new ObservableList<Notification>() {
            @Override
            public void addListener(ListChangeListener<? super Notification> listener) {

            }

            @Override
            public void removeListener(ListChangeListener<? super Notification> listener) {

            }

            @Override
            public boolean addAll(Notification... elements) {
                return false;
            }

            @Override
            public boolean setAll(Notification... elements) {
                return false;
            }

            @Override
            public boolean setAll(Collection<? extends Notification> col) {
                return false;
            }

            @Override
            public boolean removeAll(Notification... elements) {
                return false;
            }

            @Override
            public boolean retainAll(Notification... elements) {
                return false;
            }

            @Override
            public void remove(int from, int to) {

            }

            @Override
            public int size() {
                return 0;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public boolean contains(Object o) {
                return false;
            }

            @NotNull
            @Override
            public Iterator<Notification> iterator() {
                return null;
            }

            @NotNull
            @Override
            public Object[] toArray() {
                return new Object[0];
            }

            @NotNull
            @Override
            public <T> T[] toArray(@NotNull T[] a) {
                return null;
            }

            @Override
            public boolean add(Notification notification) {
                return false;
            }

            @Override
            public boolean remove(Object o) {
                return false;
            }

            @Override
            public boolean containsAll(@NotNull Collection<?> c) {
                return false;
            }

            @Override
            public boolean addAll(@NotNull Collection<? extends Notification> c) {
                return false;
            }

            @Override
            public boolean addAll(int index, @NotNull Collection<? extends Notification> c) {
                return false;
            }

            @Override
            public boolean removeAll(@NotNull Collection<?> c) {
                return false;
            }

            @Override
            public boolean retainAll(@NotNull Collection<?> c) {
                return false;
            }

            @Override
            public void clear() {

            }

            @Override
            public Notification get(int index) {
                return null;
            }

            @Override
            public Notification set(int index, Notification element) {
                return null;
            }

            @Override
            public void add(int index, Notification element) {

            }

            @Override
            public Notification remove(int index) {
                return null;
            }

            @Override
            public int indexOf(Object o) {
                return 0;
            }

            @Override
            public int lastIndexOf(Object o) {
                return 0;
            }

            @NotNull
            @Override
            public ListIterator<Notification> listIterator() {
                return null;
            }

            @NotNull
            @Override
            public ListIterator<Notification> listIterator(int index) {
                return null;
            }

            @NotNull
            @Override
            public List<Notification> subList(int fromIndex, int toIndex) {
                return null;
            }

            @Override
            public void addListener(InvalidationListener listener) {

            }

            @Override
            public void removeListener(InvalidationListener listener) {

            }
        };
    }



    public void prepareNotificationArea(){

        if(mainController.notificationDTO != null && mainController.notificationDTO.notificationList != null){
            notificationBox.getChildren().clear();
            this.notificationObservableList.clear();
            this.notificationObservableList.addAll(mainController.notificationDTO.notificationList);
            for (Notification n: mainController.notificationDTO.notificationList) {

                    createNotification(n.getTitle(),n.getDate(),n.getContent());
            }

        }



    }

    public void createNotification(String title,int date,String content) {


        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource(CustomerPaths.SINGLE_NOTIFICATION));
            Node singleWordTile = loader.load();


            singleNotificationController singleNotController = loader.getController();
            singleNotController.titleText.setText(title);
            singleNotController.dateText.setText("Date: " + date);
            singleNotController.contentText.setText(content);
            notificationBox.getChildren().add(singleWordTile);

        }catch (IOException e){
            e.printStackTrace();
        }
    }
    public void unlockPaymentButtons(){
        this.payCurrButton.setDisable(false);
        this.payEntireLoanButton.setDisable(false);
        this.payRiskDebt.setDisable(false);
    }

    public void prepareTable(){


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


        unpaidLoansTable.getColumns().addAll(idColumn,timeBetweenCol,startDateColumn,amountColumn,
                statusColumn,lengthColumn,interestColumn,aaaa);


    }

    @FXML
    void payCurrButtonPressed(ActionEvent event) {
        sendPaymentRequest("payCurr",0);
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
            if(riskDebtSpinner.getValue() > mainController.customerSnapshot.loansDTO.balance){
                mainController.showAlert(Alert.AlertType.WARNING,"Warning - Not enough money!","You are trying to pay the debt of '" + selectedLoan.getId()+" '" +
                        " but the amount entered is higher than your balance");
            }else{
                    sendPaymentRequest("payRisk",riskDebtSpinner.getValue());
            }

        }

    }
    @FXML
    void payEntireLoanButtonPressed(ActionEvent event) {
            sendPaymentRequest("payEntire",0);

    }


    private void sendPaymentRequest(String type,int amount){

        ObservableList<Loan> selectedItems = this.unpaidLoansTable.getSelectionModel().getSelectedItems();

        if(selectedItems.size() == 0){
            mainController.showAlert(Alert.AlertType.WARNING,"Warning - No loan selected!","You are trying to pay a loan but no loan is selected.\nplease try" +
                    "selecting one of the loans in the table that need paying.");
        }
        else{
            this.selectedLoan = selectedItems.get(0);

            String finalUrl = HttpUrl
                    .parse(CustomerPaths.PAY_LOAN)
                    .newBuilder()
                    .addQueryParameter("type",type)
                    .addQueryParameter("user",this.mainController.getUsername())
                    .addQueryParameter("loanId", selectedLoan.getId())
                    .addQueryParameter("amount", String.valueOf(amount))
                    .build()
                    .toString();
            CustomerHttpClient.runAsync(finalUrl, "GET", null, new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {

                }
                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String s = response.body().string();

                    Platform.runLater(()->{

                        mainController.showAlert(Alert.AlertType.INFORMATION,"",s);
                    });

                }
            });
        }
    }
}
