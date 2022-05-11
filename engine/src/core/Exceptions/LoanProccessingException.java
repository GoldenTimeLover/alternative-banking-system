package core.Exceptions;

import core.entities.Loan;

public class LoanProccessingException extends Exception{

    private Loan loan;

    public LoanProccessingException(String message ,Loan loan) {
        super(message);
        this.loan = loan;
    }
}
