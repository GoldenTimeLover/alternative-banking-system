package servlets.loan;

import com.google.gson.Gson;
import core.engine.ABSEngine;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServerUtils;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "categories" , urlPatterns = "/loan/categories")
public class GetCategoriesServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


        ABSEngine engine = ServerUtils.getEngine(getServletContext());

        List<String> categories = engine.getCategories();

        Gson gson = new Gson();
        resp.getWriter().println(gson.toJson(categories));
        System.out.println("Categories in system are");
        for (String cat :
                categories) {
            System.out.println(cat);

        }
        resp.setStatus(200);
    }
}
