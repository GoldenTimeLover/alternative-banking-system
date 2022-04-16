package ui.console;

import core.Exceptions.FileFormatException;
import core.engine.ABSEngine;
import core.entities.Customer;
import core.entities.Loan;
import core.entities.Transaction;
import core.utils.utils;

import java.util.*;

import static ui.console.InputGetters.*;
import static ui.validator.Validators.*;

public class ConsoleInterface {

    private static ABSEngine engine;

    public static void main(String[] args) {
        engine = new ABSEngine();
        mainLoop();
    }


    public static void mainLoop(){
        String userChoice = "0";
        Scanner in = new Scanner(System.in);
        boolean dataLoaded = false;

        System.out.println("Hello Welcome To A.B.S - Alternative Banking System!");
        while (!userChoice.equals("8")){
            printMenu(engine.getCurrentTime(),dataLoaded);

            userChoice = in.nextLine();
            boolean validChoice = validateMainMenuInput(userChoice);
            if (validChoice){
                if (userChoice.equals("1")){
                    String filePath = getFilePath();

                    if (!filePath.equals("")) {
                        try {
                            engine.loadDataFromFile(filePath);
                        } catch (FileFormatException e) {
                            System.out.println(e.getMessage());
                        }
                    }

                }
                else if(userChoice.equals("3") && dataLoaded){
                    printAllCustomers();
                }
                else if(userChoice.equals("2") && dataLoaded){
                    printAllLoans();
                }
                else if(userChoice.equals("4") && dataLoaded){
                    System.out.println("Please Enter Amount of money you would like to deposit:");
                    String usrAmt = in.nextLine();
                    if (validateNumericValue(usrAmt)){
                        insertMoneyToCustomer(Integer.parseInt(usrAmt));

                    }else{
                        System.out.println("Please Enter A number For the Amount of money.");
                    }
                }
                else if(userChoice.equals("5") && dataLoaded){
                    System.out.println("Please Enter Amount of money you would like to withdraw:");
                    String usrAmt = in.nextLine();
                    if (validateNumericValue(usrAmt)){

                        withdrawMoneyToCustomer(Integer.parseInt(usrAmt));
                    }else{
                        System.out.println("Please Enter A number For the Amount of money.");
                    }
                }else if(userChoice.equals("6") && dataLoaded){
                    List<Customer> cls = getCustomerListFromEngine();
                    List<String> categories = getCategoriesFromEngine();
                    System.out.println("Select Customer you would like to match for:\n");
                    for (int i = 0; i < cls.size(); i++) {
                        System.out.println((i+1) + ". " + cls.get(i).getId() + " Balance:" + cls.get(i).getBalance());
                    }
                    String cusChoice = in.nextLine();
                    if (validateNumericValue(cusChoice)){
                        int cusIntChoice = Integer.parseInt(cusChoice);
                        if (cusIntChoice > 0 && cusIntChoice <= cls.size()){
                            double loanAmt = getLoanAmount(cls.get(cusIntChoice - 1).getBalance());
                            List<String> catList = getLoanCategories(categories);
                            double loanMinInterest = getMinInterest();
                            int loanMinYaz = getMinYaz();
                            processLoanMatchRequest(cls.get(cusIntChoice -1).getId(),catList,loanAmt,loanMinInterest,loanMinYaz);
                        }else{
                            System.out.println("Please Enter a valid number from the List\n");
                        }
                    }else{
                        System.out.println("Please Enter a valid number from the List\n");
                    }


                }else if(userChoice.equals("7") && dataLoaded){
                    System.out.println("Moving Time Forward by 1 unit... from " + engine.getCurrentTime() + "-> " + (engine.getCurrentTime() +1) );
                    engine.moveTimeForward();
                }
                else{
                    System.out.println("You must load data before using other functionality!");
                    System.out.println("Choose option 1 to load data\n");
                }
            }else{
                System.out.println("Please Enter a Valid Option (1,2,3,4,5,6,7,8)");
            }
            dataLoaded = engine.isDataLoaded();
        }
    }

    public static void printMenu(int currentTime,boolean loaded){

        System.out.println("\n|Current YAZ time is: " + (loaded ? currentTime : "Uninitialized") + "|");
        System.out.println("Please Choose one of the following options:");
        System.out.println("1.Load Data From File");
        System.out.println("2.Display Loan Info");
        System.out.println("3.Display Customer Info");
        System.out.println("4.Insert Money Into Account");
        System.out.println("5.Withdraw Money Out Of Account");
        System.out.println("6.Match Loan");
        System.out.println("7.Move Timeline Forward");
        System.out.println("8.Exit System");
    }
    public static List<Customer> getCustomerListFromEngine(){
        return engine.getCustomers();
    }

    public static List<String> getCategoriesFromEngine(){
        return engine.getCategories();
    }

