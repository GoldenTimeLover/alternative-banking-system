package ui;

import core.dtos.CustomerSnapshot;
import core.dtos.LoansDTO;
import core.dtos.NotificationDTO;
import core.dtos.TransactionsDTO;
import core.engine.ABSEngine;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.ScaleTransition;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

import javafx.stage.Stage;
import javafx.util.Duration;
import ui.customerPanel.CustomerPanelController;
import utils.CustomerPaths;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;

public class PrimaryController {


    enum Theme {DARK,LIGHT,MCDONADLS}

    private Stage primaryStage;

    private Theme currentTheme;


    public Stage getPrimaryStage() {
        return primaryStage;
    }

    @FXML private BorderPane customerPanelComponent;
    @FXML private CustomerPanelController customerPanelComponentController;


    @FXML
    private BorderPane mainBorderPane;


    @FXML
    private Label currentUserNameText;
    @FXML
    private Label currentYazText;

    @FXML
    private Menu themes;

    @FXML
    private RadioMenuItem defaultTheme;

    @FXML
    private ToggleGroup templates;

    @FXML
    private RadioMenuItem darkModeTheme;

    @FXML
    private MenuItem aboutButton;

    @FXML
    private CheckBox animationCheckBox;

    @FXML
    private ImageView logoImage;


    private FadeTransition fadeTransition = new FadeTransition();
    private ScaleTransition scaleTransition = new ScaleTransition();
    private ScaleTransition scaleTransition2 = new ScaleTransition();

    public LoansDTO loansDTO;
    public TransactionsDTO transactionsDTO;
    public NotificationDTO notificationDTO;
    public CustomerSnapshot customerSnapshot;


    private final StringProperty currentYazProperty = new SimpleStringProperty();

    @FXML
    public void initialize(Stage primaryStage,String currentUser){

        this.primaryStage = primaryStage;
        this.currentUserNameText.setText(currentUser);

        currentYazProperty.set("1");
        currentYazText.textProperty().bind(Bindings.concat(currentYazProperty,""));
    }
    public String getUsername(){
        return this.currentUserNameText.getText();
    }

    @FXML
    void animationCheckBoxPressed(ActionEvent event) {

        //animation not activated
        if (!this.animationCheckBox.isSelected()) {

            this.scaleTransition.jumpTo(Duration.ZERO);
            this.scaleTransition2.jumpTo(Duration.ZERO);
            this.scaleTransition2.stop();
            this.scaleTransition.stop();
            this.fadeTransition.stop();

            logoImage.setOpacity(1);
            return;
        }

    }


    public String getCustomerId(){
        return this.currentUserNameText.getText();
    }

    @FXML
    void yazClicked(MouseEvent event){
        if (animationCheckBox.isSelected() && scaleTransition.getCurrentRate()==0.0d) {
            scaleTransition.setNode(this.currentYazText);
            scaleTransition.setDuration(Duration.millis(200));
            this.scaleTransition.setCycleCount(6);
            this.scaleTransition.setAutoReverse(true);
            this.scaleTransition.setToX(-1);
            this.scaleTransition.play();
        }

    }


