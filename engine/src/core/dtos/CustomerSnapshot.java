package core.dtos;

import core.entities.Loan;

import java.util.List;

public class CustomerSnapshot {

    public LoansDTO loansDTO;
    public TransactionsDTO transactionsDTO;
    public int currentYaz = 1;
    public NotificationDTO notificationDTO;
    public boolean isRewindMode;
    public List<AdminLoanDTO> forSale;


    public CustomerSnapshot(LoansDTO loansDTO, TransactionsDTO transactionsDTO,int currentYaz,NotificationDTO notificationDTO,boolean isRewindMode,List<AdminLoanDTO>  forSale) {
        this.loansDTO = loansDTO;
        this.transactionsDTO = transactionsDTO;
        this.currentYaz = currentYaz;
        this.notificationDTO =notificationDTO;
        this.isRewindMode = isRewindMode;
        this.forSale = forSale;
    }
}
