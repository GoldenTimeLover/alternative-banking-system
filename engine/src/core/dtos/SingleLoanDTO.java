package core.dtos;

import core.entities.Loan;
import core.entities.Transaction;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;

public class SingleLoanDTO {

    public String absCategory;
    public int absCapital;
    public int absTotalYazTime;
    public int absPaysEveryYaz;
    public int absIntristPerPayment;
    public String id;
    private List<String> lenders;
    public Loan.LoanStatus status;
    public String owenerName;
    double unpaidDebt;
    double paymentPerYaz ;
    double singlePayment;
    double singlePaymentTotal ;
    double completeAmountToBePaid;
    double amountPaidUntilNow;
    List<Transaction> payments;
    boolean paidThisYaz = false;

    public SingleLoanDTO(String absCategory, int absCapital, int absTotalYazTime, int absPaysEveryYaz, int absIntristPerPayment, String id,Loan.LoanStatus status) {
        this.absCategory = absCategory;
        this.absCapital = absCapital;
        this.absTotalYazTime = absTotalYazTime;
        this.absPaysEveryYaz = absPaysEveryYaz;
        this.absIntristPerPayment = absIntristPerPayment;
        this.id = id;
        this.status = status;
    }
    public SingleLoanDTO(Loan l){
        this.absCategory = l.getCategory();
        this.absCapital = (int) l.getAmount();
        this.absTotalYazTime = l.getLengthOfTime();
        this.absPaysEveryYaz = l.getTimeBetweenPayments();
        this.absIntristPerPayment = l.getInterestRate();
        this.id = l.getId();
        this.owenerName = l.getOwnerName();
        this.status = l.getStatus();
    }

    public String getAbsCategory() {
        return absCategory;
    }

    public int getAbsCapital() {
        return absCapital;
    }

    public int getAbsTotalYazTime() {
        return absTotalYazTime;
    }

    public int getAbsPaysEveryYaz() {
        return absPaysEveryYaz;
    }

    public int getAbsIntristPerPayment() {
        return absIntristPerPayment;
    }

    public String getId() {
        return id;
    }
}
