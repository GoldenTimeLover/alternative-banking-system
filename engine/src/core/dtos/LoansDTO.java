package core.dtos;

import core.entities.Customer;
import core.entities.Loan;

import java.util.ArrayList;
import java.util.List;

public class LoansDTO {

    public List<SingleLoanDTO> loanList;
    public List<SingleLoanDTO> loansCustomerGaveToOthers;
    public List<String> categories;
    public String userName = "tempName";
    public double balance = 0.0;

    public LoansDTO(List<SingleLoanDTO> loanList,List<SingleLoanDTO> loansCustomerGaveToOthers,String userName,double balance) {
        this.loanList = loanList;
        this.loansCustomerGaveToOthers = loansCustomerGaveToOthers;
        this.userName = userName;
        this.balance = balance;
    }
    public LoansDTO(List<SingleLoanDTO> loanList,List<SingleLoanDTO> loansCustomerGaveToOthers,String userName,double balance,List<String> categories) {
        this.loanList = loanList;
        this.loansCustomerGaveToOthers = loansCustomerGaveToOthers;
        this.userName = userName;
        this.balance = balance;
        this.categories = categories;
    }

    //java gods forgive me for my sins
    public LoansDTO(Customer customer){

        loanList = new ArrayList<>();
        for (Loan l : customer.getTakingLoans()){
                loanList.add(new SingleLoanDTO(l));
        }

        loansCustomerGaveToOthers = new ArrayList<>();
        for (Loan l: customer.getGivingLoans()){
            loansCustomerGaveToOthers.add(new SingleLoanDTO(l));
        }
        userName = customer.getId();
        balance = customer.getBalance();
    }

    public LoansDTO() {

        loanList = new ArrayList<>();
        loansCustomerGaveToOthers = new ArrayList<>();
        userName = "";
        balance = 0.0;
    }
}
