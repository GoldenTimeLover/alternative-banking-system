package servlets.login;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

//import managers.UserManager;
//import utils.ServletUtils;

import java.io.IOException;

@WebServlet(name = "LoginServlet" , urlPatterns = "/user/login")
public class LoginServlet extends HttpServlet {

    @Override protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String userName;
        boolean isAdmin;

        resp.getWriter().println("Log in page!");
        resp.addHeader("username", "hi");

    }
}