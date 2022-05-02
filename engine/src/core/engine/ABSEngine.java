package core.engine;

import core.Exceptions.FileFormatException;
import core.entities.Customer;
import core.entities.Loan;
import core.entities.Transaction;
import core.utils.utils;
import core.xml.ABSXmlParser;

import java.util.*;

public class ABSEngine implements Engine{

    List<Loan> loans;
    List<Customer> customers;
    List<String> categories;
    int currentTime = 1;
    boolean dataLoaded= false;
    String currentFilePath = "";

    public ABSEngine(){


        this.customers = new ArrayList<>();
        this.loans = new ArrayList<>();
        this.categories = new ArrayList<>();

    }


    public Customer findCustomerById(String id){
        for (int i = 0; i < customers.size(); i++) {
            if (id.equals(customers.get(i).getId())){
                return customers.get(i);
            }
        }
        return null;
    }

    public String getFilePath(){
        return currentFilePath;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void loadDataFromFile(String filePath) throws FileFormatException {

        System.out.println("Trying to Load Data From... " + filePath);
        ABSXmlParser xmlParser = new ABSXmlParser(filePath);

        //Load categories customers and loans from xmlFIle
        categories = xmlParser.getCategoriesFromFile();
        customers = xmlParser.getCustomersFromFile();
        loans = xmlParser.getLoansFromFile();

        //Set Current Time to 1 whenever you load new Data
        this.currentTime = 1;

        try {
            verifyData();
            //Connect the Loans and The Customers you got from file
            connectDataLoadedFromFile();

            dataLoaded = true;
            currentFilePath = filePath;
            System.out.println("Data Loaded Successfully!");
        }catch (FileFormatException e){
            throw e;
        }
    }

    private void connectDataLoadedFromFile(){
        for (int i = 0; i < loans.size(); i++) {
            Customer temp = null;
            for (int j = 0; j < customers.size(); j++) {
                if (customers.get(j).getId().equals(loans.get(i).getOwnerName())){
                    temp = customers.get(j);
                }
            }
            loans.get(i).setBorrower(temp);
        }

        for (int i = 0; i < customers.size(); i++) {

            for (int j = 0; j < loans.size(); j++) {
                if (loans.get(j).getOwnerName().equals(customers.get(i).getId())){
                    customers.get(i).addLoan(loans.get(j));
                }
            }
        }


    }

    public List<Loan> getLoans() {
        return loans;
    }

    public List<Customer> getCustomers() {
        return customers;
    }

    private void verifyData() throws FileFormatException{

        List<String> customerNames = new ArrayList<>();
        for (Customer c : customers){
            customerNames.add(c.getId());
        }

        for (int i = 0; i < customerNames.size(); i++) {
            for (int j = i + 1 ; j < customerNames.size(); j++) {
                if (customerNames.get(i).equals(customerNames.get(j))) {
                    throw new FileFormatException("File Defective! The name " + customerNames.get(i) +
                            " Appears in more than one time in the file");
                }
            }
        }

        for (int i = 0; i < loans.size(); i++) {

            if (!categories.contains(loans.get(i).getCategory())){
                throw new FileFormatException("File Defective! The category " + loans.get(i).getCategory() +
                        " Appears in a loan but doesn't appear in category List!");
            }

            if (!customerNames.contains(loans.get(i).getOwnerName())){
                throw new FileFormatException("File Defective! The Owner name " + loans.get(i).getOwnerName() +
                        " Appears in a loan but doesn't appear in Customer List!");
            }

            checkLoanTimes(loans.get(i));
        }
    }

    public void checkLoanTimes(Loan loan) throws FileFormatException {
        if (loan.getLengthOfTime() % loan.getTimeBetweenPayments()  != 0){
            throw new FileFormatException("File Defective! Time between payments and total time of" +
                    "loan don't divide without remainder in loan " + loan.getId());
        }
        int divide =  loan.getLengthOfTime() / loan.getTimeBetweenPayments();
        if(loan.getSinglePayment() * divide != loan.getAmount()){
            throw new FileFormatException(" File Defective! Loan payments don't add up to full amount of loan\n" +
                    loan.getId());
        }
    }
    public boolean isDataLoaded() {
        return dataLoaded;
    }


    public List<Loan> findPossibleLoanMatches(String customerID,List<String> categoryFilters,double amount,double interest,int time){
        List<Loan> possibleLoans = new ArrayList<>();

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
            if ((!curLoan.getOwnerName().equals(customerID)) && inCategories && okInterest && okTime){
                possibleLoans.add(loans.get(i));
            }

        }
        return possibleLoans;
    }

    public void matchLoan(String loanId,double amountOfMoney,String lenderId){

        Loan theLoan = null;
        Customer lender = findCustomerById(lenderId);
        for (int i = 0; i < loans.size(); i++) {
            if (loanId.equals(loans.get(i).getId())){
                theLoan = loans.get(i);
            }
        }


        if (theLoan == null){
            System.out.println("Null Pointer to Loan Process Canceled");
            return;
        }
        if (lender == null){
            System.out.println("Null Pointer to Lender Process Canceled");
            return;
        }


        // add lender to list of lender
        theLoan.addLender(lender,amountOfMoney);

        // set amount of money remaining to start loan
        theLoan.setRemainingAmount(utils.round(theLoan.getRemainingAmount() - amountOfMoney));

        //Update loan from new->pending->active
        theLoan.updateStatus();

        //If loan became active add money to the loaners account via transaction
        if (theLoan.getStatus().equals(Loan.LoanStatus.ACTIVE)){
            addTransactionToCustomer(theLoan.getOwnerName(),theLoan.getAmount(), Transaction.TransactionType.DEPOSIT);
        }
        //remove money and add transaction from lender's account
        addTransactionToCustomer(lenderId,amountOfMoney,Transaction.TransactionType.WITHDRAW);

        //add Loan to the list of loan the lender gave
        lender.addLending(theLoan);


    }

