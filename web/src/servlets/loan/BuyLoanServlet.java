package servlets.loan;

import core.engine.ABSEngine;
import core.entities.Customer;
import core.entities.Loan;
import core.entities.Transaction;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServerUtils;

import java.io.IOException;


@WebServlet(name = "buyLoan",urlPatterns = "/loan/buy")
public class BuyLoanServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


        System.out.println("Arrived in buy loan servlet");
        String buyerName = req.getParameter("user");
        String sellerName = req.getParameter("seller");
        String loanId= req.getParameter("loanId");

        if(loanId == null || buyerName == null ||sellerName == null){
            resp.setStatus(400);
            resp.getWriter().println("Needed parameters not provided");
            return;
        }

        if(buyerName.equals(sellerName)){
            resp.getWriter().println("Cannot buy your own loan");
            resp.setStatus(400);
            return;
        }
        System.out.println("Parameters ok");

        ABSEngine engine = ServerUtils.getEngine(getServletContext());




        Loan loanById = engine.getLoanById(loanId);
        Customer newOwner = engine.findCustomerById(buyerName);

        Customer oldOwner = engine.findCustomerById(sellerName);


        //remove this part of loan from list of loans for sale
        for (int i = 0; i < engine.getLoansForSale().size(); i++) {
            if(engine.getLoansForSale().get(i).getId().equals(loanId)
                    && engine.getLoansForSale().get(i).getWhoSelling().equals(sellerName)){
                engine.getLoansForSale().remove(i);
                break;
            }
        }



        //remove old lender from loan's lender list
        for (int i = 0; i < loanById.getLenders().size(); i++) {
            if(oldOwner.getId().equals(loanById.getLenders().get(i).getId())){
                loanById.getLenders().remove(i);
                break;
            }
        }
        //remove loan from old lender's giving loans list
        for (int i = 0; i < oldOwner.getGivingLoans().size(); i++) {
            if(oldOwner.getGivingLoans().get(i).getId().equals(loanId)){
                oldOwner.getGivingLoans().remove(i);
                break;
            }
        }

        //add new owner to loans lenders
        loanById.getLenders().add(newOwner);
        //add loan to new owners giving list
        newOwner.getGivingLoans().add(loanById);

        // get amount he owned in loan
        Double aDouble = loanById.getLenderAmounts().get(oldOwner.getId());

        // remove him from dictionary of amounts
        loanById.getLenderAmounts().remove(oldOwner.getId());


        // add the person buying share of loan instead
        //if he is already one of the lenders add to his share else create new entry for him
        if(loanById.getLenderAmounts().containsKey(newOwner.getId())){
            loanById.getLenderAmounts().put(newOwner.getId(),loanById.getLenderAmounts().get(newOwner.getId()) + aDouble);
        }else{
            loanById.getLenderAmounts().put(newOwner.getId(),aDouble);
        }


        double completeAmountToBePaid = loanById.getCompleteAmountToBePaid();
        double amountPaidUntilNow = loanById.getAmountPaidUntilNow();

        double percentagePagePaidSoFar = 1 - (amountPaidUntilNow / completeAmountToBePaid);
        double amount =  aDouble * percentagePagePaidSoFar;

        engine.addTransactionToCustomer(newOwner.getId(),amount, Transaction.TransactionType.WITHDRAW);
        engine.addTransactionToCustomer(oldOwner.getId(),amount, Transaction.TransactionType.DEPOSIT);


        System.out.println("The part of the loan previously owned by"+ oldOwner.getId() +
                        " is not owned by " + newOwner.getId());


        resp.setStatus(200);
    }
}
