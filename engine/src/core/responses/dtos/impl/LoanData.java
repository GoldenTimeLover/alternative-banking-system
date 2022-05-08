package core.responses.dtos.impl;


import core.entities.Loan;

import core.responses.dtos.DataTransferObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LoanData implements DataTransferObject {


    public String id;
    public int startDate;
    public int endDate;
    public double amount;
    public double remainingAmount;
    public CustomerData borrower;
    public ArrayList<CustomerData> lenders;
    public Map<String,Double> lenderAmounts;
    public Loan.LoanStatus status;
    public String category;
    public int interestRate;
    public String ownerName;
    public int lengthOfTime;
    public int timeBetweenPayments;
    public int timeNextPayment;
    public double unpaidDebt;
    public double paymentPerYaz ;
    public double singlePayment;
    public double singlePaymentTotal ;
    public List<TransactionData> payments;

}
