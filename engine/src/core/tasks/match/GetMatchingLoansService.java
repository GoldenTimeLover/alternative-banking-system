package core.tasks.match;


import core.entities.Loan;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.util.List;


public class GetMatchingLoansService extends Service<ObservableList<Loan>> {

    private List<Loan> loans;
    private String customerID;
    private List<String> categoryFilters;
    private double amount;
    private double interest;
    private int time;
    private int amountOfOpenLoans;
    private int maxPercentage;

    public GetMatchingLoansService(List<Loan> loans, String customerID, List<String> categoryFilters, double amount, double interest, int time, int amountOfOpenLoans, int maxPercentage) {
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
    protected Task<ObservableList<Loan>> createTask() {
        return new GetMatchingLoansTask(loans,customerID,categoryFilters,amount,interest,time,amountOfOpenLoans,maxPercentage);
    }
}
