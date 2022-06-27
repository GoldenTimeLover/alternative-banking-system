package ui.customerAddLoanPanel;



import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import core.dtos.CustomerSnapshot;
import core.dtos.LoansDTO;
import core.dtos.SingleLoanDTO;
import core.entities.Loan;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringExpression;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import ui.subcontrollers.CustomerSubController;
import utils.CustomerPaths;
import utils.http.CustomerHttpClient;
import utils.xml.ClientXmlParser;
import utils.xml.generated.AbsLoan;
import utils.xml.generated.AbsLoans;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class CustomerAddLoanPanelController extends CustomerSubController {

    @FXML
    private TextField LoanIdTextField;

    @FXML
    private Button loadLoanFromFileButton;

    @FXML
    private Spinner<Integer> loanAmountInput;


    @FXML
    private Spinner<Integer> mininumIntrerestSpinner;

    @FXML
    private Spinner<Integer> minLoanYazSpinner;

    @FXML
    private Spinner<Integer> PaysEverySpinner;

    @FXML
    private Button SendLoanRequestButton;

    @FXML
    private ListView<String> categorySpinner;

    private ObservableList<String> selectedCategories;


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


        // percentage of loan field
        SpinnerValueFactory<Integer> mininumIntrerestSpinnerValueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0,100,0);
        mininumIntrerestSpinner.setValueFactory(mininumIntrerestSpinnerValueFactory);

        mininumIntrerestSpinner.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                mininumIntrerestSpinner.increment(0); // won't change value, but will commit editor
            }
        });


        // percentage of loan field
        SpinnerValueFactory<Integer> PaysEverySpinnerValueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0,100,0);
        PaysEverySpinner.setValueFactory(PaysEverySpinnerValueFactory);

        PaysEverySpinner.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                PaysEverySpinner.increment(0); // won't change value, but will commit editor
            }
        });


        // percentage of loan field
        SpinnerValueFactory<Integer> minLoanYazSpinnerValueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0,100,0);
        minLoanYazSpinner.setValueFactory(minLoanYazSpinnerValueFactory);

        minLoanYazSpinner.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                minLoanYazSpinner.increment(0); // won't change value, but will commit editor
            }
        });


        categorySpinner.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        categorySpinner.setOnMouseClicked(new EventHandler<Event>() {

            @Override
            public void handle(Event event) {
                selectedCategories =  categorySpinner.getSelectionModel().getSelectedItems();
            }

        });


    }

    @FXML
    void SendLoanRequestButtonPressed(ActionEvent event)
    {

        if(selectedCategories.size() == 0){

            Platform.runLater(()->{
                    mainController.showAlert(Alert.AlertType.INFORMATION,"","No category chosen");
            });
            return;
        }
        String customerId = mainController.getUsername();
        String loanId = LoanIdTextField.getText();
        String categories = selectedCategories.get(0);
        int amount = loanAmountInput.getValue();
        int minYaz = minLoanYazSpinner.getValue();
        int minInterest = mininumIntrerestSpinner.getValue();
        int paysEvery = PaysEverySpinner.getValue();

        if (amount == 0 || minYaz == 0 || minInterest == 0 || paysEvery == 0 || Objects.equals(loanId, "") || categories.equals("")){
            Platform.runLater(()->{
                mainController.showAlert(Alert.AlertType.INFORMATION,"","Please fill out all the fields");
            });

            return;
        }




        try{
            LoansDTO loansDTO = new LoansDTO(new ArrayList<>(),new ArrayList<>(), mainController.getUsername(), 0);
            loansDTO.loanList.add(new SingleLoanDTO(categories,amount,minYaz,paysEvery,minInterest,loanId, Loan.LoanStatus.NEW));

            Gson gson = new Gson();
            String s = gson.toJson(loansDTO,LoansDTO.class);


            String finalUrl = HttpUrl
                    .parse(CustomerPaths.ADD_LOAN_PATH)
                    .newBuilder()
                    .addQueryParameter("loanData",s)
                    .addQueryParameter("user",this.mainController.getUsername())
                    .build()
                    .toString();


            CustomerHttpClient.runAsync(finalUrl, "GET", null, new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {

                }
                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                    if (response.code() == 200) {
                        Platform.runLater(() -> {
                                    try {
                                        String s = response.body().string();
                                        mainController.showAlert(Alert.AlertType.INFORMATION,"",s);

                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                        );
                    }else{
                        Platform.runLater(() -> {
                                    try {
                                        String s = response.body().string();
                                        mainController.showAlert(Alert.AlertType.INFORMATION,"",s);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                        );
                    }

                }
            });

        }
        catch(Exception e)
        {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error loading File!");
            ButtonType yesButton = new ButtonType("Cool");
            alert.getButtonTypes().setAll(yesButton);
            Optional<ButtonType> result = alert.showAndWait();

        }


    }

    @FXML
    void loadLoanFromFileButtonPressed(ActionEvent event) {


            FileChooser fileChooser = new FileChooser();
            File selectedFile;
            fileChooser.setTitle("Select a file");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("xml files", "*.xml"));
            selectedFile = fileChooser.showOpenDialog(this.mainController.getPrimaryStage());


            if(selectedFile == null)
                return;

            try{
                ClientXmlParser clientXmlParser = new ClientXmlParser(selectedFile.getAbsolutePath());
                List<AbsLoan> loans = clientXmlParser.getLoans();
                List<String> categories = clientXmlParser.getCategories();
                LoansDTO lsdto = new LoansDTO(new ArrayList(),new ArrayList<>(), mainController.getUsername(), 0,categories);

                for (AbsLoan loan : loans) {
                    lsdto.loanList.add(new SingleLoanDTO(loan.getAbsCategory(),
                            loan.getAbsCapital(), loan.getAbsTotalYazTime(),
                            loan.getAbsPaysEveryYaz(), loan.getAbsIntristPerPayment(),
                            loan.getId(), Loan.LoanStatus.NEW));

                }

                Gson gson = new Gson();
                String s = gson.toJson(lsdto,LoansDTO.class);


                String finalUrl = HttpUrl
                        .parse(CustomerPaths.ADD_LOAN_PATH)
                        .newBuilder()
                        .addQueryParameter("loanData",s)
                        .addQueryParameter("user",this.mainController.getUsername())
                        .build()
                        .toString();

                CustomerHttpClient.runAsync(finalUrl, "GET", null, new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {

                    }
                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                        Platform.runLater(()->{


                                String s = "";
                                try {
                                    s = response.body().string();
                                } catch (IOException e) {
                                    s = "";
                                }
                                mainController.showAlert(Alert.AlertType.INFORMATION,"",s);
                                setCategories();
                        }
                        );

                    }
                });

            }
            catch(Exception e)
            {

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Error loading File!");
                ButtonType yesButton = new ButtonType("Cool");
                alert.getButtonTypes().setAll(yesButton);
                Optional<ButtonType> result = alert.showAndWait();

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

}
