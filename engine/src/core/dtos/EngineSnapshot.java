package core.dtos;

import core.entities.Customer;
import core.entities.Loan;
import core.entities.Notification;
import core.entities.Transaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EngineSnapshot {

    public List<Loan> loans;
    public List<Customer> customers;
    public List<String> categories;
    public Map<String,List<Notification>> notifications;
    public int currentTime = 1;

    public EngineSnapshot(List<Loan> loans, List<Customer> customers, List<String> categories, Map<String, List<Notification>> notifications, int currentTime) {



        this.loans = new ArrayList<>();

        Map<String,Loan> tempLenders = new HashMap<>();

        for (Loan l : loans) {
            Loan tempLoan = new Loan(l);

            tempLoan.setLenderAmounts(new HashMap<>(l.getLenderAmounts()));
            this.loans.add(tempLoan);
            tempLenders.put(l.getId(),tempLoan);
            
        }

        this.customers = new ArrayList<>();
        for (Customer c : customers){
            Customer temp = new Customer(c.getId(), (int) c.getBalance(),new ArrayList<>(),new ArrayList<>(),new ArrayList<>());
            temp.setAdmin(c.isAdmin());

            //add transactions
            for (Transaction t:c.getTransactions())
                temp.addTransaction(new Transaction(t.getAmount(),t.getDate(),t.getType(),t.getBalanceBefore(),t.getBalanceAfter()));


            //add lending loans to customers
            for (Loan l : c.getGivingLoans())
                temp.getGivingLoans().add(tempLenders.get(l.getId()));


            this.customers.add(temp);
        }

        connectDataLoadedFromFile();


        this.categories = new ArrayList<>();
        this.categories.addAll(categories);

        this.currentTime = currentTime;
        this.notifications = new HashMap<>();

        for (Map.Entry<String,List<Notification>> entry : notifications.entrySet()){
            this.notifications.put(entry.getKey(),new ArrayList<>());
            for (Notification n : notifications.get(entry.getKey())){
                this.notifications.get(entry.getKey()).add(new Notification(n.getTitle(),n.getDate(),n.getContent()));
            }
        }

    }


    private void connectDataLoadedFromFile(){
        for (Loan loan : loans) {
            Customer temp = null;
            for (Customer customer : customers) {
                if (customer.getId().equals(loan.getOwnerName())) {
                    temp = customer;
                }
            }
            loan.setBorrower(temp);

            assert temp != null;


            temp.addLoan(loan);

        }





    }
}
