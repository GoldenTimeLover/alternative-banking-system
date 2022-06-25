package servlets.login;
import core.engine.ABSEngine;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import jakarta.servlet.http.HttpSession;
import utils.ServerUtils;
import users.UserManager;


import java.io.IOException;

@WebServlet(name = "LoginServlet" , urlPatterns = "/user/login")
public class LoginServlet extends HttpServlet {

    @Override protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {



        //if there is a session
        HttpSession session = req.getSession(false);
        Object sessionAttribute = session != null ? session.getAttribute("username") : null;
        String sessionUserName = sessionAttribute != null ? sessionAttribute.toString() : null;

        // user manager
        UserManager userManager = ServerUtils.getUserManager(getServletContext());

        String adminUsername = req.getParameter("adminUsername");
        String customerUserName = req.getParameter("customerUsername");
        String userName;

        ABSEngine engine = ServerUtils.getEngine(getServletContext());

        if (sessionUserName == null) { //user is not logged in yet

            // if user trying to log in as admin
            if(adminUsername != null){
                userName = adminUsername.trim();
                if(!userManager.getAdminName().equals("")){
                    resp.getWriter().println("Sorry! Only one admin can log in at a time.");
                    resp.setStatus(400);
                }
                else if(userManager.isUserLoggedIn(userName)){
                    resp.getWriter().println("Sorry! This username is taken!\nPlease try another!");
                    resp.setStatus(400);
                }
                else{

                    System.out.println("added admin" + userName);
                    userManager.addAdmin(userName,engine);
                    resp.getWriter().println("hello admin " + userName);
                    req.getSession(true).setAttribute("username", userName);

                    //redirect the request to the chat room - in order to actually change the URL
                    resp.setStatus(HttpServletResponse.SC_OK);
                    resp.setStatus(200);
                }


            }
            else if(customerUserName != null){
                userName = customerUserName.trim();
                if(userManager.isUserLoggedIn(userName)){
                    resp.getWriter().println("Sorry! This username is taken!\nPlease try another!");
                    resp.setStatus(400);
                }
                else{
                    userManager.addCustomer(userName,engine);
                    System.out.println("logged in" + userName);
                    req.getSession(true).setAttribute("username", userName);
                    resp.setStatus(200);
                }
            }
        } else {
            //user is already logged in
            resp.setStatus(HttpServletResponse.SC_OK);
        }




    }

}