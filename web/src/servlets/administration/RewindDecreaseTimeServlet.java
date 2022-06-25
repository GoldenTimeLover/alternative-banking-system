package servlets.administration;

import core.engine.ABSEngine;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServerUtils;

import java.io.IOException;

@WebServlet(name = "decreaseTime" , urlPatterns = "/admin/rewind/decrease")
public class RewindDecreaseTimeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


        ABSEngine engine = ServerUtils.getEngine(getServletContext());

        if(!engine.isRewind()){

            resp.setStatus(400);
            resp.getWriter().println("System must be in rewind mode to access this endpoint");
        }else{
            resp.setStatus(200);
            engine.rewindDecreaseTime();
            resp.getWriter().println("new sys time is " + engine.getCurrentTime());
        }
    }
}
