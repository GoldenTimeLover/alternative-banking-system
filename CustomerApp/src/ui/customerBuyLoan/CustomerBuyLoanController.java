package ui.customerBuyLoan;

import core.dtos.AdminLoanDTO;
import core.entities.Loan;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import ui.subcontrollers.CustomerSubController;
import utils.CustomerPaths;
import utils.http.CustomerHttpClient;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class CustomerBuyLoanController extends CustomerSubController implements Initializable {

    @FXML
    private Button buyLoanButton;

    @FXML
    private TableView<AdminLoanDTO> forSaleLoansTable;


    public ObservableList<AdminLoanDTO> forSaleLoanObservable;
    private AdminLoanDTO selectedLoan;




    @FXML
    void buyLoanButtonPressed(ActionEvent event) {
        ObservableList<AdminLoanDTO> selectedItems = this.forSaleLoansTable.getSelectionModel().getSelectedItems();
        if(selectedItems.size() == 0){
            mainController.showAlert(Alert.AlertType.WARNING,"Warning - No loan selected!","No loan is selected.\nplease try" +
                    "selecting one of the loans in the table.");
        }
        else{
            this.selectedLoan = selectedItems.get(0);
            String finalUrl = HttpUrl
                    .parse(CustomerPaths.BUY_LOAN)
                    .newBuilder()
                    .addQueryParameter("user",this.mainController.getUsername())
                    .addQueryParameter("loanId", selectedLoan.getId())
                    .addQueryParameter("seller",selectedLoan.getWhoSelling())
                    .build()
                    .toString();
            CustomerHttpClient.runAsync(finalUrl, "GET", null, new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {

                }
                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {


                    if(response.code() != 200){
                        String s =  response.body().string();
                        Platform.runLater(() ->{
                                mainController.showAlert(Alert.AlertType.ERROR,s,s);
                                }
                        );
                    }else{
                        Platform.runLater(() ->{
                                    mainController.showAlert(Alert.AlertType.INFORMATION,"Loan purchase","" +
                                            "Successfully bought loan "+ selectedLoan.getId() + " from " + selectedLoan.getWhoSelling());
                                }
                        );

                    }
                    System.out.println("Returned from buy loan request");
                }
            });
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        forSaleLoanObservable = forSaleLoansTable.getItems();
    }


    public void prepareTable(){


        //id
        TableColumn<AdminLoanDTO,String> idColumn = new TableColumn<>("Loan ID");
        idColumn.setMinWidth(150);
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));



        //owner
        TableColumn<AdminLoanDTO,String> ownerColumn = new TableColumn<>("Owner");
        ownerColumn.setMinWidth(150);
        ownerColumn.setCellValueFactory(c-> new SimpleStringProperty(c.getValue().getOwnerName()));

        //category
        TableColumn<AdminLoanDTO,String> categoryColumn = new TableColumn<>("Category");
        categoryColumn.setMinWidth(150);
        categoryColumn.setCellValueFactory(c-> new SimpleStringProperty(c.getValue().getCategory()));

        //start date
        TableColumn<AdminLoanDTO,Integer> startDateColumn = new TableColumn<>("Start Date");
        startDateColumn.setMinWidth(150);
        startDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));


        //amount
        TableColumn<AdminLoanDTO,Double> amountColumn = new TableColumn<>("Amount");
        amountColumn.setMinWidth(150);
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));


        //length of time
        TableColumn<AdminLoanDTO,Integer> lengthColumn = new TableColumn<>("Time Length");
        lengthColumn.setMinWidth(150);
        lengthColumn.setCellValueFactory(new PropertyValueFactory<>("lengthOfTime"));

        //Intreset Rate
        TableColumn<AdminLoanDTO,Integer> interestColumn = new TableColumn<>("Interest Rate");
        interestColumn.setMinWidth(150);
        interestColumn.setCellValueFactory(new PropertyValueFactory<>("interestRate"));


        //Time between each payment
        TableColumn<AdminLoanDTO,Integer> timeBetweenCol = new TableColumn<>("pays every");
        timeBetweenCol.setMinWidth(150);
        timeBetweenCol.setCellValueFactory(new PropertyValueFactory<>("timeBetweenPayments"));

        TableColumn<AdminLoanDTO,Double> aaaa = new TableColumn<>("Total to be paid");
        aaaa.setMinWidth(150);
        aaaa.setCellValueFactory(new PropertyValueFactory<>("completeAmountToBePaid"));


        TableColumn<AdminLoanDTO, Boolean> ccc = new TableColumn<>("seller name");
        ccc.setMinWidth(150);
        ccc.setCellValueFactory(new PropertyValueFactory<>("whoSelling"));


        forSaleLoansTable.getColumns().addAll(idColumn,ccc,amountColumn,timeBetweenCol,startDateColumn,
                lengthColumn,interestColumn,aaaa);


    }
}
