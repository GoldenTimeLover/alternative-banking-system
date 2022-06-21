package servlets.loan;

import core.Exceptions.LoanProccessingException;
import core.Exceptions.NotEnoughMoneyException;
import core.engine.ABSEngine;
import core.entities.Loan;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServerUtils;

import java.io.IOException;

@WebServlet(name = "paybackLoanServlet" , urlPatterns = "/loan/payback")
public class PayBackLoanServlet extends HttpServlet{

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

            ABSEngine engine = ServerUtils.getEngine(getServletContext());
            String type = req.getParameter("type");
            String user = req.getParameter("user");
            String loanId= req.getParameter("loanId");


            if(loanId == null || user == null || type == null){
                resp.setStatus(400);
                return;
            }
            Loan loanToPay = engine.getLoanById(loanId);
        switch (type) {
            case "payCurr":

                try {
                    engine.payCurrLoan(loanToPay);
                    resp.setStatus(200);
                    resp.getWriter().println("Success");
                } catch (NotEnoughMoneyException e) {

                    resp.getWriter().println("You are trying to make a payment on the loan '" +
                            loanToPay.getId() + " '" +
                            " but the amount required is " +
                            e.amountTryingToExtract + "$ and you have only " +
                            e.balance + "$ in your account.");


                } catch (LoanProccessingException e) {
                    resp.getWriter().println("Warning - Can't pay loan now");
                } catch (Exception e) {
                    e.printStackTrace();
                }


                break;
            case "payRisk":
                int amount = Integer.parseInt(req.getParameter("amount"));
                engine.payLoanDebtAmount(loanToPay, amount);
                resp.setStatus(200);
                resp.getWriter().println("Success");

                break;
            case "payEntire":
                engine.payEntireLoan(loanToPay);
                resp.setStatus(200);
                resp.getWriter().println("Success");

                break;
            default:
                resp.setStatus(400);
                resp.getWriter().println("Something went wrong");
                break;
        }


    }

}
