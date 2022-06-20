package servlets.snapshot;

import com.google.gson.Gson;
import core.dtos.CustomerSnapshot;
import core.dtos.LoansDTO;
import core.dtos.TransactionsDTO;
import core.engine.ABSEngine;
import core.entities.Customer;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import utils.ServerUtils;

import java.io.IOException;
import java.util.ArrayList;

@WebServlet(name = "usersnapshot",urlPatterns = "/user/snapshot")
public class UserSnapshotServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        ABSEngine engine = ServerUtils.getEngine(getServletContext());
        String usernameFromSession = getUsername(req);
        if (usernameFromSession == null){
            usernameFromSession = req.getParameter("username");
        }
        Gson gson = new Gson();

        LoansDTO loansDTO;
        if(usernameFromSession == null){
            loansDTO = new LoansDTO(new ArrayList<>(),new ArrayList<>(),"",0.0);
        }else{
            Customer customer = engine.findCustomerById(usernameFromSession);

            loansDTO = new LoansDTO(customer);
        }

        CustomerSnapshot customerSnapshot = new CustomerSnapshot(loansDTO,
                new TransactionsDTO(loansDTO.balance,engine.findCustomerById(usernameFromSession).getTransactions()));
        String s = gson.toJson(customerSnapshot);

        resp.getWriter().println(s);
        resp.setStatus(200);
    }

    private static String getUsername (HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Object sessionAttribute = session != null ? session.getAttribute("username") : null;
        return sessionAttribute != null ? sessionAttribute.toString() : null;
    }
}
