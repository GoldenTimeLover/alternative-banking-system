package core.engine;

import core.Exceptions.FileFormatException;
import core.entities.Customer;
import core.entities.Loan;
import core.entities.Transaction;

import java.util.List;

public interface Engine {

    public int getCurrentTime();
    public void addTransactionToCustomer(String customerName, double amount, Transaction.TransactionType type);
    public List<Loan> getActiveLoans();
    public void moveTimeForward();
    public void matchLoan(String loanId,double amountOfMoney,String lenderId);
    public List<Loan> findPossibleLoanMatches(String customerID,List<String> categoryFilters,double amount,double interest,int time);
    public boolean isDataLoaded();
    public void checkLoanTimes(Loan loan) throws FileFormatException;
    public void loadDataFromFile(String filePath) throws FileFormatException;
    public List<Customer> getCustomers();
    public List<Loan> getLoans();
    public List<String> getCategories();
    public Customer findCustomerById(String id);
}