    public static void processLoanMatchRequest(String customerID, List<String> categories, double amount, double interest, int time){
        Scanner in = new Scanner(System.in);
        String[] splitted = new String[0];
        System.out.println("Processing LOAN Match request:" + "FROM: " + customerID +
                "FILTERS:\n" +" Loan categories: " + categories +
                "Loan amount: " + amount + "loan interest" + interest + "loan Time: " + time);
        List<Loan> possibleLoans = engine.findPossibleLoanMatches(customerID,categories,amount,interest,time);

        if (possibleLoans.size() == 0){
            System.out.println("Sorry no available Loans that match filters available.\n");
            return;
        }
        String userChoice = "INVALID";
        while(userChoice.equals("INVALID")) {
            System.out.println("Select possible loans (Enter space seperated numbers to select more than one loan)" +
                    "Example: 1 2 5");
            for (int i = 0; i < possibleLoans.size(); i++) {
                System.out.println(i+"."+ possibleLoans.get(i).stringForConsole() + "\n");
            }
            userChoice = in.nextLine();
            splitted = userChoice.split("\\s+");
            for (int i = 0; i < splitted.length; i++) {
                if (!validateNumericValue(splitted[i])){
                    userChoice = "INVALID";
                    System.out.println("Please enter number from the list seperated by a space!");
                }
            }
        }


        if(!(checkOutOfArrayBounds(splitted,possibleLoans.size()) && checkNoDuplicates(splitted))){
            return;
        }

        double amountForEach = utils.round(amount / splitted.length);
        for (int i = 0; i < splitted.length; i++) {
            int index = Integer.parseInt(splitted[i]);
            Loan loan = possibleLoans.get(index);
            double amountGiven = Math.min(loan.getRemainingAmount(),amountForEach);
            engine.matchLoan(loan.getId(), amountGiven, customerID);
            System.out.println("Matched " + amountGiven + "$ To loan '" + loan.getId() +"'");

        }

    }




    public static void printAllCustomers(){
        List<Customer> ls = engine.getCustomers();
        for (Customer l : ls) {
            System.out.println("***********************************");
            System.out.println(l.stringForConsole());
            System.out.println("***********************************");
        }
    }

    public static void printAllLoans(){
        List<Loan> ls = engine.getLoans();
        for (Loan l : ls) {
            System.out.println("###################################");
            System.out.println(l.stringForConsole());
            System.out.println("###################################");
        }
    }


    /**
     *Function takes amount of money, It then asks the user to choose a
     * Customer from a list and adds the amount of money to the selected customer
     * @param amount amount of money to give customer
     * @return True if success False if Failed
     */
    public static boolean insertMoneyToCustomer(int amount) {

        System.out.println("Please Select A customer to give money to:");
        List<Customer> customers = engine.getCustomers();
        printCustomersMinimal();

        Scanner in = new Scanner(System.in);
        String userInput = in.nextLine();

        if (validateNumericValue(userInput)) {
            int res = Integer.parseInt(userInput);
            if (res > 0 && res <= customers.size()) {
                String cName = customers.get(res - 1).getId();
                engine.addTransactionToCustomer(cName,amount, Transaction.TransactionType.DEPOSIT);
                System.out.println("Deposited " + amount + " To " + cName+"'s account.");
                return true;
            }
        }
        System.out.println("Please Enter a valid int from the list of customers.");
        return false;
    }

    /**
     *Function takes amount of money, It then asks the user to choose a
     * Customer from a list and removes the amount of money to the selected customer
     * if the user doesn't have enough money action fails
     * @param amount amount of money to remove from customer
     * @return True if success False if Failed
     */
    public static boolean withdrawMoneyToCustomer(int amount) {
        System.out.println("Please Select A customer to withdraw from:");
        List<Customer> ls = engine.getCustomers();
        printCustomersMinimal();
        Scanner in = new Scanner(System.in);
        String userInput = in.nextLine();

        if (validateNumericValue(userInput)) {
            int res = Integer.parseInt(userInput);
            if (res > 0 && res <= ls.size()) {
                if (ls.get(res - 1).getBalance() < amount){
                    System.out.println("Sorry. the Customer's Balance is lower than the amount requested");
                    System.out.println("Request Denied!");
                    return false;
                }
                else {
                    String cName = ls.get(res - 1).getId();
                    engine.addTransactionToCustomer(cName,amount, Transaction.TransactionType.WITHDRAW);
                    System.out.println("Withdrew " + amount + " From " + cName+"'s account.");
                    return true;
                }
            }
        }
        System.out.println("Please Enter a valid int from the list of customers.");
        return false;
    }


    public static void printCustomersMinimal(){
        List<Customer> ls = engine.getCustomers();
        for (int i = 0; i < ls.size(); i++) {

            System.out.printf("%d. " + ls.get(i).getId() + "%n", i+1);

        }
    }
}
