package core.dtos;

import core.entities.Transaction;

import java.util.List;

public class TransactionsDTO {

    public double balance = 0.0;
    public List<Transaction> transactions;

    public TransactionsDTO(double balance, List<Transaction> transactions) {
        this.balance = balance;
        this.transactions = transactions;
    }
}
