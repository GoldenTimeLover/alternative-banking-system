package core.engine;

import core.Exceptions.FileFormatException;
import core.Exceptions.LoanProccessingException;
import core.Exceptions.NotEnoughMoneyException;
import core.entities.Customer;
import core.entities.Loan;
import core.entities.Notification;
import core.entities.Transaction;
import core.utils.utils;
import core.xml.ABSXmlParser;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Task;

import java.util.*;

public class ABSEngine implements Engine{

    private List<Loan> loans;
    private List<Customer> customers;
    private List<String> categories;
    private int currentTime = 1;
    private boolean dataLoaded= false;
    private final StringProperty currentFilePath = new SimpleStringProperty(this,"currentFilePath","No File Selected");
    private final Map<String,List<Notification>> notifications;

    private Task<Boolean> currentRunningTask;

    public Map<String, List<Notification>> getNotifications() {
        return notifications;
    }


    public IntegerProperty currTimeForGuiProperty() {
        return CurrTimeForGui;
    }

    IntegerProperty CurrTimeForGui = new SimpleIntegerProperty(1);

    public ABSEngine(){


        this.customers = new ArrayList<>();
        this.loans = new ArrayList<>();
        this.categories = new ArrayList<>();
        this.notifications = new HashMap<>();

    }


    public Customer findCustomerById(String id){
        for (int i = 0; i < customers.size(); i++) {
            if (id.equals(customers.get(i).getId())){
                return customers.get(i);
            }
        }
        return null;
    }

    public String getCurrentFilePath() {
        return currentFilePath.get();
    }

    public StringProperty currentFilePathProperty() {
        return currentFilePath;
    }

    public void setCurrentFilePath(String currentFilePath) {
        this.currentFilePath.set(currentFilePath);
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
        CurrTimeForGui.set(currentTime);

        verifyData();
        //Connect the Loans and The Customers you got from file
        connectDataLoadedFromFile();

        dataLoaded = true;
        currentFilePath.set(filePath);
        System.out.println("Data Loaded Successfully!");
    }


    /**
     * <ul>
     *
     * <li>
     * Iterates over each loan object read from .xml file.
     * Find sets each loan owner to the customer with the matching ID.
     * </li>
     *
     *<li>
     * Iterates over all the customers and adds to each customer the loans that are
     * related to him.
     *</li>
     *<li>
     * Sets a new arrayList of notifications for each customer
     *</li>
     * </ul>
     */
    private void connectDataLoadedFromFile(){
        for (Loan loan : loans) {
            Customer temp = null;
            for (Customer customer : customers) {
                if (customer.getId().equals(loan.getOwnerName())) {
                    temp = customer;
                }
                notifications.putIfAbsent(customer.getId(), new ArrayList<Notification>());
            }
            loan.setBorrower(temp);

            assert temp != null;
            temp.addLoan(loan);
        }





    }

    public List<Loan> getLoans() {
        return loans;
    }

    public List<Customer> getCustomers() {
        return customers;
    }


    /**
     *
     * performs check to ensure that the .xml file read is valid.
     * - doesn't have owners of loans that are not customers of the system
     * - doesn't have loan categories that appear in loans but aren't part of the system.
     * - doesn't have repeating customers
     * - check that loans received from file divide evenly across the timespan provided.
     */
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