    public void moveTimeForward(){



        // function to collect money from loaners
        List<Loan> activeLoans = getActiveLoans();


        payLoans(activeLoans);

        // set current time plus 1
        currentTime+= 1;
    }

    private void payLoans(List<Loan> loanList){

        //Sort the list of Active loans
        //First sort by which loan started earlier
        //Then sort by which loan is greater
        Collections.sort(loanList, new Comparator<Loan>() {
            @Override
            public int compare(Loan o1, Loan o2) {
                Integer x1 = (o1).getStartDate();
                Integer x2 = (o2).getStartDate();
                int sComp = x1.compareTo(x2);

                if (sComp != 0) {
                    return sComp;
                }

                Double y1 = (o1).getAmount();
                Double y2 = (o2).getAmount();
                return y1.compareTo(y2);
            }
        });



        for (int i = 0; i < loanList.size(); i++) {
            payLoan(loanList.get(i));
        }
    }

    private void payLoan(Loan loan){



        //If it's still not time to pay
        if (loan.getTimeNextPayment() > 1){
            loan.setTimeNextPayment(loan.getTimeNextPayment() - 1);
            return;
        }
        else{
            //reset to the amount of time between payments
            loan.setTimeNextPayment(loan.getTimeBetweenPayments());
        }

        //the customer that needs to pay
        Customer c = findCustomerById(loan.getOwnerName());

        // money that needs to be paid
        double moneyToReturn = loan.getSinglePaymentTotal();
        moneyToReturn += loan.getUnpaidDebt();

        //if the customer is so late on payments he doesn't need to pay anymore
        if (loan.getLengthOfTime() + loan.getStartDate() <= currentTime){
            moneyToReturn = loan.getUnpaidDebt();
        }


        // if the customer has enough money to return
        if (c.getBalance() >= moneyToReturn){
            //Remove money for loaner's account with transaction
            addTransactionToCustomer(loan.getOwnerName(), (int) moneyToReturn, Transaction.TransactionType.WITHDRAW);

            //calculate the amount each lender should recieve proportionally to what he gave
            Map<String, Double> lenderMap= loan.getLenderAmounts();
            for (int i = 0; i < loan.getLenders().size(); i++) {
                String lenderName = loan.getLenders().get(i).getId();
                double amountLent = lenderMap.get(lenderName);
                double percentageLent =  amountLent /  loan.getAmount();
                double lenderReturn = utils.round(percentageLent * moneyToReturn);
                addTransactionToCustomer(lenderName, lenderReturn, Transaction.TransactionType.DEPOSIT);
            }
            loan.getPayments().add(new Transaction(moneyToReturn,currentTime,Transaction.TransactionType.DEPOSIT,0,0));

            if (loan.getLengthOfTime() + loan.getStartDate() <= currentTime + 1){
                loan.setStatus(Loan.LoanStatus.FINISHED);
                loan.setEndDate(currentTime);
            }
        }
        // if customer doesn't have enough money in his account
        else{
            // set loan to risk mode
            loan.setStatus(Loan.LoanStatus.RISK);
            // add money supposed to return to unpaid counter
            loan.setUnpaidDebt(moneyToReturn);
        }
    }

    /**
     * getter function for all loans that are ACTIVE or RISK (loans that need to be paid).
     * @return List<Loan> of loans
     */
    public List<Loan> getActiveLoans(){

        List<Loan> output = new ArrayList<>();

        for (int i = 0; i < loans.size(); i++) {

            if (loans.get(i).getStatus().equals(Loan.LoanStatus.ACTIVE) ||
                    loans.get(i).getStatus().equals(Loan.LoanStatus.RISK))
            output.add(loans.get(i));
        }
        return output;
    }

    /**
     * Function generates a new transaction base on the parameters it was given
     * and adds that transaction to the customer(whose id\name is a parameter)
     * transaction list. Also, it updates the current balance of the customer.
     * @param customerName A string value of the customer's name (it's also his unique id)
     * @param amount The amount of money to deposit\withdraw from the account
     * @param type enum of if the transaction is WITHDRAW or DEPOSIT
     */
    public void addTransactionToCustomer(String customerName, double amount, Transaction.TransactionType type){

        //iterate over all customers
        for (int i = 0; i < customers.size(); i++) {
                Customer curCustomer = customers.get(i);
                //find customer with the matching name
                if (curCustomer.getId().equals(customerName)){
                    double balance = curCustomer.getBalance();
                    // if it's a deposit add the funds
                    if (type.equals(Transaction.TransactionType.DEPOSIT)) {
                        curCustomer.addTransaction(new Transaction(amount, currentTime, type,
                                balance, utils.round(balance + amount)));
                        curCustomer.setBalance(utils.round(balance + amount));
                    }

                    // if it's a withdrawal remove the funds
                    else if(type.equals(Transaction.TransactionType.WITHDRAW)){
                        curCustomer.addTransaction(new Transaction(amount, currentTime, type,
                                balance, utils.round(balance - amount)));
                        curCustomer.setBalance(utils.round(balance - amount));
                    }
                }
        }
    }


    public int getCurrentTime() {
        return currentTime;
    }
}
