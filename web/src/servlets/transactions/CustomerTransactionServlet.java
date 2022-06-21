package servlets.transactions;


import com.google.gson.Gson;
import core.dtos.TransactionsDTO;
import core.engine.ABSEngine;
import core.entities.Customer;
import core.entities.Transaction;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServerUtils;

import java.io.IOException;

@WebServlet(name = "userTransactionServlet" , urlPatterns = "/user/transaction")
public class CustomerTransactionServlet extends HttpServlet {


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String user = req.getParameter("user");
        String type = req.getParameter("type");
        String amountStr = req.getParameter("amount");

        double amount = Double.parseDouble(amountStr);

        ABSEngine engine = ServerUtils.getEngine(getServletContext());

        Customer customer = engine.findCustomerById(user);
        if(type.equals("deposit")){
            engine.addTransactionToCustomer(user,amount, Transaction.TransactionType.DEPOSIT);

        }else{
            if(amount > customer.getBalance()){
                resp.setStatus(400);
                resp.getWriter().println("Sorry the customer doesn't have enough in the account!");
                return;
            }
            engine.addTransactionToCustomer(user,amount, Transaction.TransactionType.WITHDRAW);
        }

        resp.setStatus(200);
        Gson gson = new Gson();
        TransactionsDTO res = new TransactionsDTO(customer.getBalance(),customer.getTransactions());
        String output = gson.toJson(res);
        resp.getWriter().println(output);

    }
}