    /**
     * <p>
     *     scans all the loans in the system that are not yet active (PENDING,NEW)
     *     and filters them based on the following parameters:
     * </p>
     * <ul>
     *     <li>Make sure the customer looking to lend isn't lending to himself.</li>
     *     <li>Find only loans that are one of the categories provided in categoryFilters.</li>
     *     <li>The min interest that the lender is willing to accept.</li>
     *     <li>The minimum amount of time the lender is willing the loan to span across.</li>
     * </ul>
     */
//    public void findPossibleLoanMatches(Consumer<List<Loan>> loansDelegate,String customerID,List<String> categoryFilters,double amount,double interest,int time,int amountOfOpenLoans,int maxPercentage){
//        List<Loan> possibleLoans = new ArrayList<>();
//
//        for (int i = 0; i < loans.size(); i++) {
//
//            Loan curLoan = loans.get(i);
//            if (curLoan.getStatus().equals(Loan.LoanStatus.RISK) ||
//                    curLoan.getStatus().equals(Loan.LoanStatus.ACTIVE)||
//                    curLoan.getStatus().equals(Loan.LoanStatus.FINISHED)){
//                continue;
//            }
//            // if current loan isn't owned by customer requesting match
//            // and is one of the categories in the loan
//            // and the interest loan is lower
//            boolean inCategories =  (categoryFilters.contains(curLoan.getCategory()) || categoryFilters.size() == 0);
//            boolean okInterest =  curLoan.getInterestRate() >= interest;
//            boolean okTime = curLoan.getLengthOfTime() >= time;
//            int openLoansCount = 0;
//            for(Loan l : curLoan.getBorrower().getTakingLoans()){
//                if (!l.getStatus().equals(Loan.LoanStatus.FINISHED)){
//                    openLoansCount++;
//                }
//            }
//            boolean okOpenLoanAmount = openLoansCount <= amountOfOpenLoans;
//
//            if(amountOfOpenLoans == 0){
//                okOpenLoanAmount = true;
//            }
//
//
//            if ((!curLoan.getOwnerName().equals(customerID)) && inCategories && okInterest && okTime && okOpenLoanAmount){
//                possibleLoans.add(loans.get(i));
//            }
//
//        }
//        return possibleLoans;
//
//        currentRunningTask = new MatchLoansTask(loansDelegate,this.loans,customerID,categoryFilters,amount,interest,time,amountOfOpenLoans,maxPercentage);
//
//        new Thread(currentRunningTask).start();
//
//    }

    public double matchLoan(String loanId,double amountOfMoney,String lenderId,int maxPercentageOfLoan){

        Loan theLoan = null;
        Customer lender = findCustomerById(lenderId);
        boolean customerCareAboutPercentage = maxPercentageOfLoan > 0;


        for (Loan loan : loans) {
            if (loanId.equals(loan.getId())) {
                theLoan = loan;
            }
        }


        if (theLoan == null){
            System.out.println("Null Pointer to Loan Process Canceled");
            return 0.0;
        }
        if (lender == null){
            System.out.println("Null Pointer to Lender Process Canceled");
            return 0.0;
        }


        double maxAmountWillingToInvestByPercentage = utils.round(theLoan.getAmount()*(maxPercentageOfLoan/100.0f));


        // if the customer has already invested in this loan and cares about their percentage in the loan
        if (customerCareAboutPercentage && theLoan.getLenderAmounts().containsKey(lenderId)){

            // if the amount the customer already invested and the amount he is trying to invest is greater than the
            // maximum amount they allow
            if(theLoan.getLenderAmounts().get(lenderId) + amountOfMoney  > maxAmountWillingToInvestByPercentage){
                // um... it's um... it's... um.... um... ....
                maxAmountWillingToInvestByPercentage -= theLoan.getLenderAmounts().get(lenderId);
            }
        }

        // if the customer cares about percentage and the amount they are trying to invest is bigger than the percent they allow
        if(customerCareAboutPercentage && maxAmountWillingToInvestByPercentage < amountOfMoney){
            amountOfMoney = maxAmountWillingToInvestByPercentage;
        }

        if (customerCareAboutPercentage && maxAmountWillingToInvestByPercentage == 0.0){
            return 0.0;
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
            if(theLoan.getTimeBetweenPayments() == 1){
                List<Loan> temp = new ArrayList<>();
                temp.add(theLoan);
                sendNotifications(temp);
            }
        }
        //remove money and add transaction from lender's account
        addTransactionToCustomer(lenderId,amountOfMoney,Transaction.TransactionType.WITHDRAW);

        //add Loan to the list of loan the lender gave
        lender.addLending(theLoan);

        return amountOfMoney;

    }



