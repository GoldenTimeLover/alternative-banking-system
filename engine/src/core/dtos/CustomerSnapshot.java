package core.dtos;

public class CustomerSnapshot {

    public LoansDTO loansDTO;
    public TransactionsDTO transactionsDTO;

    public CustomerSnapshot(LoansDTO loansDTO, TransactionsDTO transactionsDTO) {
        this.loansDTO = loansDTO;
        this.transactionsDTO = transactionsDTO;
    }
}
