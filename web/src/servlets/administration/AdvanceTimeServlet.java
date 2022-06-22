package servlets.administration;

import core.engine.ABSEngine;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServerUtils;

import java.io.IOException;


@WebServlet(name = "AdvanceTime" , urlPatterns = "/admin/time/advance")
public class AdvanceTimeServlet extends HttpServlet {


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


        ABSEngine engine = ServerUtils.getEngine(getServletContext());
        engine.moveTimeForward();
        System.out.println("Beep Boop advancing time by one unit");
        resp.setStatus(200);
    }
}