    public void moveTimeForward(){

        // function to collect money from loaners
        List<Loan> activeLoans = getActiveLoans();

        // set current time plus 1
        currentTime+= 1;
        CurrTimeForGui.set(currentTime);

        // reduce time until next payment, if not paid update status to risk
        reduceTimeNextPayment(activeLoans);

        //send to notification to customers that need to pay this date
        sendNotifications(activeLoans);

        setLoansToUnPaidThisTurn(activeLoans);
    }



    private void setLoansToUnPaidThisTurn(List<Loan> loanList){
        for (Loan loan: loanList) {
            loan.setPaidThisYaz(false);
        }
    }

    private void sendNotifications(List<Loan> loanList){

        for (Loan l : loanList) {
            double amount  = Math.min(l.getSinglePaymentTotal() + l.getUnpaidDebt(),l.getCompleteAmountToBePaid() - l.getAmountPaidUntilNow());

            Notification notification = new Notification("Loan" + l.getId() + "Needs Payment",currentTime,"" +
                    "Dear Mr./Mrs./Miss. " + l.getOwnerName() + " This is a formal notification that you need to repay the" +
                    "amount of " + amount + " for the loan '"+ l.getId() +"'.");
            if(notifications.get(l.getOwnerName()).contains(notification)){
                return;
            }
            if (l.getTimeNextPayment() <= 1){
                notifications.get(l.getOwnerName()).add(0,notification);
            }


        }


    }

    public void reduceTimeNextPayment(List<Loan> loanList){


        for (Loan loan : loanList) {
            //If it's still not time to pay
            if (loan.getTimeNextPayment() > 1) {
                loan.setTimeNextPayment(loan.getTimeNextPayment() - 1);
                return;
            } else {
                //reset to the amount of time between payments
                loan.setTimeNextPayment(loan.getTimeBetweenPayments());


                // if this was a yaz to be paid and the customer did not pay... set to risk
                if (!loan.isPaidThisYaz()){

                    // if the amount the customer paid until now + the amount he owes + the amount for a single payment
                    // is greater than the total he has to pay set the amount to the remainder...
                    if(loan.getUnpaidDebt() == loan.getCompleteAmountToBePaid() - loan.getAmountPaidUntilNow()){
                        System.out.println("do nothing");
                    }
                    else if(loan.getUnpaidDebt() + loan.getSinglePaymentTotal() <= loan.getCompleteAmountToBePaid() - loan.getAmountPaidUntilNow()){
                        loan.setUnpaidDebt(loan.getUnpaidDebt() + loan.getSinglePaymentTotal());


                    }
                    else if(loan.getUnpaidDebt() + loan.getSinglePaymentTotal() > loan.getCompleteAmountToBePaid() - loan.getAmountPaidUntilNow()){
                        System.out.println("Something is wrong");
                    }

                    else{
                        loan.setUnpaidDebt(loan.getCompleteAmountToBePaid() -loan.getAmountPaidUntilNow());
                    }
                    loan.setStatus(Loan.LoanStatus.RISK);
                }
            }
        }
    }

