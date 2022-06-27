package servlets.loan;


import core.dtos.AdminLoanDTO;
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



        String user = req.getParameter("user");
        String loanId= req.getParameter("loanId");

        if(loanId == null || user == null){
            resp.setStatus(400);
            resp.getWriter().println("Needed parameters not provided");
            return;
        }


        ABSEngine engine = ServerUtils.getEngine(getServletContext());

        Loan ln = engine.getLoanById(loanId);
        if(ln == null ){
            resp.setStatus(400);
            resp.getWriter().println("No such loan in system");
            return;
        }
        if(!ln.getStatus().equals(Loan.LoanStatus.ACTIVE)){
            resp.setStatus(400);
            resp.getWriter().println("Can only sell loans that are ACTIVE");
            return;

        }


        AdminLoanDTO loan = new AdminLoanDTO(ln);
        loan.setWhoSelling(user);

        boolean wasForSale = false;
        for (AdminLoanDTO l : engine.getLoansForSale()){
            if(l.getId().equals(loanId)){
                engine.getLoansForSale().remove(l);
                wasForSale = true;
                break;
            }
        }

        if(!wasForSale){
        engine.getLoansForSale().add(loan);
        resp.setStatus(200);
        resp.getWriter().println("Added loan '" + loanId +"' to loans for sale");
        }
        else{
            resp.setStatus(200);
            resp.getWriter().println("Removed loan '" + loanId +"' from loans for sale");
        }
    }
}
