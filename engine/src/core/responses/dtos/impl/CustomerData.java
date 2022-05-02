package core.responses.dtos.impl;


import core.entities.Transaction;
import core.responses.dtos.DataTransferObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CustomerData implements DataTransferObject {

    public String id;
    public double balance;
    public List<TransactionData> transactionDataList;
    private ArrayList<LoanData> givingLoans;
    private ArrayList<LoanData> takingLoans;


    @Override
    public String toString() {
        return "Customer ID/Name: " + id + "\n" +
                "Current Account Balance: " + balance + "\n";
    }


}
