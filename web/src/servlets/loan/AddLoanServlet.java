package servlets.loan;

import com.google.gson.Gson;
import core.Exceptions.FileFormatException;
import core.dtos.*;
import core.engine.ABSEngine;
import core.entities.Customer;
import core.entities.Loan;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServerUtils;


import java.io.IOException;

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

        // verify information
        for (SingleLoanDTO l: loansDTO.loanList) {

            try {
                engine.checkLoanTimes(new Loan(l,user));
            } catch (FileFormatException e) {
                resp.setStatus(400);
                resp.getWriter().println("Loan interest and time don't match up");
                return;
            }
            if(engine.getLoanById(l.getId()) != null){
                resp.setStatus(400);
                resp.getWriter().println("Loan with name " + l.getId() +" already exists.");
                return;
            }

            if (loansDTO.categories != null && !loansDTO.categories.contains(l.absCategory)){
                resp.setStatus(400);
                resp.getWriter().println("Loan with name " + l.getId() +" categories not matching.");
                return;
            }
            singleLoanDTOS.add(l);

        }



        Gson gson = new Gson();
        List<Loan> loanList = new ArrayList<>();
        for (SingleLoanDTO absloan : singleLoanDTOS){

            Loan l = new Loan(absloan.getId(),1,absloan.getAbsCapital(),
                    engine.findCustomerById(user), new ArrayList<>(),
                    Loan.LoanStatus.NEW,absloan.getAbsCategory(),
                    absloan.getAbsIntristPerPayment(),
                    user,absloan.getAbsTotalYazTime(),absloan.getAbsPaysEveryYaz());


            engine.getLoans().add(l);
            loanList.add(l);

            if(!engine.getCategories().contains(absloan.getAbsCategory())){
                engine.getCategories().add(absloan.getAbsCategory());
            }

        }

        engine.addOwnerToLoanInEngine(loanList);
        resp.getWriter().println("Loans" + loanList + " have been successfully added to system.");
        resp.setStatus(200);


    }
}
