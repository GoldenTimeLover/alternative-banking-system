package core.tasks.match;

import core.entities.Loan;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

import java.util.ArrayList;
import java.util.List;

public class GetMatchingLoansTask extends Task<ObservableList<Loan>> {


    private List<Loan> loans;
    private String customerID;
    private List<String> categoryFilters;
    private double amount;
    private double interest;
    private int time;
    private int amountOfOpenLoans;
    private int maxPercentage;


    private final int SLEEP_TIME = 300;

    public GetMatchingLoansTask(List<Loan> loans, String customerID, List<String> categoryFilters, double amount, double interest, int time, int amountOfOpenLoans, int maxPercentage) {
        this.loans = loans;
        this.customerID = customerID;
        this.categoryFilters = categoryFilters;
        this.amount = amount;
        this.interest = interest;
        this.time = time;
        this.amountOfOpenLoans = amountOfOpenLoans;
        this.maxPercentage = maxPercentage;
    }

    @Override
    protected ObservableList<Loan> call() throws Exception {


        List<Loan> possibleLoans = new ArrayList<>();

        updateProgress(0,500);
        Thread.sleep(SLEEP_TIME);


        updateProgress(100,500);

        for (int i = 0; i < loans.size(); i++) {

            Loan curLoan = loans.get(i);
            if (curLoan.getStatus().equals(Loan.LoanStatus.RISK) ||
                    curLoan.getStatus().equals(Loan.LoanStatus.ACTIVE)||
                    curLoan.getStatus().equals(Loan.LoanStatus.FINISHED)){
                continue;
            }
            // if current loan isn't owned by customer requesting match
            // and is one of the categories in the loan
            // and the interest loan is lower
            boolean inCategories =  (categoryFilters.contains(curLoan.getCategory()) || categoryFilters.size() == 0);
            boolean okInterest =  curLoan.getInterestRate() >= interest;
            boolean okTime = curLoan.getLengthOfTime() >= time;
            int openLoansCount = 0;
            for(Loan l : curLoan.getBorrower().getTakingLoans()){
                if (!l.getStatus().equals(Loan.LoanStatus.FINISHED)){
                    openLoansCount++;
                }
            }

            // if the amount of loans the customer has open is bigger than the amount of loans
            //the lender is accepting
            boolean okOpenLoanAmount;
            if(openLoansCount > amountOfOpenLoans && amountOfOpenLoans != 0){
                okOpenLoanAmount = false;
            }else{
                okOpenLoanAmount = true;
            }

            if ((!curLoan.getOwnerName().equals(customerID)) && inCategories && okInterest && okTime && okOpenLoanAmount){
                possibleLoans.add(loans.get(i));
            }

        }
        updateProgress(200,500);
        Thread.sleep(SLEEP_TIME);
        updateProgress(300,500);

        updateMessage("Finalizing...");
        Thread.sleep(SLEEP_TIME);
        updateProgress(400,500);
        Thread.sleep(SLEEP_TIME);

        updateMessage("Done...");
        updateProgress(500,500);
        Thread.sleep(SLEEP_TIME);
        ObservableList<Loan> returnedVal = FXCollections.observableArrayList();
        returnedVal.setAll(possibleLoans);

        return returnedVal;

    }
}
