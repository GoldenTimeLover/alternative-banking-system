package core.entities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Customer {

    private String id;
    private double balance;
    private ArrayList<Loan> givingLoans;
    private ArrayList<Loan> takingLoans;
    private ArrayList<Transaction> transactions;

    public Customer(String id, int balance, ArrayList<Loan> givingLoans,
                    ArrayList<Loan> takingLoans,ArrayList<Transaction> transactions) {
        this.id = id;
        this.balance = balance;
        this.givingLoans = givingLoans;
        this.takingLoans = takingLoans;
        this.transactions = transactions;
    }

    public void addTransaction(Transaction transaction){
        transactions.add(transaction);
    }
    public void addLoan(Loan loan){
        takingLoans.add(loan);
    }
    public void addLending(Loan loan) {
        for (int i = 0; i < givingLoans.size(); i++) {
            if (loan.getId().equals(givingLoans.get(i).getId())){
                return;
            }
        }
        givingLoans.add(loan);
    }

    @Override
    public String toString() {
        return id + "-(Customer)";
    }

    public String getId() {
        return id;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String stringForConsole(){
        String returnedString = "Customer ID/Name: " + id + "\n" +
                "Current Account Balance: " + balance + "\n" +
                "Account Movements/Transactions:\n" + getTransactionsString() + "\n" +
                "Loans The Customer GAVE:\n" + getLoansCustomerGaveString() + "\n" +
                "Loans The Customer RECEIVED/IS ASKING FOR:\n" + getLoansCustomerReivedString() +"\n";

        return returnedString;
    }


    private String getLoansCustomerGaveString(){
        String res = "";
        for (int i = 0; i < givingLoans.size(); i++) {
            Loan l = givingLoans.get(i);
            res+= "Loan ID: " + l.getId() +"\n";
            res+= "Loan Category: " + l.getCategory() + "\n";
            res+= "Loan Amount: " + l.getAmount() + "\n";
            res+=  "Original length of time: " + l.getLengthOfTime() + "\n" +
                    "Loan Interest: " + l.getInterestRate() + "\n" +
                    "Pays every: " + l.getTimeBetweenPayments() + "\n" +
                    "Loan Status: " + l.getStatus() + "\n";
            if (l.getStatus().equals(Loan.LoanStatus.PENDING)){
                res+= "Amount Needed to become active: " + l.getRemainingAmount();
            }
            if(l.getStatus().equals(Loan.LoanStatus.ACTIVE)){
                res+= "Loan Start Date: " + l.getStartDate() + "\n";
                res+= "Will pay in " + l.getTimeNextPayment() + " YAZ.\n";
                res+= "The loan will pay " + l.getSinglePaymentTotal() +"\n";
            }
            if (l.getStatus().equals(Loan.LoanStatus.RISK)){
                res+= "Loan Start Date: " + l.getStartDate() + "\n";
                res+= "The amount of unpaid debt of this loan " + l.getUnpaidDebt()+"\n";
            }
            if (l.getStatus().equals(Loan.LoanStatus.FINISHED)){
                res+= "Loan Start Date: " + l.getStartDate() + "\n";
                res+= "Loan End Date: " + l.getEndDate() + "\n";
            }
        }
        if (res.equals(""))
            return "None";
        else
            return res;
    }
    private String getLoansCustomerReivedString(){
        String res = "";
        for (int i = 0; i < takingLoans.size(); i++) {
            Loan l = takingLoans.get(i);
            res+= "Loan ID: " + l.getId() +"\n";
            res+= "Loan Category: " + l.getCategory() + "\n";
            res+= "Loan Amount: " + l.getAmount() + "\n";
            res+=  "Original length of time: " + l.getLengthOfTime() + "\n" +
                    "Loan Interest: " + l.getInterestRate() + "\n" +
                    "Pays every: " + l.getTimeBetweenPayments() + "\n" +
                    "Loan Status: " + l.getStatus() + "\n";
            if (l.getStatus().equals(Loan.LoanStatus.PENDING)){
                res+= "Amount Needed to become active: " + l.getRemainingAmount();
            }
            if(l.getStatus().equals(Loan.LoanStatus.ACTIVE)){
                res+= "Loan Start Date: " + l.getStartDate() + "\n";
                res+= "Will pay in " + l.getTimeNextPayment() + " YAZ.\n";
                res+= "The loan will pay " + l.getSinglePaymentTotal() +"\n";
            }
            if (l.getStatus().equals(Loan.LoanStatus.RISK)){
                res+= "Loan Start Date: " + l.getStartDate() + "\n";
                res+= "The amount of unpaid debt of this loan " + l.getUnpaidDebt()+"\n";
            }
            if (l.getStatus().equals(Loan.LoanStatus.FINISHED)){
                res+= "Loan Start Date: " + l.getStartDate() + "\n";
                res+= "Loan End Date: " + l.getEndDate() + "\n";
            }
        }
        if (res.equals(""))
            return "None";
        else
            return res;
    }

    private String getTransactionsString(){
        Collections.sort(transactions);
        String res = "";
        if (transactions.size() == 0){
            return "None.";
        }
        for (int i = 0; i < transactions.size(); i++) {
            int date = transactions.get(i).date;
            double amount  = transactions.get(i).amount;
            double balanceAfter = transactions.get(i).balanceAfter;
            double balanceBefore = transactions.get(i).balanceBefore;
            String type = transactions.get(i).type.equals(Transaction.TransactionType.DEPOSIT) ? "+" : "-";
            res += "Date: " + date + "\n" + "Amount: " + amount + type + "\n" + "Balance Before: " + balanceBefore + "\n" +
                    "Balance After: " + balanceAfter +"\n";
        }
        return res;
    }


}
