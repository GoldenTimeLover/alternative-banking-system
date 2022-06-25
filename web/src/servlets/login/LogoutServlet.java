package servlets.login;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServerUtils;
import users.UserManager;

import java.io.IOException;

@WebServlet(name = "LogoutServlet" , urlPatterns = "/user/logout")
public class LogoutServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {


        String username = request.getParameter("username");
        UserManager userManager = ServerUtils.getUserManager(getServletContext());


        // if customer remove from list
        if (username != null) {
            userManager.logoutUser(username);
            request.getSession().invalidate();
            response.getWriter().println("The user " +username+" logged out.");
        }
        //if the user trying to log out is the admin
        if(username != null && userManager.getAdminName().equals(username)){
            userManager.setAdminName("");
            request.getSession().invalidate();
            response.getWriter().println("The admin logged out.");
        }
    }

}
