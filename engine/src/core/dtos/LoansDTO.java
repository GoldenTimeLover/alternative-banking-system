package core.dtos;

import java.util.List;

public class LoansDTO {

    public List<SingleLoanDTO> loanList;
    public List<SingleLoanDTO> loansCustomerGaveToOthers;
    public String userName = "tempName";
    public double balance = 0.0;

    public LoansDTO(List<SingleLoanDTO> loanList,List<SingleLoanDTO> loansCustomerGaveToOthers,String userName,double balance) {
        this.loanList = loanList;
        this.loansCustomerGaveToOthers = loansCustomerGaveToOthers;
        this.userName = userName;
        this.balance = balance;
    }
}
