package servlets.login;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import users.UserManager;
import utils.ServerUtils;

//import managers.UserManager;
//import utils.ServletUtils;

import java.io.IOException;

@WebServlet(name = "LoginServlet" , urlPatterns = "/user/login")
public class LoginServlet extends HttpServlet {

    @Override protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        boolean isAdmin = false;

        String adminUsername = req.getParameter("adminUsername");
        String customerUserName = req.getParameter("customerUsername");
        String userName = "";
        if(adminUsername!=null || customerUserName != null ){

            if(adminUsername != null){
                isAdmin = true;
                userName = adminUsername;
            }else{
                userName = customerUserName;
            }
        }else{
            resp.setStatus(400);
            resp.getWriter().println("Did not provide a user name parameter!");
            return;
        }

        resp.setStatus(200);
        return;
//        userName = userName.trim();
//        userName = userName.toLowerCase();
//
//        UserManager userManager = ServerUtils.getUserManager(getServletContext());
//
//        if(userManager.isUserExists(userName)){
//            resp.getWriter().println("Sorry! This username is taken!\nPlease try another!");
//            resp.setStatus(400);
//        }
//        else {
//            if (isAdmin) {
//                userManager.addAdmin(userName);
//                resp.getWriter().println("hello admin " + userName);
//            } else {
//                userManager.addCustomer(userName);
//                resp.getWriter().println("hello simple user " + userName);
//            }
//
//            resp.setStatus(200);
//        }
    }
}