package servlets.loan;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import core.dtos.LoansDTO;
import core.dtos.SingleLoanDTO;
import core.engine.ABSEngine;
import core.entities.Loan;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServerUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "FilterLoansServlet" , urlPatterns = "/loan/filter")
public class FilterLoansServlet extends HttpServlet{
    @Override protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {


        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = jsonParser.parse(req.getParameter("filters")).getAsJsonObject();

        Gson gson = new Gson();

        List<String> categories = gson.fromJson(req.getParameter("categories"),new TypeToken<List<String>>(){}.getType());
        String customerID = jsonObject.get("customerId").getAsString();
        double amount = jsonObject.get("amount").getAsDouble();
        double interest = jsonObject.get("interest").getAsDouble();
        int time = jsonObject.get("time").getAsInt();
        int amountOfOpenLoans = jsonObject.get("amountOpen").getAsInt();
        int maxPercentage = jsonObject.get("maxPercent").getAsInt();


        ABSEngine engine = ServerUtils.getEngine(getServletContext());

        List<Loan> filteredLoans = engine.findPossibleLoanMatches(customerID,categories,
                amount,interest,time,
                amountOfOpenLoans,
                maxPercentage);


        LoansDTO loansDTO = new LoansDTO(new ArrayList<>(),null,"",0.0);
        for (Loan l : filteredLoans){
            loansDTO.loanList.add(new SingleLoanDTO(l));
        }
        resp.getWriter().println(gson.toJson(loansDTO));
        resp.setStatus(200);


    }
}