    public void payEntireLoan(Loan loan){

        //the customer that needs to pay
        Customer c = findCustomerById(loan.getOwnerName());
        double moneyToReturn = loan.getCompleteAmountToBePaid() - loan.getAmountPaidUntilNow();

        if (c.getBalance() >= moneyToReturn){

            //set did pay this yaz to true
            loan.setPaidThisYaz(true);



            //Remove money for loaner's account with transaction
            addTransactionToCustomer(loan.getOwnerName(), (int) moneyToReturn, Transaction.TransactionType.WITHDRAW);

            //calculate the amount each lender should receive proportionally to what he gave
            Map<String, Double> lenderMap= loan.getLenderAmounts();
            for (int i = 0; i < loan.getLenders().size(); i++) {
                String lenderName = loan.getLenders().get(i).getId();
                double amountLent = lenderMap.get(lenderName);
                double percentageLent =  amountLent /  loan.getAmount();
                double lenderReturn = utils.round(percentageLent * moneyToReturn);
                addTransactionToCustomer(lenderName, lenderReturn, Transaction.TransactionType.DEPOSIT);
            }

            loan.getPayments().add(new Transaction(moneyToReturn,currentTime,Transaction.TransactionType.DEPOSIT,0,0));

            loan.setAmountPaidUntilNow(loan.getCompleteAmountToBePaid());


            loan.setStatus(Loan.LoanStatus.FINISHED);
            loan.setEndDate(currentTime);


        }

    }

    public void payCurrLoan(Loan loan) throws NotEnoughMoneyException, LoanProccessingException {


        boolean exitRisk = false;
        if(loan.isPaidThisYaz()){
            throw new LoanProccessingException("Customer already made the payment on this turn.",loan);
        }
        if(loan.getTimeNextPayment() > 1 && !loan.getStatus().equals(Loan.LoanStatus.RISK)){
            throw new LoanProccessingException("Current yaz is not the turn to pay the loan.",loan);
        }


        //the customer that needs to pay
        Customer c = findCustomerById(loan.getOwnerName());

        // money that needs to be paid
        double moneyToReturn = loan.getSinglePaymentTotal();
        moneyToReturn += loan.getUnpaidDebt();


        // if missed more payments than original length of time don't keep charging customer
        if(loan.getAmountPaidUntilNow() + loan.getUnpaidDebt() + loan.getSinglePaymentTotal() > loan.getCompleteAmountToBePaid()){
            moneyToReturn = loan.getCompleteAmountToBePaid() - loan.getAmountPaidUntilNow();
        }


        // if not customers turn to pay but he wants to exit risk mode
        if (loan.getStatus().equals(Loan.LoanStatus.RISK) && loan.getTimeNextPayment() > 1){
            moneyToReturn = loan.getUnpaidDebt();
            exitRisk = true;
        }

        if (c.getBalance() >= moneyToReturn){


            //set did pay this yaz to true
            loan.setPaidThisYaz(true);

            //set status to active in case it's in risk
            loan.setStatus(Loan.LoanStatus.ACTIVE);

            //Remove money for loaner's account with transaction
            addTransactionToCustomer(loan.getOwnerName(), (int) moneyToReturn, Transaction.TransactionType.WITHDRAW);

            //calculate the amount each lender should receive proportionally to what he gave
            Map<String, Double> lenderMap= loan.getLenderAmounts();
            for (int i = 0; i < loan.getLenders().size(); i++) {
                String lenderName = loan.getLenders().get(i).getId();
                double amountLent = lenderMap.get(lenderName);
                double percentageLent =  amountLent /  loan.getAmount();
                double lenderReturn = utils.round(percentageLent * moneyToReturn);
                addTransactionToCustomer(lenderName, lenderReturn, Transaction.TransactionType.DEPOSIT);
            }
            loan.getPayments().add(new Transaction(moneyToReturn,currentTime,Transaction.TransactionType.DEPOSIT,0,0));

            loan.setAmountPaidUntilNow(loan.getAmountPaidUntilNow() + moneyToReturn);

            if (exitRisk){
                loan.setUnpaidDebt(0);
            }
            if(loan.getAmountPaidUntilNow() == loan.getCompleteAmountToBePaid()){
                loan.setStatus(Loan.LoanStatus.FINISHED);
                loan.setEndDate(currentTime);
            }


        }
        else{
            throw new NotEnoughMoneyException("seems like you don't have enough money in your account",c.getBalance(),moneyToReturn);
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


        // if the amount entered is zero or less it ain't valid.. ignore it (:
        if(amount <= 0){
            return;
        }
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
