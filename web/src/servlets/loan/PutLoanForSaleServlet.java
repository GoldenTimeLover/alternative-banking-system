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

@WebServlet(name = "putLoanForSaleOrNot",urlPatterns = "/loan/buy/toggle")
public class PutLoanForSaleServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


        System.out.println("did you come here?");
        String user = req.getParameter("user");
        String loanId= req.getParameter("loanId");

        if(loanId == null || user == null){
            resp.setStatus(400);
            resp.getWriter().println("Needed parameters not provided");
            return;
        }
        ABSEngine engine = ServerUtils.getEngine(getServletContext());

        Loan loan = engine.getLoanById(loanId);
        if(loan == null){
            resp.setStatus(400);
            resp.getWriter().println("No such loan in system");
            return;
        }

        boolean wasForSale = false;
        for (Loan l : engine.getLoansForSale()){
            if(l.getId().equals(loanId)){
                engine.getLoansForSale().remove(l);
                wasForSale = true;
                break;
            }
        }
        if(!wasForSale){
        engine.getLoansForSale().add(loan);
        resp.setStatus(200);
        resp.getWriter().println("Added loan to loans for sale");
        }
        else{
            resp.setStatus(200);
            resp.getWriter().println("Loan was removed");
        }
    }
}
