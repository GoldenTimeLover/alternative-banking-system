package servlets.loan;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;


@WebServlet(name = "buyLoan",urlPatterns = "/loan/buy")
public class BuyLoanServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String user = req.getParameter("user");
        String loanId= req.getParameter("loanId");

        if(loanId == null || user == null){
            resp.setStatus(400);
            resp.getWriter().println("Needed parameters not provided");
            return;
        }

        resp.setStatus(200);
        resp.getWriter().println("Trying to buy loan " + loanId);
    }
}
