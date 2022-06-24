package ui.loanDetail;

import core.dtos.AdminLoanDTO;
import core.entities.Loan;
import core.entities.Transaction;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import ui.subcontroller.AdminSubController;

public class LoanDetailAlert extends AdminSubController {



    @FXML
    private VBox mybox;

    @FXML
    private Text loanId;

    @FXML
    private Text loanOwner;

    @FXML
    private Text lengthOfTime;

    @FXML
    private Text interest;

    @FXML
    private Text status;


    public void setInfoFromLoan(AdminLoanDTO loan){

        loanId.setText(loan.getId());
        loanOwner.setText(loan.getOwnerName());
        lengthOfTime.setText(String.valueOf(loan.getLengthOfTime()));
        interest.setText(String.valueOf(loan.getInterestRate()));
        status.setText(String.valueOf(loan.getStatus()));

        if(loan.getStatus().equals("ACTIVE") ||
                loan.getStatus().equals("RISK")||
                loan.getStatus().equals("FINISHED")){

            Text startDateLabel = new Text("Start Date:");
            startDateLabel.setStyle("-fx-font-weight: bold");
            Text startDate = new Text(String.valueOf(loan.getStartDate()));
            mybox.getChildren().add(startDateLabel);
            mybox.getChildren().add(startDate);
        }
        if(loan.getStatus().equals("FINISHED")){

            Text endDateLabel = new Text("End Date:");
            endDateLabel.setStyle("-fx-font-weight: bold");
            Text endDate = new Text(String.valueOf(loan.getEndDate()));
            mybox.getChildren().add(endDateLabel);
            mybox.getChildren().add(endDate);
        }

        if (loan.getStatus().equals("PENDING")){
            Text investorsLabel = new Text("Investors:");
            investorsLabel.setStyle("-fx-font-weight: bold");
            Text investors = new Text(loan.getLenders().toString());
            mybox.getChildren().add(investorsLabel);
            mybox.getChildren().add(investors);

            Text amountRaisedLabel = new Text("Amount raised by investors:");
            amountRaisedLabel.setStyle("-fx-font-weight: bold");
            double amountRaisedby = loan.getAmount() - loan.getRemainingAmount();
            Text amountRaised = new Text(String.valueOf(amountRaisedby));
            mybox.getChildren().add(amountRaisedLabel);
            mybox.getChildren().add(amountRaised);

            Text amountNeededLabel = new Text("Amount needed to start loan:");
            amountNeededLabel.setStyle("-fx-font-weight: bold");

            Text amountNeeded = new Text(String.valueOf(loan.getRemainingAmount()));
            mybox.getChildren().add(amountNeededLabel);
            mybox.getChildren().add(amountNeeded);
        }

        if (loan.getStatus().equals("ACTIVE")){

            Text investorsLabel = new Text("Investors:");
            investorsLabel.setStyle("-fx-font-weight: bold");
            Text investors = new Text(loan.getLenders().toString());
            mybox.getChildren().add(investorsLabel);
            mybox.getChildren().add(investors);

            Text nextPaymentDateLabel = new Text("Next payment date:");
            nextPaymentDateLabel.setStyle("-fx-font-weight: bold");
            Text nextPaymentDate = new Text(String.valueOf(loan.getTimeNextPayment() + Integer.parseInt(mainController.currentYazProperty.get())));
            mybox.getChildren().add(nextPaymentDateLabel);
            mybox.getChildren().add(nextPaymentDate);


        }

        if(loan.getStatus().equals("ACTIVE") ||
                loan.getStatus().equals("RISK")||
                loan.getStatus().equals("FINISHED")){

            Text paymentLabel = new Text("Payments made on loan:");
            paymentLabel.setStyle("-fx-font-weight: bold");


            Text transactions = new Text(loan.paymentsString);
            mybox.getChildren().add(paymentLabel);
            mybox.getChildren().add(transactions);

            Text paySoFarLabel = new Text("amount paid so far:");
            paySoFarLabel.setStyle("-fx-font-weight: bold");
            Text paySoFar = new Text(String.valueOf(loan.getAmountPaidUntilNow()));
            mybox.getChildren().add(paySoFarLabel);
            mybox.getChildren().add(paySoFar);


            Text needsTobePaidLabel = new Text("amount that needs to be paid:");
            needsTobePaidLabel.setStyle("-fx-font-weight: bold");
            Text needsTobePaid = new Text(String.valueOf(loan.getCompleteAmountToBePaid() - loan.getAmountPaidUntilNow()));
            mybox.getChildren().add(needsTobePaidLabel);
            mybox.getChildren().add(needsTobePaid);
        }

        if (loan.getStatus().equals("FINISHED")){
            Text investorsLabel = new Text("Investors:");
            investorsLabel.setStyle("-fx-font-weight: bold");
            Text investors = new Text(loan.getLenders().toString());
            mybox.getChildren().add(investorsLabel);
            mybox.getChildren().add(investors);
        }

        mybox.setAlignment(Pos.TOP_CENTER);
    }
}
