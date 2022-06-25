package servlets.snapshot;


import com.google.gson.Gson;
import core.dtos.*;
import core.engine.ABSEngine;
import core.entities.Customer;
import core.entities.Loan;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServerUtils;

import java.io.IOException;
import java.util.ArrayList;

@WebServlet(name = "adminSnapshot",urlPatterns = "/admin/snapshot")
public class AdminSnapshotServlet extends HttpServlet{

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        ABSEngine engine = ServerUtils.getEngine(getServletContext());

        String usernameFromSession = req.getParameter("username");

        Gson gson = new Gson();

        AdminSnapshot adminSnapshot = new AdminSnapshot(
                new ArrayList<>(),
                usernameFromSession,engine.getCurrentTime(),
                new ArrayList<>(),
                engine.isRewind());
        for (Customer c : engine.getCustomers()) {
            if (!c.isAdmin()){
                adminSnapshot.customerSnapshotList.add(new CustomerSnapshot(new LoansDTO(c),
                        new TransactionsDTO(c.getBalance(),c.getTransactions()), engine.getCurrentTime(),
                        new NotificationDTO(engine.getNotifications().get(c.getId())),engine.isRewind(),engine.getLoansForSale()));
            }
        }

        for (Loan l: engine.getLoans()){
            adminSnapshot.loanList.add(new AdminLoanDTO(l));
        }



        String s = gson.toJson(adminSnapshot,AdminSnapshot.class);
        resp.getWriter().println(s);
        resp.setStatus(200);
    }
}
