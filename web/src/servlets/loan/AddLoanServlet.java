package servlets.loan;

import com.google.gson.Gson;
import core.dtos.LoansDTO;
import core.dtos.SingleLoanDTO;
import core.engine.ABSEngine;
import core.engine.Engine;
import core.entities.Customer;
import core.entities.Loan;
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


        System.out.println("hello");


        String loanData = req.getParameter("loanData");
        String user = req.getParameter("user");
        System.out.println(loanData);
        Gson g = new Gson();


        LoansDTO loansDTO = g.fromJson(loanData, LoansDTO.class);

        ABSEngine engine = ServerUtils.getEngine(getServletContext());





        Gson gson = new Gson();

        List<Loan> res = new ArrayList<>();
        for (SingleLoanDTO absloan : loansDTO.loanList){

            engine.getLoans().add(new Loan(absloan.getId(),1,absloan.getAbsCapital(),
                    engine.findCustomerById(user), new ArrayList<>(),
                    Loan.LoanStatus.NEW,absloan.getAbsCategory(),
                    absloan.getAbsIntristPerPayment(),
                    user,absloan.getAbsTotalYazTime(),absloan.getAbsPaysEveryYaz()));


            res.add(new Loan(absloan.getId(),1,absloan.getAbsCapital(),
                    engine.findCustomerById(user), new ArrayList<>(),
                    Loan.LoanStatus.NEW,absloan.getAbsCategory(),
                    absloan.getAbsIntristPerPayment(),
                    user,absloan.getAbsTotalYazTime(),absloan.getAbsPaysEveryYaz()));
        }



        LoansDTO outputData = new LoansDTO(new ArrayList<>(),new ArrayList<>(),user,engine.findCustomerById(user).getBalance());
        for (Loan l: engine.getLoans()){
            Customer customer = engine.findCustomerById(user);

            if(l.getBorrower().getId().equals(user)){
                outputData.loanList.add(new SingleLoanDTO(l));
            }
            if(l.getLenders().contains(customer)){
                outputData.loansCustomerGaveToOthers.add(new SingleLoanDTO(l));
            }

        }

        String loanInfo = gson.toJson(outputData,LoansDTO.class);

        resp.getWriter().println(loanInfo);
        resp.setStatus(200);


    }
}