    @FXML
    void logoClicked(MouseEvent event){

        if (animationCheckBox.isSelected() && fadeTransition.getCurrentRate()==0.0d) {

            this.fadeTransition.setNode(this.logoImage);


            this.fadeTransition.setDuration(Duration.millis(400));
            this.fadeTransition.setCycleCount(5);
            this.fadeTransition.setAutoReverse(true);
            this.fadeTransition.setInterpolator(Interpolator.EASE_IN);
            this.fadeTransition.setFromValue(0);
            this.fadeTransition.setToValue(1);
            fadeTransition.play();

        }
    }
    @FXML
    void aboutButtonPressed(ActionEvent event) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("About");
        alert.setHeaderText("What's the deal with ABS?");
        String content = "The economic sector has been controlled for decades by a central body called the Bank.\n" +
                "One of the main functions of the Bank is to enable the economy to move by allowing the funds to flow between a certain source (usually the Bank) and the target (individuals / businesses). The main means in this model is to provide financial loans of various types to the entity that requires them: a mortgage / starting a business / closing a minus, etc." +"\n" +"\n" +"But the bank is not a sucker.\n" +"An old saying goes: \"..whoever deals with money - makes money ...\" And so is the bank.\n" +"Sitting as the main enabling factor for the economic game - the bank is at the center of all transactions, and for each such transfer of funds it deducts a commission / interest. In the absence of competition (who said the centralized structure of the banks?) The commissions / interest rates in the bank are increasing and the end users (private individuals / businesses) can only succumb to the model and be given no choice - simply because there is no alternative." +"\n is that so ? \n"+"Suppose an individual needed a loan for certain needs. Imagine that instead of contacting the bank (the oppressor) he would turn to his friend (the favorite), who happens to have a sum of money available.\n" +"The member could lend him the money, while reaching an agreement on an arrangement and payment schedule acceptable to both, and hoping and out of a desire that they could agree on a lower commission (interest) so that both the borrower and the lender were spacious:\n" +"The borrower would get a loan at a lower interest rate; The lender would create for itself a more profitable investment channel on its available funds.\n" +"\n" +"It's probably nice and possible between 2 friends who know each other and trust each other over time.\n" +"But what if we could leverage this model between a collection of private individuals who have available funds and want to lend it to others, and a (different?) Collection of private individuals who need to borrow funds? But in this case, if there is no prior acquaintance and a security factor, how do we ensure that the borrowers will meet their payment schedule and the lenders will not risk their money " +
                "in vain?" +"\nIn recent years, a number of companies / organizations have been operating in the field that offer exactly this service: a platform that enables a connection between a lender and a private lender while ensuring a low interest rate for a lender on the one hand, and an investment with a reasonable and safe return for a lender on the other. (For those interested, Blender and BTB are 2 examples of Israeli companies operating in this field).\n" +
                "\n" +
                "In this System, a similar alternative banking system was constructed.\n" +
                "The VAT will make it possible to maintain loans within it, and will define 2 types of customers: borrowers and lenders.\n" +
                "Borrowers will be able to apply for a loan. Each loan will be characterized by a number of details (see details below).\n" +
                "On the other h  and, the VAT will allow lenders to define the amount they are interested in investing and the levels of risk they are willing to pay (see details below).\n" +
                "The VAT will operate a special placement algorithm that will offer the investor to lend his money to a number of loans, so that his risk is actually spread between several channels. In this way, the VAT reduces the chance of losing the funds if the loan is not paid on time (or at all).\n" +
                "The VAT will reflect the full data on the process and its progress to both borrowers and lenders.\n" +
                "The VAT will also allow lenders to \"sell\" an existing loan, if they want / need their money earlier than the end of the loan.";
        TextArea area = new TextArea(content);
        area.setWrapText(true);
        area.setEditable(false);

        alert.getDialogPane().setContent(area);
        alert.setResizable(true);
        ButtonType yesButton = new ButtonType("Cool");
        alert.getButtonTypes().setAll(yesButton);
        Optional<ButtonType> result = alert.showAndWait();

    }



    @FXML
    void darkModeThemePressed(ActionEvent event) {



        currentTheme = Theme.DARK;
        Scene scene = primaryStage.getScene();
        scene.getStylesheets().clear();
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource(CustomerPaths.DARK_PRIMARY_THEME)).toExternalForm());
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource(CustomerPaths.DARK_BODY_THEME)).toExternalForm());



    }

    @FXML
    void mcDonaldModeThemePressed(ActionEvent event) {

        currentTheme = Theme.MCDONADLS;
        Scene scene = primaryStage.getScene();
        scene.getStylesheets().clear();
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource(CustomerPaths.MCDONALDS_PRIMARY_THEME)).toExternalForm());
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource(CustomerPaths.MCDONALDS_BODY_THEME)).toExternalForm());



    }
    @FXML
    void defaultThemePressed(ActionEvent event) {

        currentTheme = Theme.LIGHT;
        Scene scene = primaryStage.getScene();
        scene.getStylesheets().clear();
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource(CustomerPaths.LIGHT_PRIMARY_THEME)).toExternalForm());
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource(CustomerPaths.LIGHT_BODY_THEME)).toExternalForm());


    }




    public void setPrimaryStage(Stage stage){
        this.primaryStage = stage;
    }




    public void customerPanel(){

        FXMLLoader loader = new FXMLLoader();
        URL url = getClass().getResource(CustomerPaths.CUSTOMER_PANEL);
        loader.setLocation(url);

        try {

            assert url != null;
            customerPanelComponent = loader.load(url.openStream());
            customerPanelComponentController = loader.getController();
            customerPanelComponentController.setMainController(this);
            mainBorderPane.setCenter(customerPanelComponent);
            customerPanelComponentController.initcomps();
            customerPanelComponentController.startListRefresher();

        } catch (IOException e) {
            System.out.println("nope");
            e.printStackTrace();
        }
    }
    public ABSEngine getEngine(){
        return new ABSEngine();
    }

    public void showAlert(Alert.AlertType type,String title,String content){

        Alert alert = new Alert(type);
        alert.setTitle(title);

        alert.setContentText(content);

        ButtonType yesButton = new ButtonType("Ok");
        alert.getButtonTypes().setAll(yesButton);
        Optional<ButtonType> result = alert.showAndWait();
    }

    public void SpeardInfoToAll(CustomerSnapshot customerSnapshot){

        currentYazProperty.set(String.valueOf(customerSnapshot.currentYaz));
        this.loansDTO = customerSnapshot.loansDTO;
        this.transactionsDTO = customerSnapshot.transactionsDTO;
        this.customerSnapshot = customerSnapshot;
        this.notificationDTO = customerSnapshot.notificationDTO;

    }


}
