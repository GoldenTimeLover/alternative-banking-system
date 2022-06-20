package ui.customerAddLoanPanel;



import com.google.gson.Gson;
import com.google.gson.JsonObject;
import core.dtos.CustomerSnapshot;
import core.dtos.LoansDTO;
import core.dtos.SingleLoanDTO;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringExpression;
import javafx.event.ActionEvent;
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
    private TextField categoriesTextField;

    @FXML
    private Spinner<Integer> mininumIntrerestSpinner;

    @FXML
    private Spinner<Integer> minLoanYazSpinner;

    @FXML
    private Spinner<Integer> PaysEverySpinner;

    @FXML
    private Button SendLoanRequestButton;


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

    }

    @FXML
    void SendLoanRequestButtonPressed(ActionEvent event) {

        String customerId = mainController.getUsername();
        String loanId = LoanIdTextField.getText();
        String categories = categoriesTextField.getText();
        int amount = loanAmountInput.getValue();
        int minYaz = minLoanYazSpinner.getValue();
        int minInterest = mininumIntrerestSpinner.getValue();
        int paysEvery = PaysEverySpinner.getValue();

        if (amount == 0 || minYaz == 0 || minInterest == 0 || paysEvery == 0 || Objects.equals(loanId, "") || categories.equals("")){
            return;
        }




        try{
            LoansDTO loansDTO = new LoansDTO(new ArrayList<>(),new ArrayList<>(), mainController.getUsername(), 0);
            loansDTO.loanList.add(new SingleLoanDTO(categories,amount,minYaz,paysEvery,minInterest,loanId));

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

                    Platform.runLater(()->{
                                try {
                                    String s = response.body().string();
                                    CustomerSnapshot customerSnapshot = gson.fromJson(s,CustomerSnapshot.class);
                                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                    alert.setTitle("Success");
                                    alert.setHeaderText("File loaded Successfully");
                                    alert.setContentText(s);
                                    ButtonType yesButton = new ButtonType("Cool");
                                    alert.getButtonTypes().setAll(yesButton);
                                    Optional<ButtonType> result = alert.showAndWait();
                                    mainController.SpeardInfoToAll(customerSnapshot);
                                }
                                catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                    );

                }
            });

        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
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
                LoansDTO lsdto = new LoansDTO(new ArrayList(),new ArrayList<>(), mainController.getUsername(), 0);

                for (AbsLoan loan : loans) {
                    lsdto.loanList.add(new SingleLoanDTO(loan.getAbsCategory(),
                            loan.getAbsCapital(), loan.getAbsTotalYazTime(),
                            loan.getAbsPaysEveryYaz(), loan.getAbsIntristPerPayment(),
                            loan.getId()));

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
                            try {
                                String s = response.body().string();
                                CustomerSnapshot customerSnapshot = gson.fromJson(s,CustomerSnapshot.class);
                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                alert.setTitle("Success");
                                alert.setHeaderText("File loaded Successfully");
                                alert.setContentText(s);
                                ButtonType yesButton = new ButtonType("Cool");
                                alert.getButtonTypes().setAll(yesButton);
                                Optional<ButtonType> result = alert.showAndWait();
                            }
                            catch (IOException e) {
                                        e.printStackTrace();
                            }
                        }
                        );

                    }
                });

            }
            catch(Exception e)
            {
                System.out.println(e.getMessage());
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Error loading File!");
                ButtonType yesButton = new ButtonType("Cool");
                alert.getButtonTypes().setAll(yesButton);
                Optional<ButtonType> result = alert.showAndWait();

            }

    }

}
