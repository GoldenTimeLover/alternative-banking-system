package core.dtos;

public class CustomerSnapshot {

    public LoansDTO loansDTO;
    public TransactionsDTO transactionsDTO;
    public int currentYaz = 1;
    public NotificationDTO notificationDTO;


    public CustomerSnapshot(LoansDTO loansDTO, TransactionsDTO transactionsDTO,int currentYaz,NotificationDTO notificationDTO) {
        this.loansDTO = loansDTO;
        this.transactionsDTO = transactionsDTO;
        this.currentYaz = currentYaz;
        this.notificationDTO =notificationDTO;
    }
}
