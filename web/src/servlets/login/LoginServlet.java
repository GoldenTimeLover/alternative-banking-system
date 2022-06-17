package servlets.login;
import core.engine.ABSEngine;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import utils.ServerUtils;
import users.UserManager;


import java.io.IOException;

@WebServlet(name = "LoginServlet" , urlPatterns = "/user/login")
public class LoginServlet extends HttpServlet {

    @Override protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {


        String adminUsername = req.getParameter("adminUsername");
        String customerUserName = req.getParameter("customerUsername");
        String userName;
        UserManager userManager = ServerUtils.getUserManager(getServletContext());
        ABSEngine engine = ServerUtils.getEngine(getServletContext());


        // if user trying to log in as admin
        if(adminUsername != null){
            userName = adminUsername;
            if(!userManager.getAdminName().equals("")){
                resp.getWriter().println("Sorry! Only one admin can log in at a time.");
                resp.setStatus(400);
            }
            else if(userManager.isUserLoggedIn(userName)){
                resp.getWriter().println("Sorry! This username is taken!\nPlease try another!");
                resp.setStatus(400);
            }
            else{

                userManager.addAdmin(userName,engine);
                resp.getWriter().println("hello admin " + userName);

                resp.setStatus(200);
            }


        }
        // if user trying to log in as customer
        else if(customerUserName != null){
            userName = customerUserName;
            if(userManager.isUserLoggedIn(userName)){
                resp.getWriter().println("Sorry! This username is taken!\nPlease try another!");
                resp.setStatus(400);
            }
            else{
                userManager.addCustomer(userName,engine);
                resp.getWriter().println("hello simple user " + userName);
                resp.setStatus(200);
            }
        }else{
            resp.setStatus(400);
            resp.getWriter().println("Did not provide a user name parameter!");

        }


    }

}