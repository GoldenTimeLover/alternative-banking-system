package core.Exceptions;

public class NotEnoughMoneyException extends  Exception{
    public double balance;
    public double amountTryingToExtract;

    public NotEnoughMoneyException(String message,double balance, double amountTryingToExtract){
        super(message);
        this.balance = balance;
        this.amountTryingToExtract = amountTryingToExtract;
    }
}