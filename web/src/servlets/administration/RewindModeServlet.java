package servlets.administration;

import core.engine.ABSEngine;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServerUtils;

import java.io.IOException;


@WebServlet(name = "adminrewindtoggle" , urlPatterns = "/admin/rewind/toggle")
public class RewindModeServlet extends HttpServlet {


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        ABSEngine engine = ServerUtils.getEngine(getServletContext());
        System.out.println("Setting rewind to " + !engine.isRewind());
        if(engine.isRewind()){
            engine.exitRewindMode();
        }else{
            engine.enterRewindMode();
        }
    }
}
