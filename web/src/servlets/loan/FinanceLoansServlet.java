package servlets.loan;

import core.engine.ABSEngine;
import core.entities.Loan;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServerUtils;

import java.io.IOException;


@WebServlet(name = "financeLoans" , urlPatterns = "/loan/finance")
public class FinanceLoansServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


        String loanId = req.getParameter("loanId");
        double amount = Double.parseDouble(req.getParameter("amount"));
        String loanOwnerName = req.getParameter("loanOwner");
        String lenderName = req.getParameter("lenderName");
        int maxPercentage = Integer.parseInt(req.getParameter("maxPercentage"));
        ABSEngine engine = ServerUtils.getEngine(getServletContext());

        Loan loan = engine.getLoanById(loanId);
        double amountGiven = Math.min(loan.getRemainingAmount(),amount);
        double finalAmountLoaned = engine.matchLoan(loan.getId(), amountGiven, lenderName,maxPercentage);


        resp.getWriter().println("Loan to loan " + loan.getId() + " " + finalAmountLoaned);
        resp.setStatus(200);

    }
}
