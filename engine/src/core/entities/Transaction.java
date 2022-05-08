package core.entities;

public class Transaction implements Comparable  {



    public enum TransactionType {WITHDRAW,DEPOSIT,GAVELOAN,RECIEVEDLOAN};

    double amount;
    int date;
    TransactionType type;
    double balanceBefore;

    public double getAmount() {
        return amount;
    }

    public TransactionType getType() {
        return type;
    }

    public double getBalanceBefore() {
        return balanceBefore;
    }

    public double getBalanceAfter() {
        return balanceAfter;
    }

    double balanceAfter;

    public Transaction(double amount, int date, TransactionType type, double balanceBefore, double balanceAfter) {
        this.amount = amount;
        this.date = date;
        this.type = type;
        this.balanceBefore = balanceBefore;
        this.balanceAfter = balanceAfter;
    }

    @Override
    public String toString() {
        return "amount: " + amount +
                ", date: " + date +
                ", type: " + type;
    }

    public int getDate() {
        return date;
    }

    @Override
    public int compareTo(Object o) {
        int compareData=((Transaction)o).getDate();
        /* For Ascending order*/
        return this.date-compareData;
    }
}
