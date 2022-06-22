package core.dtos;

import core.entities.Customer;
import core.entities.Loan;
import core.entities.Notification;

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
        for (Loan l : loans) {
            this.loans.add(new Loan(l.getId(),l.getStartDate(), (int) l.getAmount(),null,new ArrayList<>(),l.getStatus(),l.getCategory(),
                    l.getInterestRate(),l.getOwnerName(),l.getLengthOfTime(),l.getTimeBetweenPayments()));
        }

        this.customers = new ArrayList<>();
        for (Customer c : customers){
            this.customers.add(new Customer(c.getId(), (int) c.getBalance(),new ArrayList<>(),new ArrayList<>(),new ArrayList<>()));
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
