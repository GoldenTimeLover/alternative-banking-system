package core.dtos;

import core.entities.Customer;
import core.entities.Loan;
import core.entities.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AdminLoanDTO {

    String id;
    int startDate;
    int endDate;
    double amount;
    double remainingAmount;
    String ownerName;
    ArrayList<String> lenders;
    String status;
    String category;
    int interestRate;
    int lengthOfTime;
    int timeBetweenPayments;
    int timeNextPayment;
    double unpaidDebt;
    double paymentPerYaz ;
    double singlePayment;
    double singlePaymentTotal ;
    double completeAmountToBePaid;
    double amountPaidUntilNow;
    boolean paidThisYaz = false;
    String whoSelling = "";
    public String paymentsString = "";


    public AdminLoanDTO(String id, int startDate, int endDate, double amount, double remainingAmount, String ownerName, ArrayList<String> lenders, String status, String category, int interestRate, int lengthOfTime, int timeBetweenPayments, int timeNextPayment, double unpaidDebt, double paymentPerYaz, double singlePayment, double singlePaymentTotal, double completeAmountToBePaid, double amountPaidUntilNow, boolean paidThisYaz) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.amount = amount;
        this.remainingAmount = remainingAmount;
        this.ownerName = ownerName;
        this.lenders = lenders;
        this.status = status;
        this.category = category;
        this.interestRate = interestRate;
        this.lengthOfTime = lengthOfTime;
        this.timeBetweenPayments = timeBetweenPayments;
        this.timeNextPayment = timeNextPayment;
        this.unpaidDebt = unpaidDebt;
        this.paymentPerYaz = paymentPerYaz;
        this.singlePayment = singlePayment;
        this.singlePaymentTotal = singlePaymentTotal;
        this.completeAmountToBePaid = completeAmountToBePaid;
        this.amountPaidUntilNow = amountPaidUntilNow;
        this.paidThisYaz = paidThisYaz;
    }

    public AdminLoanDTO(Loan l){
        this.id = l.getId();
        this.startDate = l.getStartDate();
        this.endDate = l.getEndDate();
        this.amount = l.getAmount();
        this.remainingAmount = l.getRemainingAmount();
        this.ownerName = l.getOwnerName();

        this.lenders = new ArrayList<>();
        for (Customer c : l.getLenders())
            this.lenders.add(c.getId());

        this.status = l.getStatus().toString();
        this.category = l.getCategory();
        this.interestRate = l.getInterestRate();
        this.lengthOfTime = l.getLengthOfTime();
        this.timeBetweenPayments = l.getTimeBetweenPayments();
        this.timeNextPayment = l.getTimeNextPayment();
        this.unpaidDebt = l.getUnpaidDebt();
        this.paymentPerYaz = l.getSinglePayment();
        this.singlePayment = l.getSinglePayment();
        this.singlePaymentTotal = l.getSinglePaymentTotal();
        this.completeAmountToBePaid = l.getCompleteAmountToBePaid();
        this.amountPaidUntilNow = l.getAmountPaidUntilNow();
        this.paidThisYaz = l.isPaidThisYaz();
        for (Transaction t: l.getPayments()) {
            paymentsString += t.toString() +" |";

        }
    }

    public String getId() {
        return id;
    }

    public int getStartDate() {
        return startDate;
    }

    public int getEndDate() {
        return endDate;
    }

    public double getAmount() {
        return amount;
    }

    public double getRemainingAmount() {
        return remainingAmount;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public ArrayList<String> getLenders() {
        return lenders;
    }

    public String getStatus() {
        return status;
    }

    public String getCategory() {
        return category;
    }

    public int getInterestRate() {
        return interestRate;
    }

    public int getLengthOfTime() {
        return lengthOfTime;
    }

    public int getTimeBetweenPayments() {
        return timeBetweenPayments;
    }

    public int getTimeNextPayment() {
        return timeNextPayment;
    }

    public double getUnpaidDebt() {
        return unpaidDebt;
    }

    public double getPaymentPerYaz() {
        return paymentPerYaz;
    }

    public double getSinglePayment() {
        return singlePayment;
    }

    public double getSinglePaymentTotal() {
        return singlePaymentTotal;
    }

    public double getCompleteAmountToBePaid() {
        return completeAmountToBePaid;
    }

    public double getAmountPaidUntilNow() {
        return amountPaidUntilNow;
    }

    public boolean isPaidThisYaz() {
        return paidThisYaz;
    }

    public String getWhoSelling() {
        return whoSelling;
    }

    public void setWhoSelling(String whoSelling) {
        this.whoSelling = whoSelling;
    }
}
