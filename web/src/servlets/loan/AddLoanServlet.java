package servlets.loan;

import com.google.gson.Gson;
import core.dtos.*;
import core.engine.ABSEngine;
import core.engine.Engine;
import core.entities.Customer;
import core.entities.Loan;
import core.entities.Transaction;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServerUtils;
import utils.dtos.AbsLoan;
import utils.dtos.AbsLoans;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


@WebServlet(name = "AddLoanServlet" , urlPatterns = "/loan/add")
public class AddLoanServlet extends HttpServlet {


    @Override protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {




        String loanData = req.getParameter("loanData");
        String user = req.getParameter("user");

        Gson g = new Gson();


        LoansDTO loansDTO = g.fromJson(loanData, LoansDTO.class);

        ABSEngine engine = ServerUtils.getEngine(getServletContext());


        List<SingleLoanDTO> singleLoanDTOS = new ArrayList<>();

        for (SingleLoanDTO l: loansDTO.loanList) {

            if(engine.getLoanById(l.id) != null)
                continue;


            singleLoanDTOS.add(l);

        }

        Gson gson = new Gson();
        List<Loan> loanList = new ArrayList<>();
        for (SingleLoanDTO absloan : singleLoanDTOS){

            if(engine.getLoanById(absloan.id) != null)
                continue;
            Loan l = new Loan(absloan.getId(),1,absloan.getAbsCapital(),
                    engine.findCustomerById(user), new ArrayList<>(),
                    Loan.LoanStatus.NEW,absloan.getAbsCategory(),
                    absloan.getAbsIntristPerPayment(),
                    user,absloan.getAbsTotalYazTime(),absloan.getAbsPaysEveryYaz());

            System.out.println(absloan.id);

            engine.getLoans().add(l);
            loanList.add(l);

            if(!engine.getCategories().contains(absloan.getAbsCategory())){
                engine.getCategories().add(absloan.getAbsCategory());
            }

        }

        engine.addOwnerToLoanInEngine(loanList);


        LoansDTO outputData = new LoansDTO(new ArrayList<>(),new ArrayList<>(),
                user,engine.findCustomerById(user).getBalance());

        for (Loan l: engine.getLoans()){

            Customer customer = engine.findCustomerById(user);

            if(l.getBorrower().getId().equals(user)){
                outputData.loanList.add(new SingleLoanDTO(l));
            }
            if(l.getLenders().contains(customer)){
                outputData.loansCustomerGaveToOthers.add(new SingleLoanDTO(l));
            }
        }

        TransactionsDTO transactionsDTO = new TransactionsDTO(engine.findCustomerById (user).getBalance(),new ArrayList<>());
        transactionsDTO.transactions.addAll(engine.findCustomerById(user).getTransactions());

        NotificationDTO notificationDTO = new NotificationDTO(engine.getNotifications().get(user));

        CustomerSnapshot customerSnapshot =
                new CustomerSnapshot(outputData,
                transactionsDTO,
                engine.getCurrentTime(),
                notificationDTO);

        String loanInfo = gson.toJson(customerSnapshot,CustomerSnapshot.class);
        resp.getWriter().println(loanInfo);
        resp.setStatus(200);


    }
}
